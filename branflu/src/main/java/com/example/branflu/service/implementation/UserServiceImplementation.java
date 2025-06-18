package com.example.branflu.service.implementation;

import com.example.branflu.entity.Business;
import com.example.branflu.entity.Influencer;
import com.example.branflu.entity.InfluencerPlatform;
import com.example.branflu.enums.Platform;
import com.example.branflu.enums.Role;
import com.example.branflu.exception.ResourceNotFoundException;
import com.example.branflu.payload.request.BusinessRequest;
import com.example.branflu.payload.request.InfluencerRequest;
import com.example.branflu.payload.response.UserResponse;
import com.example.branflu.repository.BusinessRepository;
import com.example.branflu.repository.InfluencerRepository;
import com.example.branflu.service.UserService;
import com.example.branflu.transformer.BusinessRequestToBusinessTransformer;
import com.example.branflu.transformer.BusinessToBusinessResponseTransformer;
import com.example.branflu.transformer.InfluencerRequestToInfluencerTransformer;
import com.example.branflu.transformer.InfluencerToInfluencerResponseTransformer;
import com.example.branflu.validator.UserRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
        for (Platform platform : influencerRequest.getPlatforms()) {
            InfluencerPlatform ip = new InfluencerPlatform();
            ip.setPlatform(platform);
            ip.setInfluencer(influencer);
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
        log.info("Starting business registration for PayPal Email: {}", businessRequest.getPayPalEmail());


        try {
            userRequestValidator.validateBusiness(businessRequest);
        } catch (Exception e) {
            log.error("Validation failed for business: {}", businessRequest, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


        Business business = businessRepository.findBusinessByPayPalEmail(businessRequest.getPayPalEmail())
                .map(existing -> {
                    log.info("Updating existing business with PayPal email: {}", businessRequest.getPayPalEmail());
                    Business updated = businessRequestToBusinessTransformer.transform(businessRequest, existing);
                    updated.setRole(Role.BUSINESS);
                    return updated;
                })
                .orElseGet(() -> {
                    log.info("Creating new business with PayPal email: {}", businessRequest.getPayPalEmail());
                    Business newBusiness = businessRequestToBusinessTransformer.transform(businessRequest);
                    newBusiness.setRole(Role.BUSINESS);
                    newBusiness.setCreatedAt(new Date());
                    return newBusiness;
                });


        Business saved = businessRepository.save(business);
        log.info("Business saved successfully with ID: {}", saved.getUserId());


        UserResponse response = businessResponseTransformer.transform(saved);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<UserResponse>> getAllInfluencer() {
        List<Influencer> allInfluencer=this.influencerRepository.findAll();

        List<UserResponse> responseList=allInfluencer.stream()
                .map(influencerToInfluencerResponseTransformer::transform)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);

    }

    @Override
    public ResponseEntity<UserResponse> getInfluencerById(UUID userId) {
        Influencer influencer = influencerRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("Influencer","userId",userId.toString()));
        UserResponse response=influencerToInfluencerResponseTransformer.transform(influencer);
        return ResponseEntity.ok(response);
    }


}
