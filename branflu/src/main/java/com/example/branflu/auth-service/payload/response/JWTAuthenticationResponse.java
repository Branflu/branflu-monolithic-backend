package com.example.branflu.payload.response;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JWTAuthenticationResponse {
    private String token;
    private String message;

    public JWTAuthenticationResponse(String message) {
        this.message = message;
    }


}
