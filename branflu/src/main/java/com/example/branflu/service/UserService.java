/*
 * /*
 *  * Copyright (c) 2025 ATHARV GAWANDE. All rights reserved.
 *  *
 *  * This source code is proprietary and confidential.
 *  * Unauthorized copying, modification, distribution, or use
 *  * of this file, via any medium, is strictly prohibited.
 *  *
 *  * For licensing inquiries, contact: atharvagawande19@gmail.com
 *  */
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
