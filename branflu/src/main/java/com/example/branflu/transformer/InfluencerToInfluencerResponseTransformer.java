package com.example.branflu.transformer;

import com.example.branflu.entity.Influencer;
import com.example.branflu.payload.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InfluencerToInfluencerResponseTransformer {

//    private final PasswordEncoder passwordEncoder;
    public UserResponse transform(Influencer influencer){
        log.info("{} >> tranformer >> transforming -> profile: {}", getClass().getSimpleName(), influencer);
        return UserResponse.builder()
                .userId(influencer.getUserId())
                .name(influencer.getName())
                .payPalEmail(influencer.getPayPalEmail())
                .role(influencer.getRole()) // 👈 Include the role here
                .createdAt(influencer.getCreatedAt())
                .build();
    }


}
