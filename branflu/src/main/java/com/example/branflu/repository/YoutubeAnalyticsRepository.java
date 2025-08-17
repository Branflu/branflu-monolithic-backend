package com.example.branflu.repository;

import com.example.branflu.entity.YoutubeAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface YoutubeAnalyticsRepository extends JpaRepository<YoutubeAnalytics, Long> {

    List<YoutubeAnalytics> findByInfluencer_ChannelIdOrderByDateAsc(String channelId);

    boolean existsByInfluencer_ChannelIdAndDate(String channelId, LocalDate date);
}
