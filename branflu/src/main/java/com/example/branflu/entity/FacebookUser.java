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
    private String instagramUsername;
    @Column(name = "insta_profile_picture_url", length = 512)
    private String instagramProfilePictureUrl;
    private String instagramBio;
    private Integer instagramMediaCount;


}
