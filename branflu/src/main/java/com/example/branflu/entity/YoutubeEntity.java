package com.example.branflu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class YoutubeEntity {

    @Id
    private String channelId;
    @Column(length = 512)
    private String imageUrl;
    private String title;
    private Long subscribers;
    private Long views;
    private Long videos;
    private double engagementRate;

}
