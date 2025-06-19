package com.example.branflu.repository;

import com.example.branflu.entity.YoutubeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeRepository extends JpaRepository<YoutubeEntity, String> {
}
