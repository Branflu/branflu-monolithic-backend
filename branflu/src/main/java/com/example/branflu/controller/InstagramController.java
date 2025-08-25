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

//package com.example.branflu.controller;
//
//import com.example.branflu.entity.InstagramUser;
//import com.example.branflu.service.InstagramService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.view.RedirectView;
//
//@RestController
//@RequestMapping("/api/instagram")
//@RequiredArgsConstructor
//public class InstagramController {
//
//    private final InstagramService instagramService;
//
//    @Value("${instagram.client.id}")
//    private String clientId;
//
//    @Value("${INSTA_REDIRECT_URI}")
//    private String redirectUri;
//
//    @Value("${instagram.verify.token}")
//    private String verifyToken;
//
//    @GetMapping("/login")
//    public RedirectView loginWithInstagram() {
//        String instagramAuthUrl = "https://api.instagram.com/oauth/authorize"
//                + "?client_id=" + clientId
//                + "&redirect_uri=" + redirectUri
//                + "&scope=user_profile,user_media"
//                + "&response_type=code";
//        return new RedirectView(instagramAuthUrl);
//    }
//
//    @GetMapping("/callback")
//    public String handleInstagramCallback(@RequestParam("code") String code) {
//        return instagramService.exchangeCodeForAccessToken(code);
//    }
//
//    /**
//     * Step 3: Webhook verification
//     * Meta will hit this endpoint with challenge parameters to verify your server.
//     */
//    @GetMapping("/webhook")
//    public ResponseEntity<String> verifyWebhook(
//            @RequestParam(name = "hub.mode") String mode,
//            @RequestParam(name = "hub.verify_token") String token,
//            @RequestParam(name = "hub.challenge") String challenge
//    ) {
//        System.out.println("Received verify token from Meta: " + token);
//        System.out.println("Expected token from config: " + verifyToken);
//
//        if ("subscribe".equals(mode) && verifyToken.equals(token)) {
//            return ResponseEntity.ok(challenge);
//        } else {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Verification failed");
//        }
//    }
//
//
//    /**
//     * Step 4: Handle actual POST webhook events from Instagram
//     */
//    @PostMapping("/webhook")
//    public ResponseEntity<String> receiveWebhookEvent(@RequestBody String payload) {
//        // Log or process the payload
//        System.out.println("Instagram Webhook Event: " + payload);
//        return ResponseEntity.ok("Received");
//    }
//}
