package com.example.branflu.controller;

import com.example.branflu.service.GoogleAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@RestController
@RequestMapping("/auth/google")
@RequiredArgsConstructor
public class GoogleAuthController {

    private final GoogleAuthService googleAuthService;

    @Value("${frontend.url}")
    private String frontendUrl;

    // Step 1: Redirect to Google
    @GetMapping("/auth")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String authUrl = googleAuthService.buildAuthUrl();
        response.sendRedirect(authUrl);
    }

    // Step 2: Handle callback
    @GetMapping("/callback")
    public void handleCallback(@RequestParam("code") String code,
                               HttpServletResponse response) throws IOException {
        String jwt = googleAuthService.handleOAuthCallback(code);
        response.sendRedirect(frontendUrl + "/auth/callback?token=" + jwt);
    }

    @GetMapping("/failure")
    public ResponseEntity<String> googleFailure() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Google login failed!");
    }
}
