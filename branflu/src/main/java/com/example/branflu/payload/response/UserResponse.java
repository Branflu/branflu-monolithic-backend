package com.example.branflu.payload.response;

import com.example.branflu.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;
import java.util.List;
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
    private Role role;
    private Date createdAt;
    private List<PlatformLinkResponse> platforms;
}