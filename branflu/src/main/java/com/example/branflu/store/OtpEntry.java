package com.example.branflu.store;

import lombok.Data;
import java.time.Instant;

@Data
public class OtpEntry {
    private final String otpHash;      // HMAC-SHA256 or similar
    private final Instant expiresAt;
    private boolean used = false;
}
