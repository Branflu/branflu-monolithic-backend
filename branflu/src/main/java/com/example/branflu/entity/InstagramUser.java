package com.example.branflu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "instagram_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstagramUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instagramUserId;

    private String username;

    private String fullName;

    private String profilePictureUrl;

    @Column(length = 1000)
    private String accessToken;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
