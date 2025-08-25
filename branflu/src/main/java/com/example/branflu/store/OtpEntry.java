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

import lombok.Data;
import java.time.Instant;

@Data
public class OtpEntry {
    private final String otpHash;      // HMAC-SHA256 or similar
    private final Instant expiresAt;
    private boolean used = false;
}
