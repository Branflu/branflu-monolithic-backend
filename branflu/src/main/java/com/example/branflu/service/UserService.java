package com.example.branflu.service;

import com.example.branflu.payload.request.BusinessRequest;
import com.example.branflu.payload.request.InfluencerRequest;
import com.example.branflu.payload.response.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {
    ResponseEntity<UserResponse> registerAsInfluencer(InfluencerRequest influencerRequest);
    ResponseEntity<UserResponse> registerAsBusiness(BusinessRequest businessRequest);
    ResponseEntity<List<UserResponse>> getAllInfluencer();
    ResponseEntity<UserResponse> getInfluencerById(UUID userId);
    ResponseEntity<String> deleteLoggedInInfluencer();




}
