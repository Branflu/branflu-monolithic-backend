package com.example.branflu.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTAuthenticationRequest {
    private String payPalEmail;
    private String password;
}
