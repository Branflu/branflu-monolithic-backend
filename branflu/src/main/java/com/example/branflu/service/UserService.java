package com.example.branflu.service;

import com.example.branflu.payload.request.BusinessRequest;
import com.example.branflu.payload.request.InfluencerRequest;
import com.example.branflu.payload.response.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<UserResponse> registerAsInfluencer(InfluencerRequest influencerRequest);
    ResponseEntity<UserResponse> registerAsBusiness(BusinessRequest businessRequest);


}
