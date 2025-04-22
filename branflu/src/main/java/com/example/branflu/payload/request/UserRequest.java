package com.example.branflu.payload.request;

import com.example.branflu.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
@ToString
public class UserRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String payPalEmail;
    @NotBlank
    private String password;
    @NotBlank
    private Role role;
}
