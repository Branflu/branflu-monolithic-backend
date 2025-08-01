package com.example.branflu.payload.request;

import com.example.branflu.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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