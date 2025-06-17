package com.example.branflu.controller;

import com.example.branflu.payload.request.BusinessRequest;
import com.example.branflu.payload.request.InfluencerRequest;
import com.example.branflu.payload.request.JWTAuthenticationRequest;
import com.example.branflu.payload.response.JWTAuthenticationResponse;
import com.example.branflu.payload.response.UserResponse;
import com.example.branflu.security.CustomUserDetailsService;
import com.example.branflu.security.JWTTokenHelper;

import com.example.branflu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenHelper jwtTokenHelper;
    private final CustomUserDetailsService userDetailsService;


    @PostMapping("/influencer/register")
    public ResponseEntity<UserResponse> registerAsInfluencer(
            @RequestBody InfluencerRequest request
    ) {
        return userService.registerAsInfluencer(request);
    }
    @PostMapping("/business/register")
    public ResponseEntity<UserResponse> registerAsBusiness(@RequestBody BusinessRequest request) {
        return userService.registerAsBusiness(request);
    }

    @PostMapping("/login/influencer")
    public ResponseEntity<JWTAuthenticationResponse> createToken(@RequestBody JWTAuthenticationRequest jwtAuthenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtAuthenticationRequest.getPayPalEMail(),
                            jwtAuthenticationRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new Exception("Invalid username or password", e);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtAuthenticationRequest.getPayPalEMail());
        String token = jwtTokenHelper.generateToken(userDetails);

        JWTAuthenticationResponse jwtAuthenticationResponse = new JWTAuthenticationResponse();
        jwtAuthenticationResponse.setToken(token);
        return new ResponseEntity<>(jwtAuthenticationResponse, HttpStatus.CREATED);
    }

}
