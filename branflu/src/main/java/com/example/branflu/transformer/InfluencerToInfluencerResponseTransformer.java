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
package com.example.branflu.transformer;

import com.example.branflu.entity.Influencer;
import com.example.branflu.payload.response.PlatformLinkResponse;
import com.example.branflu.payload.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class InfluencerToInfluencerResponseTransformer {

    public UserResponse transform(Influencer influencer) {
        log.info("{} >> transformer >> transforming -> profile: {}", getClass().getSimpleName(), influencer);

        List<PlatformLinkResponse> platforms = influencer.getPlatforms().stream()
                .map(ip -> PlatformLinkResponse.builder()
                        .platform(ip.getPlatform())
                        .url(ip.getLink().getUrl())
                        .audienceCount(ip.getAudienceCount())
                        .averageViews(ip.getAverageViews())
                        .engagementRate(ip.getEngagementRate())
                        .build())
                .collect(Collectors.toList());

        return UserResponse.builder()
                .userId(influencer.getUserId())
                .name(influencer.getName())
                .payPalEmail(influencer.getPayPalEmail())
                .role(influencer.getRole())
                .createdAt(influencer.getCreatedAt())
                .build();
    }
}
