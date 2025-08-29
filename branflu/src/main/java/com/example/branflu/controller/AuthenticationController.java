/*
 *  * Copyright (c) 2025 ATHARV GAWANDE. All rights reserved.
 *  *
 *  * This source code is proprietary and confidential.
 *  * Unauthorized copying, modification, distribution, or use
 *  * of this file, via any medium, is strictly prohibited.
 *  *
 *  * For licensing inquiries, contact: atharvagawande19@gmail.com
 */
package com.example.branflu.controller;

import com.example.branflu.payload.request.BusinessRequest;
import com.example.branflu.payload.request.InfluencerRequest;
import com.example.branflu.payload.request.JWTAuthenticationRequest;
import com.example.branflu.payload.response.JWTAuthenticationResponse;
import com.example.branflu.payload.response.UserResponse;
import com.example.branflu.security.CustomUserDetailsService;
import com.example.branflu.security.JwtService;
import com.example.branflu.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://native-violently-imp.ngrok-free.app"})
@RequestMapping("/api")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    // frontend URL (set in application.properties). Default kept for safety.
    @Value("${frontend.url:http://localhost:3000}")
    private String frontendUrl;

    // ------------------------
    // Influencer registration (existing behaviour)
    // ------------------------
    @PostMapping("/influencer/register")
    public ResponseEntity<UserResponse> registerAsInfluencer(@RequestBody InfluencerRequest request) {
        return userService.registerAsInfluencer(request);
    }

    // ------------------------
    // Business registration - JSON endpoint (SPA)
    // Sets HttpOnly cookie and returns 200 OK (SPA can redirect client-side)
    // ------------------------
    @PostMapping(value = "/business/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registerAsBusinessJson(@RequestBody BusinessRequest request,
                                                       HttpServletResponse response) {
        log.info("➡️ Received JSON business register request: {}", request);
        String token = userService.registerAsBusinessAndReturnJwt(request);
        addJwtCookie(response, token);
        return ResponseEntity.ok().build();
    }

    // ------------------------
    // Business registration - Form endpoint (browser form submit)
    // Sets HttpOnly cookie and redirects the browser to frontend success page
    // ------------------------
    @PostMapping(value = "/business/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void registerAsBusinessForm(@ModelAttribute BusinessRequest request,
                                       HttpServletResponse response) throws IOException {
        log.info("➡️ Received FORM business register request (form-post): {}", request);
        String token = userService.registerAsBusinessAndReturnJwt(request);
        addJwtCookie(response, token);

        // Server-side redirect to frontend success page (no token in URL)
        response.sendRedirect(frontendUrl + "/login-redirecting");
    }

    // ------------------------
    // Login (existing behaviour)
    // ------------------------
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

    // ------------------------
    // Helper: add JWT as Secure, HttpOnly cookie and set SameSite via header
    // ------------------------
    private void addJwtCookie(HttpServletResponse response, String token) {
        final int maxAgeSeconds = 60 * 60 * 24; // 1 day — adjust as required

        // Detect localhost for local dev; avoid Secure flag on localhost (http).
        boolean isLocalhost = frontendUrl.contains("localhost") || frontendUrl.contains("127.0.0.1");

        // Add normal cookie (some containers honor these attributes)
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(!isLocalhost); // true in production (requires HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeSeconds);
        response.addCookie(cookie);

        // Also add Set-Cookie header including SameSite (for containers that ignore cookie's SameSite)
        // URL-encode token for safe header transport
        String encodedToken = URLEncoder.encode(Objects.toString(token, ""), StandardCharsets.UTF_8);
        String sameSite = "Lax"; // Lax is a sensible default; use Strict if you prefer stronger protection
        String secureFlag = isLocalhost ? "" : "; Secure";

        String headerValue = String.format("jwt=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=%s%s",
                encodedToken, maxAgeSeconds, sameSite, secureFlag);

        response.addHeader("Set-Cookie", headerValue);
    }
}
