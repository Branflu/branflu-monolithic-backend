package com.example.branflu.controller;

import com.example.branflu.payload.request.InfluencerRequest;
import com.example.branflu.payload.response.UserResponse;
import com.example.branflu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/influencer/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody InfluencerRequest request
    ) {
        return userService.registerAsInfluencer(request);
    }
}
