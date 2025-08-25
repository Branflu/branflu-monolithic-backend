/*
 * /*
 *  * Copyright (c) 2025 ATHARV GAWANDE. All rights reserved.
 *  *
 *  * This source code is proprietary and confidential.
 *  * Unauthorized copying, modification, distribution, or use
 *  * of this file, via any medium, is strictly prohibited.
 *  *
 *  * For licensing inquiries, contact: atharvagawande19@gmail.com
 *  */
 package com.example.branflu.controller;

import com.example.branflu.entity.YoutubeInfluencer;
import com.example.branflu.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/youtube")
@RequiredArgsConstructor
public class YoutubeController {

    @Value("${frontend.url}")
    private String frontendUrl;

    private final YoutubeService youtubeService;

    @GetMapping("/auth")
    public void redirectToGoogleAuth(HttpServletResponse response) throws IOException {
        response.sendRedirect(youtubeService.buildAuthUrl());
    }

    @GetMapping("/callback")
    public void handleOAuthCallback(@RequestParam("code") String code,
                                    HttpServletResponse response) throws IOException {
        String jwtToken = youtubeService.handleOAuthCallback(code);
        response.sendRedirect(frontendUrl + "/login-success?token=" + jwtToken);
    }

    @GetMapping("/influencer/{channelId}")
    public ResponseEntity<?> getInfluencerData(@PathVariable String channelId) {
        YoutubeInfluencer influencer = youtubeService.getInfluencerByChannelId(channelId);
        if (influencer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Influencer not found"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("channelId", influencer.getChannelId());
        response.put("name", influencer.getTitle());
        response.put("imageUrl", influencer.getImageUrl());
        response.put("subscriberCount", influencer.getSubscriberCount());
        response.put("videoCount", influencer.getVideoCount());
        response.put("totalViews", influencer.getTotalViews());
        response.put("analytics", youtubeService.getAnalyticsForGraph(channelId));

        return ResponseEntity.ok(response);
    }


}
