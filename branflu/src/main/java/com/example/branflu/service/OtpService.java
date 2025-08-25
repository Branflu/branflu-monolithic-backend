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
package com.example.branflu.service;

public interface OtpService {
    /**
     * Generate an OTP, persist it (hashed) with TTL, and send via email.
     *
     * @param email target email
     * @param ip    origin ip (used for rate limiting; may be null)
     */
    void generateAndSendOtp(String email, String ip);

    /**
     * Verify that supplied OTP belongs to given email and is not expired.
     * OTP should be single-use (deleted after successful verification).
     *
     * @return true if valid
     */
    boolean verifyOtp(String email, String otp);
}
