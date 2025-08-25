package com.example.branflu.repository;

import com.example.branflu.entity.YoutubeInfluencer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeRepository extends JpaRepository<YoutubeInfluencer, String> {
    YoutubeInfluencer findByChannelId(String channelId);
}
