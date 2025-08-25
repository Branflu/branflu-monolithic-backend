//package com.example.branflu.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//
//@Service
//@RequiredArgsConstructor
//public class InstagramService {
//
//    @Value("${instagram.client.id}")
//    private String clientId;
//
//    @Value("${instagram.client.secret}")
//    private String clientSecret;
//
//    @Value("${INSTA_REDIRECT_URI}")
//    private String redirectUri;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public String exchangeCodeForAccessToken(String code) {
//        String tokenUrl = "https://api.instagram.com/oauth/access_token";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("client_id", clientId);
//        body.add("client_secret", clientSecret);
//        body.add("grant_type", "authorization_code");
//        body.add("redirect_uri", redirectUri);
//        body.add("code", code);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);
//        return response.getBody();
//    }
//}
