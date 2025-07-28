package com.example.branflu.service;

import com.example.branflu.entity.YoutubeInfluencer;
import com.example.branflu.repository.YoutubeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class YoutubeService {

    @Value("${youtube.client.id}")
    private String clientId;

    @Value("${youtube.client.secret}")
    private String clientSecret;

    @Value("${youtube.redirect.uri}")
    private String redirectUri;

    private final YoutubeRepository repository;

    public String buildAuthUrl() {
        try {
            String encodedRedirectUri = URLEncoder.encode(redirectUri, "UTF-8");
            return "https://accounts.google.com/o/oauth2/v2/auth" +
                    "?client_id=" + clientId +
                    "&redirect_uri=" + encodedRedirectUri +
                    "&response_type=code" +
                    "&scope=" + URLEncoder.encode("https://www.googleapis.com/auth/youtube.readonly", "UTF-8") +
                    "&access_type=offline" +
                    "&prompt=consent";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error encoding redirect URI", e);
        }
    }

    public String handleOAuthCallback(String code) {
        RestTemplate restTemplate = new RestTemplate();

        // Step 1: Exchange code for access token
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.add("code", code);
        tokenRequest.add("client_id", clientId);
        tokenRequest.add("client_secret", clientSecret);
        tokenRequest.add("redirect_uri", redirectUri);
        tokenRequest.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> tokenEntity = new HttpEntity<>(tokenRequest, tokenHeaders);

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token",
                tokenEntity,
                Map.class
        );

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // Step 2: Get channel details
        HttpHeaders youtubeHeaders = new HttpHeaders();
        youtubeHeaders.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(youtubeHeaders);

        String url = "https://www.googleapis.com/youtube/v3/channels?part=snippet,statistics,contentDetails&mine=true";

        ResponseEntity<Map> youtubeResponse = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> channel = ((List<Map<String, Object>>) youtubeResponse.getBody().get("items")).get(0);
        Map<String, Object> snippet = (Map<String, Object>) channel.get("snippet");
        Map<String, Object> statistics = (Map<String, Object>) channel.get("statistics");
        Map<String, Object> contentDetails = (Map<String, Object>) channel.get("contentDetails");

        // Thumbnail image
        String imageUrl = null;
        Map<String, Object> thumbnails = (Map<String, Object>) snippet.get("thumbnails");
        if (thumbnails != null) {
            Map<String, Object> high = (Map<String, Object>) thumbnails.get("high");
            if (high != null) {
                imageUrl = (String) high.get("url");
            } else {
                Map<String, Object> defaultThumb = (Map<String, Object>) thumbnails.get("default");
                if (defaultThumb != null) {
                    imageUrl = (String) defaultThumb.get("url");
                }
            }
        }

        // Step 3: Get uploads playlist ID
        Map<String, Object> relatedPlaylists = (Map<String, Object>) contentDetails.get("relatedPlaylists");
        String uploadsPlaylistId = (String) relatedPlaylists.get("uploads");

        // Step 4: Get latest 10 video IDs
        String playlistUrl = "https://www.googleapis.com/youtube/v3/playlistItems" +
                "?part=contentDetails&playlistId=" + uploadsPlaylistId + "&maxResults=10";
        ResponseEntity<Map> playlistResponse = restTemplate.exchange(playlistUrl, HttpMethod.GET, entity, Map.class);

        List<Map<String, Object>> items = (List<Map<String, Object>>) playlistResponse.getBody().get("items");

        StringBuilder videoIdsBuilder = new StringBuilder();
        for (Map<String, Object> item : items) {
            Map<String, Object> content = (Map<String, Object>) item.get("contentDetails");
            videoIdsBuilder.append(content.get("videoId")).append(",");
        }

        String videoIds = videoIdsBuilder.length() > 0
                ? videoIdsBuilder.substring(0, videoIdsBuilder.length() - 1)
                : "";

        // Step 5: Get stats of those videos
        double engagementRate = 0.0;

        if (!videoIds.isEmpty()) {
            String statsUrl = "https://www.googleapis.com/youtube/v3/videos" +
                    "?part=statistics&id=" + videoIds;
            ResponseEntity<Map> statsResponse = restTemplate.exchange(statsUrl, HttpMethod.GET, entity, Map.class);
            List<Map<String, Object>> videos = (List<Map<String, Object>>) statsResponse.getBody().get("items");

            long totalLikes = 0;
            long totalComments = 0;
            long totalViews = 0;

            for (Map<String, Object> video : videos) {
                Map<String, Object> stats = (Map<String, Object>) video.get("statistics");
                totalLikes += Long.parseLong(stats.getOrDefault("likeCount", "0").toString());
                totalComments += Long.parseLong(stats.getOrDefault("commentCount", "0").toString());
                totalViews += Long.parseLong(stats.getOrDefault("viewCount", "0").toString());
            }

            if (totalViews > 0) {
                engagementRate = ((double) (totalLikes + totalComments) / totalViews) * 100;
                engagementRate = Math.round(engagementRate * 100.0) / 100.0;
            }
        }

        // Save to DB
        YoutubeInfluencer youTubeChannel = new YoutubeInfluencer();
        youTubeChannel.setChannelId((String) channel.get("id"));
        youTubeChannel.setTitle((String) snippet.get("title"));
        youTubeChannel.setSubscribers(Long.parseLong(statistics.get("subscriberCount").toString()));
        youTubeChannel.setViews(Long.parseLong(statistics.get("viewCount").toString()));
        youTubeChannel.setVideos(Long.parseLong(statistics.get("videoCount").toString()));
        youTubeChannel.setImageUrl(imageUrl);
        youTubeChannel.setEngagementRate(engagementRate);  // ✅ Save engagement rate

        repository.save(youTubeChannel);

        return "Data Saved Successfully";
    }
}
