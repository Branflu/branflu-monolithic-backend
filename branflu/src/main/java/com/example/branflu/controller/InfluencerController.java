package com.example.branflu.controller;

import com.example.branflu.payload.response.UserResponse;
import com.example.branflu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
