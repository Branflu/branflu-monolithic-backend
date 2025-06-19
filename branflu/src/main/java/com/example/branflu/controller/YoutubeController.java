package com.example.branflu.controller;

import com.example.branflu.service.YoutubeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/youtube")
@RequiredArgsConstructor
public class YoutubeController {

    private final YoutubeService youtubeService;

    // Step 1: Redirect user to Google OAuth consent screen
    @GetMapping("/auth")
    public void redirectToGoogleAuth(HttpServletResponse response) throws IOException {
        String authUrl = youtubeService.buildAuthUrl();
        response.sendRedirect(authUrl);
    }

    // Step 2: Google redirects back here with auth code
    @GetMapping("/callback")
    public String handleOAuthCallback(@RequestParam("code") String code) {
        return youtubeService.handleOAuthCallback(code);
    }
}
