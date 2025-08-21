package com.example.branflu.transformer;

import com.example.branflu.entity.Business;
import com.example.branflu.payload.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BusinessToBusinessResponseTransformer {
    //    private final PasswordEncoder passwordEncoder;
    public UserResponse transform(Business business){
        log.info("{} >> tranformer >> transforming -> profile: {}", getClass().getSimpleName(), business);
        return UserResponse.builder()
                .userId(business.getUserId())
                .name(business.getName())
                .payPalEmail(business.getPayPalEmail())
                .role(business.getRole()) // 👈 Include the role here
                .createdAt(business.getCreatedAt())
                .build();
    }


}
