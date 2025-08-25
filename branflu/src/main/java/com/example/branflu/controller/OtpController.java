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
 package com.example.branflu.controller;


import com.example.branflu.payload.request.OtpRequest;
import com.example.branflu.payload.request.OtpVerifyRequest;

import com.example.branflu.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {
    @Autowired
    private final OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@Validated @RequestBody OtpRequest req, @RequestHeader(value = "X-Forwarded-For", required = false) String xForwardedFor, @RequestHeader(value = "X-Real-IP", required = false) String xRealIp) {
        String ip = xRealIp != null ? xRealIp : xForwardedFor;
        otpService.generateAndSendOtp(req.getEmail(), ip);
        return ResponseEntity.ok().body(java.util.Map.of("ok", true, "message", "OTP sent"));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@Validated @RequestBody OtpVerifyRequest req) {
        boolean ok = otpService.verifyOtp(req.getEmail(), req.getOtp());
        if (!ok) {
            return ResponseEntity.status(400).body(java.util.Map.of("ok", false, "message", "Invalid or expired OTP"));
        }
        return ResponseEntity.ok(java.util.Map.of("ok", true, "message", "Verified"));
    }
}
