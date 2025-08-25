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
 package com.example.branflu.controller;

import com.example.branflu.payload.response.UserResponse;
import com.example.branflu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/influencer")
public class InfluencerController {
    private UserService userService;

    @Autowired
    public InfluencerController (UserService userService){
        this.userService=userService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllInfluencers() {
        return userService.getAllInfluencer();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getInfluencerById(@PathVariable UUID userId) {
        return userService.getInfluencerById(userId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INFLUENCER')")
    public ResponseEntity<String> deleteLoggedInInfluencer() {
        return userService.deleteLoggedInInfluencer();
    }






}
