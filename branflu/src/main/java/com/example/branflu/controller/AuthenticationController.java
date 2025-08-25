package com.example.branflu.controller;

import com.example.branflu.payload.request.BusinessRequest;
import com.example.branflu.payload.request.InfluencerRequest;
import com.example.branflu.payload.request.JWTAuthenticationRequest;
import com.example.branflu.payload.response.JWTAuthenticationResponse;
import com.example.branflu.payload.response.UserResponse;
import com.example.branflu.security.CustomUserDetailsService;


import com.example.branflu.security.JwtService;
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
@CrossOrigin(origins = {"http://localhost:3000", "https://native-violently-imp.ngrok-free.app"})
@RequestMapping("/api")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;


    @PostMapping("/influencer/register")
    public ResponseEntity<UserResponse> registerAsInfluencer(
            @RequestBody InfluencerRequest request
    ) {
        return userService.registerAsInfluencer(request);
    }
    @PostMapping("/business/register")
    public ResponseEntity<UserResponse> registerAsBusiness(@RequestBody BusinessRequest request) {
        System.out.println("➡️ Received registerAsBusiness request");
        System.out.println("📦 Payload: " + request);
        return userService.registerAsBusiness(request);
    }

    @PostMapping("/login")
    public ResponseEntity<JWTAuthenticationResponse> createToken(@RequestBody JWTAuthenticationRequest jwtAuthenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtAuthenticationRequest.getPayPalEmail(),
                            jwtAuthenticationRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new JWTAuthenticationResponse("Invalid username or password"));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtAuthenticationRequest.getPayPalEmail());
        String token = jwtService.generateToken(userDetails);

        JWTAuthenticationResponse jwtAuthenticationResponse = new JWTAuthenticationResponse();
        jwtAuthenticationResponse.setToken(token);
        return new ResponseEntity<>(jwtAuthenticationResponse, HttpStatus.CREATED);
    }






}
