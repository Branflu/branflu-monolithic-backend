package com.example.branflu.controller;

import com.example.branflu.entity.FacebookUser;
import com.example.branflu.security.CustomUserDetailsService;
import com.example.branflu.service.FacebookService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
@RequestMapping("/api/facebook")
@RequiredArgsConstructor
public class FacebookController {

    private final FacebookService facebookService;
    private final CustomUserDetailsService userDetailsService;

    @Value("${facebook.client.id}")
    private String clientId;

    @Value("${FACEBOOK_REDIRECT_URI}")
    private String redirectUri;

    @Value("${frontend.url}")
    private String frontendUrl;

    // 1. Redirect user to Facebook login
    @GetMapping("/login")
    public RedirectView loginWithFacebook() {
        String facebookAuthUrl = "https://www.facebook.com/v18.0/dialog/oauth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&scope=email,public_profile,pages_show_list,pages_read_engagement,instagram_basic,instagram_manage_insights"
                + "&response_type=code";
        return new RedirectView(facebookAuthUrl);
    }

    // 2. Handle Facebook OAuth callback
    @GetMapping("/callback")
    public void handleFacebookCallback(@RequestParam("code") String code,
                                       HttpServletResponse response) throws IOException {
        String jwtToken = facebookService.handleOAuthCallback(code);
        response.sendRedirect(frontendUrl + "/login-success?token=" + jwtToken);
    }

    // 3. Optional: Add an endpoint to fetch the latest data if user is already authenticated
    @GetMapping("/refresh")
    public FacebookUser refreshUserData(@RequestParam("access_token") String accessToken) {
        return facebookService.fetchAndSaveUser(accessToken);
    }
}
