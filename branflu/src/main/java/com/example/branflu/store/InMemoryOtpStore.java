/*
 * /*
 *  * Copyright (c) 2025 ATHARV GAWANDE. All rights reserved.
 *  *
 *  * This source code is proprietary and confidential.
 *  * Unauthorized copying, modification, distribution, or use
 *  * of this file, via any medium, is strictly prohibited.
 *  *
 *  * For licensing inquiries, contact: atharvagawande19@gmail.com
 *  */
package com.example.branflu.store;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryOtpStore implements OtpStore {
    private final Map<String, OtpEntry> map = new ConcurrentHashMap<>();

    @Override
    public void put(String email, String otpHash, long ttlSeconds) {
        OtpEntry e = new OtpEntry(otpHash, Instant.now().plusSeconds(ttlSeconds));
        map.put(email.toLowerCase(), e);
    }

    @Override
    public Optional<OtpEntry> get(String email) {
        OtpEntry e = map.get(email.toLowerCase());
        if (e == null) return Optional.empty();
        if (Instant.now().isAfter(e.getExpiresAt())) {
            map.remove(email.toLowerCase());
            return Optional.empty();
        }
        return Optional.of(e);
    }

    @Override
    public void remove(String email) {
        map.remove(email.toLowerCase());
    }
}
