package com.example.branflu.repository;

import com.example.branflu.entity.InstagramUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstagramUserRepository extends JpaRepository<InstagramUser, Long> {
    InstagramUser findByInstagramUserId(String instagramUserId);
}
