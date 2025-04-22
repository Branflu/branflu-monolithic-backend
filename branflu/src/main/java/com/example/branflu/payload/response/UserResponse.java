package com.example.branflu.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
@ToString
public class UserResponse {
    private UUID userId;
    private String name;
    private String payPalEmail;
    private Date createdAt;
}