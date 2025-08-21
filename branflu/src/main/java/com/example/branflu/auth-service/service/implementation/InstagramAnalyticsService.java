package com.example.branflu.service.implementation;

import com.example.branflu.configuration.InstagramConfig;
import com.example.branflu.payload.response.InstagramAnalytics;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstagramAnalyticsService {

    private final InstagramConfig config;

    public InstagramAnalytics fetchAnalyticsByInstagramId() {
        try {
            String fields = "followers_count,media_count";
            String url = String.format(
                    "https://graph.facebook.com/v18.0/%s?fields=%s&access_token=%s",
                    config.getUserId(), fields, config.getAccessToken()
            );

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);

            InstagramAnalytics analytics = new InstagramAnalytics();
            analytics.setFollowersCount(root.path("followers_count").asInt());
            analytics.setMediaCount(root.path("media_count").asInt());

            log.info("✅ Successfully fetched analytics: {}", analytics);
            return analytics;

        } catch (Exception e) {
            log.error("❌ Error fetching Instagram analytics: {}", e.getMessage(), e);
            return null;
        }
    }
}
