package com.example.branflu.repository;

import com.example.branflu.entity.Influencer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InfluencerRepository extends JpaRepository<Influencer,String> {
    Optional<Influencer> findInfluencerByPayPalEmail (String payPalEmail);

}

