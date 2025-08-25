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

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Table(name = "youtube_analytics")
@Getter
@Setter
@DynamicUpdate
public class YoutubeAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    @JsonBackReference
    private YoutubeInfluencer influencer;

    private LocalDate date;
    private Long views;
    private Long likes;
    private Long comments;
    private Long subscribersGained;
    private Long estimatedMinutesWatched;

    public String getChannelId() {
        return influencer != null ? influencer.getChannelId() : null;
    }
}
