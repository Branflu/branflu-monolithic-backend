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
package com.example.branflu.service.implementation;

import com.example.branflu.entity.*;
import com.example.branflu.enums.ErrorData;
import com.example.branflu.enums.Platform;
import com.example.branflu.enums.Role;
import com.example.branflu.exception.CustomException;
import com.example.branflu.exception.ResourceNotFoundException;
import com.example.branflu.payload.request.BusinessRequest;
import com.example.branflu.payload.request.InfluencerRequest;
import com.example.branflu.payload.request.LinkRequest;
import com.example.branflu.payload.response.InstagramAnalytics;
import com.example.branflu.payload.response.UserResponse;
import com.example.branflu.repository.BusinessRepository;
import com.example.branflu.repository.InfluencerRepository;
import com.example.branflu.security.CustomUserDetailsService;
import com.example.branflu.security.JwtService;
import com.example.branflu.service.UserService;
import com.example.branflu.transformer.BusinessRequestToBusinessTransformer;
import com.example.branflu.transformer.BusinessToBusinessResponseTransformer;
import com.example.branflu.transformer.InfluencerRequestToInfluencerTransformer;
import com.example.branflu.transformer.InfluencerToInfluencerResponseTransformer;
import com.example.branflu.utils.InstagramUtils;
import com.example.branflu.utils.UserContextUtil;
import com.example.branflu.validator.UserRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImplementation implements UserService {

    private final InfluencerRepository influencerRepository;
    private final BusinessRepository businessRepository;
    private final UserRequestValidator userRequestValidator;
    private final InfluencerToInfluencerResponseTransformer influencerToInfluencerResponseTransformer;
    private final InfluencerRequestToInfluencerTransformer influencerRequestToInfluencerTransformer;
    private final BusinessRequestToBusinessTransformer businessRequestToBusinessTransformer;
    private final BusinessToBusinessResponseTransformer businessResponseTransformer;
    private final PasswordEncoder passwordEncoder;
    private final InstagramAnalyticsService instagramAnalyticsService;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<UserResponse> registerAsInfluencer(InfluencerRequest influencerRequest) {
        log.info("Starting influencer registration for PayPal Email: {}", influencerRequest.getPayPalEmail());

        try {
            userRequestValidator.validateInfluencer(influencerRequest);
        } catch (Exception e) {
            log.error("Validation failed for influencer: {}", influencerRequest, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Influencer influencer = influencerRepository.findInfluencerByPayPalEmail(influencerRequest.getPayPalEmail())
                .map(existing -> {
                    Influencer updated = influencerRequestToInfluencerTransformer.transform(influencerRequest, existing);
                    updated.setRole(Role.INFLUENCER);
                    return updated;
                })
                .orElseGet(() -> {
                    Influencer newInfluencer = influencerRequestToInfluencerTransformer.transform(influencerRequest);
                    newInfluencer.setRole(Role.INFLUENCER);
                    newInfluencer.setCreatedAt(new Date());
                    newInfluencer.setPassword(passwordEncoder.encode(influencerRequest.getPassword()));
                    return newInfluencer;
                });

        List<InfluencerPlatform> platformEntities = new ArrayList<>();
        List<Platform> platforms = influencerRequest.getPlatforms();
        List<LinkRequest> links = influencerRequest.getLink();

        if (platforms.size() != links.size()) {
            throw new IllegalArgumentException("Number of platforms and links must match.");
        }

        for (int i = 0; i < platforms.size(); i++) {
            Platform platform = platforms.get(i);
            String url = links.get(i).getUrl().trim();

            Link link = new Link();
            link.setUrl(url);

            InfluencerPlatform ip = new InfluencerPlatform();
            ip.setPlatform(platform);
            ip.setInfluencer(influencer);
            ip.setLink(link);

            if (platform == Platform.INSTAGRAM) {
                String username = InstagramUtils.extractUsernameFromUrl(url);
                if (username != null) {
                    InstagramAnalytics analytics = instagramAnalyticsService.fetchAnalyticsByInstagramId();
                    if (analytics != null) {
                        ip.setAudienceCount(analytics.getFollowersCount() != null ? analytics.getFollowersCount().longValue() : 0L);
                        ip.setAverageViews(analytics.getMediaCount() != null ? analytics.getMediaCount().longValue() : 0L);
                        ip.setEngagementRate(analytics.getEngagementRate() != null ? analytics.getEngagementRate() : 0.0);
                    }
                }
            }


            platformEntities.add(ip);
        }

        influencer.setPlatforms(platformEntities);

        Influencer saved = influencerRepository.save(influencer);
        log.info("Influencer saved successfully with ID: {}", saved.getUserId());

        UserResponse response = influencerToInfluencerResponseTransformer.transform(saved);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public String registerAsBusinessAndReturnJwt(BusinessRequest businessRequest) {
        log.info("✅ Inside registerAsBusiness service");

        // Normalize & validate email early
        if (businessRequest.getPayPalEmail() == null) {
            throw new CustomException("PayPal email is required");
        }
        String normalizedEmail = businessRequest.getPayPalEmail().trim().toLowerCase();
        businessRequest.setPayPalEmail(normalizedEmail);

        String authenticatedEmail = UserContextUtil.getAuthenticatedEmail();
        userRequestValidator.validateBusiness(businessRequest);

        Business saved;
        Optional<Business> optionalBusiness = businessRepository.findBusinessByPayPalEmail(normalizedEmail);

        if (optionalBusiness.isPresent()) {
            Business existing = optionalBusiness.get();

            // Ownership check — only allow update if authenticated user matches existing owner
            if (authenticatedEmail == null || !authenticatedEmail.equalsIgnoreCase(existing.getPayPalEmail())) {
                log.warn("❌ Unauthorized update attempt for PayPal Email: {}", normalizedEmail);
                throw new CustomException(ErrorData.PAYPAL_EMAIL_EXIST.getMessage());
            }

            log.info("🔁 Updating existing business: {}", existing.getPayPalEmail());
            saved = businessRequestToBusinessTransformer.transform(businessRequest, existing);
            saved.setRole(Role.BUSINESS);
            saved = businessRepository.save(saved);
        } else {
            log.info("🆕 Creating new business: {}", normalizedEmail);
            Business newBusiness = businessRequestToBusinessTransformer.transform(businessRequest);
            newBusiness.setRole(Role.BUSINESS);
            newBusiness.setCreatedAt(new Date());

            // If password provided -> encode. If not (OAuth flow) -> generate a random encoded password
            if (StringUtils.hasText(businessRequest.getPassword())) {
                newBusiness.setPassword(passwordEncoder.encode(businessRequest.getPassword()));
            } else {
                // generate a strong random password that user doesn't know (prevents null)
                String random = UUID.randomUUID().toString();
                newBusiness.setPassword(passwordEncoder.encode(random));
            }

            saved = businessRepository.save(newBusiness);
        }

        // Load user details for token generation. Try email-based loader first, fallback to username loader.
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByEmail(saved.getPayPalEmail());
        } catch (Exception e) {
            log.debug("loadUserByEmail failed, falling back to loadUserByUsername: {}", e.getMessage());
            userDetails = userDetailsService.loadUserByUsername(saved.getPayPalEmail());
        }

        // Optionally check that userDetails is enabled/valid before generating token
        String token = jwtService.generateToken(userDetails);
        log.info("🔐 Generated JWT for business: {} (userId={})", saved.getPayPalEmail(), saved.getUserId());
        return token;
    }


    @Override
    public ResponseEntity<List<UserResponse>> getAllInfluencer() {
        List<Influencer> allInfluencer = influencerRepository.findAll();

        List<UserResponse> responseList = allInfluencer.stream()
                .map(influencerToInfluencerResponseTransformer::transform)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @Override
    public ResponseEntity<UserResponse> getInfluencerById(UUID userId) {
        Influencer influencer = influencerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Influencer", "userId", userId.toString()));
        UserResponse response = influencerToInfluencerResponseTransformer.transform(influencer);
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<String> deleteLoggedInInfluencer() {
        // Get logged-in user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        log.info("Attempting to delete influencer with PayPal Email: {}", email);

        Influencer influencer = influencerRepository.findInfluencerByPayPalEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Influencer", "email", email));

        influencerRepository.delete(influencer);
        log.info("Influencer account deleted successfully: {}", email);

        return ResponseEntity.ok("Your account has been deleted successfully.");
    }


}
