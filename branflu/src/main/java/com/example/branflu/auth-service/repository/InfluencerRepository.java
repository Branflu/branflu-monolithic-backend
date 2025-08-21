package com.example.branflu.repository;

import com.example.branflu.entity.Influencer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InfluencerRepository extends JpaRepository<Influencer, UUID> {
    Optional<Influencer> findInfluencerByPayPalEmail (String payPalEmail);


}

