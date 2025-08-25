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
package com.example.branflu.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/influencer/register",
            "/login/influencer",
            "/api/business/register",
            "/api/youtube/auth",
            "/api/youtube/callback",
            "/api/youtube/influencer/**",
            "/api/facebook/login",
            "/api/facebook/callback",
            "/api/instagram/callback",
            "/api/otp/send",
            "/api/otp/verify",
            "/api/login",
            "/api/instagram/webhook",
            "/auth/google/**",
            "/oauth2/authorization/google",
            "/auth/google/auth",
            "/auth/google/callback",
            "/auth/google/failure"

    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        // Exact matches
        if (EXCLUDED_PATHS.contains(path)) {
            System.out.println("[JWT Filter] Request Path: " + path + " | Should Skip: true (exact match)");
            return true;
        }

        // Dynamic match for influencer paths
        if (path.startsWith("/api/youtube/influencer/")) {
            System.out.println("[JWT Filter] Request Path: " + path + " | Should Skip: true (prefix match)");
            return true;
        }

        System.out.println("[JWT Filter] Request Path: " + path + " | Should Skip: false");
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1) Immediately allow CORS preflight (OPTIONS) requests to pass through
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            // optional: log preflight for debug
            System.out.println("[JWT Filter] OPTIONS preflight - skipping JWT processing for: " + request.getServletPath());
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        System.out.println("[JWT Filter] Method: " + request.getMethod() + " | Authorization Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("[JWT Filter] No JWT token found in header");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        System.out.println("[JWT Filter] Extracted JWT: " + (jwt.length() > 10 ? jwt.substring(0, 10) + "..." : jwt));

        try {
            username = jwtService.extractUsername(jwt);
            System.out.println("[JWT Filter] Extracted Username: " + username);
        } catch (Exception e) {
            System.out.println("[JWT Filter] Failed to extract username: " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("[JWT Filter] Loaded UserDetails: " + userDetails.getUsername());

            if (jwtService.isTokenValid(jwt, userDetails)) {
                System.out.println("[JWT Filter] Token is valid");

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println("[JWT Filter] Invalid token");
            }
        } else {
            System.out.println("[JWT Filter] Username is null or already authenticated");
        }

        filterChain.doFilter(request, response);
    }
}
