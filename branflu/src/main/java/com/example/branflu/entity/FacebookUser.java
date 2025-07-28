package com.example.branflu.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "facebook_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacebookUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String facebookUserId;

    private String name;

    private String email;
    @Column(name = "profile_picture_url", length = 512)
    private String profilePictureUrl;

    @Column(length = 1000)
    private String accessToken;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private Integer facebookFollowersCount;  // Facebook Page followers

    private Integer instagramFollowersCount;  // Extend this when we get follower info

}
