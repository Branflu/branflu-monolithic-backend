package com.example.branflu.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "instagram")
public class InstagramConfig {
    private String userId;
    private String accessToken;
}
