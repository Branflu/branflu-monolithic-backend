package com.example.branflu.service;

import com.example.branflu.entity.YoutubeAnalytics;
import com.example.branflu.entity.YoutubeInfluencer;
import com.example.branflu.repository.YoutubeAnalyticsRepository;
import com.example.branflu.repository.YoutubeRepository;
import com.example.branflu.security.CustomUserDetailsService;
import com.example.branflu.security.JwtService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class YoutubeService {

    private static final Logger log = LoggerFactory.getLogger(YoutubeService.class);

    @Value("${youtube.client.id}")
    private String clientId;

    @Value("${youtube.client.secret}")
    private String clientSecret;

    @Value("${youtube.redirect.uri}")
    private String redirectUri;

    private final YoutubeRepository influencerRepo;
    private final YoutubeAnalyticsRepository analyticsRepo;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public String buildAuthUrl() {
        try {
            String encodedRedirectUri = URLEncoder.encode(redirectUri, "UTF-8");
            String scopes = URLEncoder.encode(
                    "https://www.googleapis.com/auth/youtube.readonly " +
                            "https://www.googleapis.com/auth/yt-analytics.readonly",
                    "UTF-8"
            );

            return "https://accounts.google.com/o/oauth2/v2/auth" +
                    "?client_id=" + clientId +
                    "&redirect_uri=" + encodedRedirectUri +
                    "&response_type=code" +
                    "&scope=" + scopes +
                    "&access_type=offline" +
                    "&prompt=consent";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error encoding redirect URI", e);
        }
    }

    public String handleOAuthCallback(String code) {
        RestTemplate restTemplate = new RestTemplate();

        // Exchange code for tokens
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.add("code", code);
        tokenRequest.add("client_id", clientId);
        tokenRequest.add("client_secret", clientSecret);
        tokenRequest.add("redirect_uri", redirectUri);
        tokenRequest.add("grant_type", "authorization_code");

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token",
                new HttpEntity<>(tokenRequest, tokenHeaders),
                Map.class
        );

        if (!tokenResponse.getStatusCode().is2xxSuccessful() || tokenResponse.getBody() == null) {
            throw new RuntimeException("Failed to exchange code for token");
        }

        String accessToken = (String) tokenResponse.getBody().get("access_token");
        if (accessToken == null) throw new RuntimeException("No access token returned from Google");

        HttpHeaders youtubeHeaders = new HttpHeaders();
        youtubeHeaders.setBearerAuth(accessToken);

        // Fetch channel info
        String channelUrl = "https://www.googleapis.com/youtube/v3/channels?part=snippet,statistics,contentDetails&mine=true";
        ResponseEntity<Map> youtubeResponse = restTemplate.exchange(channelUrl, HttpMethod.GET, new HttpEntity<>(youtubeHeaders), Map.class);

        if (youtubeResponse.getBody() == null || !youtubeResponse.getBody().containsKey("items"))
            throw new RuntimeException("No channel info returned from YouTube");

        List<Map<String, Object>> items = (List<Map<String, Object>>) youtubeResponse.getBody().get("items");
        if (items.isEmpty()) throw new RuntimeException("No channel items found");

        Map<String, Object> channel = items.get(0);
        Map<String, Object> snippet = (Map<String, Object>) channel.get("snippet");
        Map<String, Object> statistics = (Map<String, Object>) channel.get("statistics");

        String channelId = (String) channel.get("id");
        String title = (String) snippet.get("title");
        String imageUrl = Optional.ofNullable((Map<String, Object>) snippet.get("thumbnails"))
                .map(thumbs -> (Map<String, Object>) thumbs.getOrDefault("high", thumbs.get("default")))
                .map(high -> (String) high.get("url"))
                .orElse(null);

        Long subscriberCount = statistics.get("subscriberCount") != null ? Long.parseLong((String) statistics.get("subscriberCount")) : 0L;
        Long videoCount = statistics.get("videoCount") != null ? Long.parseLong((String) statistics.get("videoCount")) : 0L;
        Long totalViews = statistics.get("viewCount") != null ? Long.parseLong((String) statistics.get("viewCount")) : 0L;

        // Save or update influencer
        YoutubeInfluencer influencer = influencerRepo.findById(channelId).orElse(new YoutubeInfluencer());
        influencer.setChannelId(channelId);
        influencer.setTitle(title);
        influencer.setImageUrl(imageUrl);
        influencer.setSubscriberCount(subscriberCount);
        influencer.setVideoCount(videoCount);
        influencer.setTotalViews(totalViews);
        influencer.setAnalyticsJson(null);
        influencer.setLastFetched(LocalDateTime.now());
        influencerRepo.save(influencer);

        // Fetch and save analytics (last 90 days)
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(90);
        DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE;

        String analyticsUrl = UriComponentsBuilder.fromHttpUrl("https://youtubeanalytics.googleapis.com/v2/reports")
                .queryParam("ids", "channel==MINE")
                .queryParam("startDate", start.format(fmt))
                .queryParam("endDate", end.format(fmt))
                .queryParam("metrics", "views,likes,comments,subscribersGained,estimatedMinutesWatched")
                .queryParam("dimensions", "day")
                .queryParam("sort", "day")
                .build()
                .toUriString();

        ResponseEntity<JsonNode> analyticsResponse = restTemplate.exchange(
                analyticsUrl,
                HttpMethod.GET,
                new HttpEntity<>(youtubeHeaders),
                JsonNode.class
        );

        if (analyticsResponse.getStatusCode().is2xxSuccessful() && analyticsResponse.getBody() != null) {
            JsonNode rows = analyticsResponse.getBody().get("rows");
            if (rows != null && rows.isArray()) {
                for (JsonNode row : rows) {
                    LocalDate date = LocalDate.parse(row.get(0).asText());
                    if (analyticsRepo.existsByInfluencer_ChannelIdAndDate(channelId, date)) continue;

                    YoutubeAnalytics snapshot = new YoutubeAnalytics();
                    snapshot.setInfluencer(influencer);
                    snapshot.setDate(date);
                    snapshot.setViews(row.get(1).isNull() ? 0L : row.get(1).asLong());
                    snapshot.setLikes(row.get(2).isNull() ? 0L : row.get(2).asLong());
                    snapshot.setComments(row.get(3).isNull() ? 0L : row.get(3).asLong());
                    snapshot.setSubscribersGained(row.get(4).isNull() ? 0L : row.get(4).asLong());
                    snapshot.setEstimatedMinutesWatched(row.get(5).isNull() ? 0L : row.get(5).asLong());
                    analyticsRepo.save(snapshot);
                }
            }
        }

        UserDetails userDetails = userDetailsService.loadInfluencerByChannelId(channelId);
        return jwtService.generateToken(userDetails);
    }

    public YoutubeInfluencer getInfluencerByChannelId(String channelId) {
        return influencerRepo.findById(channelId).orElse(null);
    }

    public JsonNode getAnalyticsForGraph(String channelId) {
        List<YoutubeAnalytics> data = analyticsRepo.findByInfluencer_ChannelIdOrderByDateAsc(channelId);
        if (data == null) data = new ArrayList<>();
        return objectMapper.valueToTree(data);
    }
}
