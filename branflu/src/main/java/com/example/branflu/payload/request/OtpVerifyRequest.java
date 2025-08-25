package com.example.branflu.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpVerifyRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String otp;
}
