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
import com.example.branflu.service.implementation.InstagramAnalyticsService;
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
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public ResponseEntity<UserResponse> registerAsBusiness(BusinessRequest businessRequest) {
        log.info("✅ Inside registerAsBusiness service");
        log.info("📨 Business Name: {}", businessRequest.getName());
        log.info("📨 PayPal Email (Request): {}", businessRequest.getPayPalEmail());

        // Step 1: Get logged-in user's email if present
        String authenticatedEmail = UserContextUtil.getAuthenticatedEmail();
        log.info("🔐 Authenticated Email from JWT (if any): {}", authenticatedEmail);

        // Step 2: Validate input request
        try {
            userRequestValidator.validateBusiness(businessRequest);
        } catch (Exception e) {
            log.error("❌ Validation failed for business: {}", businessRequest, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Step 3: Check if business already exists
        Optional<Business> optionalBusiness = businessRepository.findBusinessByPayPalEmail(businessRequest.getPayPalEmail());

        if (optionalBusiness.isPresent()) {
            Business existing = optionalBusiness.get();

            // ✅ Allow update only if user is logged in AND owns the business
            if (authenticatedEmail == null || !authenticatedEmail.equalsIgnoreCase(existing.getPayPalEmail())) {
                log.warn("❌ Unauthorized update attempt for PayPal Email: {}", businessRequest.getPayPalEmail());
                throw new CustomException(ErrorData.PAYPAL_EMAIL_EXIST.getMessage());
            }

            // ✅ Perform update
            log.info("🔁 Updating existing business with PayPal email: {}", existing.getPayPalEmail());
            Business updated = businessRequestToBusinessTransformer.transform(businessRequest, existing);
            updated.setRole(Role.BUSINESS);

            Business saved = businessRepository.save(updated);
            log.info("✅ Business updated successfully with ID: {}", saved.getUserId());

            UserResponse response = businessResponseTransformer.transform(saved);
            return ResponseEntity.ok(response);
        }

        // Step 4: If business does not exist — allow anyone to register
        log.info("🆕 Creating new business with PayPal email: {}", businessRequest.getPayPalEmail());
        Business newBusiness = businessRequestToBusinessTransformer.transform(businessRequest);
        newBusiness.setRole(Role.BUSINESS);
        newBusiness.setCreatedAt(new Date());
        newBusiness.setPassword(passwordEncoder.encode(businessRequest.getPassword()));

        Business saved = businessRepository.save(newBusiness);
        log.info("✅ New business saved successfully with ID: {}", saved.getUserId());

        UserResponse response = businessResponseTransformer.transform(saved);
        return ResponseEntity.ok(response);
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
