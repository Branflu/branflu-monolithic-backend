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

    // ⛔ List of paths that don't require JWT filtering
    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/influencer/register",
            "/login/influencer",
            "/api/business/register",
            "/api/youtube/auth",
            "/api/youtube/callback",
            "/api/facebook/login",
            "/api/facebook/callback",
            "/api/instagram/callback",
            "/api/instagram/login",
            "/api/instagram/webhook"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        boolean shouldSkip = EXCLUDED_PATHS.contains(path);
        System.out.println("[JWT Filter] Request Path: " + path + " | Should Skip: " + shouldSkip);
        return shouldSkip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        System.out.println("[JWT Filter] Authorization Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("[JWT Filter] No JWT token found in header");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        System.out.println("[JWT Filter] Extracted JWT: " + jwt);

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
