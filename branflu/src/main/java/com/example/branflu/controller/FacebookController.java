package com.example.branflu.controller;

import com.example.branflu.entity.FacebookUser;
import com.example.branflu.service.FacebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/facebook")
@RequiredArgsConstructor
public class FacebookController {

    private final FacebookService facebookService;

    @Value("${facebook.client.id}")
    private String clientId;

    @Value("${FACEBOOK_REDIRECT_URI}")
    private String redirectUri;

    @GetMapping("/login")
    public RedirectView loginWithFacebook() {
        String facebookAuthUrl = "https://www.facebook.com/v18.0/dialog/oauth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&scope=email,public_profile,pages_show_list,pages_read_engagement,instagram_basic,instagram_content_publish,instagram_manage_insights"
                + "&response_type=code";
        return new RedirectView(facebookAuthUrl);
    }

    @GetMapping("/callback")
    public FacebookUser handleFacebookCallback(@RequestParam("code") String code) {
        String accessToken = facebookService.getFacebookAccessToken(code);
        return facebookService.fetchAndSaveUser(accessToken);
    }
}
