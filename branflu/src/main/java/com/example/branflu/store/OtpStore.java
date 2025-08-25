package com.example.branflu.store;

import java.util.Optional;

public interface OtpStore {
    void put(String email, String otpHash, long ttlSeconds);
    Optional<OtpEntry> get(String email);
    void remove(String email);
}
