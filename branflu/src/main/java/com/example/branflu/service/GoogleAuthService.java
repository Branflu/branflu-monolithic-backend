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

import com.example.branflu.entity.Business;
import com.example.branflu.enums.Role;
import com.example.branflu.repository.BusinessRepository;
import com.example.branflu.security.CustomUserDetailsService;
import com.example.branflu.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final BusinessRepository businessRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    // Step 1: Build Google Auth URL
    public String buildAuthUrl() {
        String scope = URLEncoder.encode("openid profile email", StandardCharsets.UTF_8);
        String encodedRedirect = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);

        return "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=" + clientId +
                "&redirect_uri=" + encodedRedirect +
                "&response_type=code" +
                "&scope=" + scope +
                "&access_type=offline" +
                "&prompt=consent";
    }

    // Step 2: Handle callback
    public String handleOAuthCallback(String code) {
        RestTemplate restTemplate = new RestTemplate();

        // Exchange code for token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("redirect_uri", redirectUri);
        form.add("grant_type", "authorization_code");

        ResponseEntity<Map> tokenResponse = restTemplate.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                new HttpEntity<>(form, headers),
                Map.class
        );

        if (!tokenResponse.getStatusCode().is2xxSuccessful() || tokenResponse.getBody() == null) {
            throw new RuntimeException("Failed to exchange code for token");
        }

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // Fetch user info
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(accessToken);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                "https://openidconnect.googleapis.com/v1/userinfo",
                HttpMethod.GET,
                new HttpEntity<>(userHeaders),
                Map.class
        );

        Map<String, Object> userInfo = userInfoResponse.getBody();
        if (userInfo == null || userInfo.get("email") == null) {
            throw new RuntimeException("Failed to fetch user info");
        }

        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        // Save or find business
        Business business = businessRepository.findBusinessByPayPalEmail(email)
                .orElseGet(() -> {
                    Business newBusiness = new Business();
                    newBusiness.setName(name);
                    newBusiness.setPayPalEmail(email);
                    newBusiness.setRole(Role.BUSINESS);
                    newBusiness.setCreatedAt(new Date());
                    newBusiness.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    return businessRepository.save(newBusiness);
                });

        // Load UserDetails and issue JWT
        UserDetails userDetails = userDetailsService.loadUserByEmail(email);
        return jwtService.generateToken(userDetails);
    }
}
