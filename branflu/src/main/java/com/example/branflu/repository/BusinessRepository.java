package com.example.branflu.repository;

import com.example.branflu.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business,String> {
    Optional<Business> findBusinessByPayPalEmail(String email);
}
