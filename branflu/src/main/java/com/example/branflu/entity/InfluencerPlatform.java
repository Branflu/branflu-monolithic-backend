package com.example.branflu.entity;

import com.example.branflu.enums.Platform;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfluencerPlatform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "influencer_id", nullable = false)
    private Influencer influencer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "link_id", referencedColumnName = "linkID", unique = true)
    private Link link;

    private Long audienceCount;
    private Long averageViews;
    private Double engagementRate;
}
