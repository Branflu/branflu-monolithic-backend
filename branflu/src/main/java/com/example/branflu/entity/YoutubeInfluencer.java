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
package com.example.branflu.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class YoutubeInfluencer {

    @Id
    @Column(name = "channel_id", nullable = false)
    private String channelId;

    @Column(length = 512)
    private String imageUrl;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String analyticsJson; // Optional: Raw JSON from YouTube API

    private LocalDateTime lastFetched; // Last analytics fetch timestamp

    @OneToMany(mappedBy = "influencer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<YoutubeAnalytics> analytics;

    private Long subscriberCount;
    private Long videoCount;
    private Long totalViews;

}
