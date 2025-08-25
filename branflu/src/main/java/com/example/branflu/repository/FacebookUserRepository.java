package com.example.branflu.repository;

import com.example.branflu.entity.FacebookUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacebookUserRepository extends JpaRepository<FacebookUser, Long> {
    FacebookUser findByFacebookUserId(String facebookUserId);
}
