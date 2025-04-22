/**
 * Proprietary License Agreement
 * <p>
 * Copyright (c) 2025 Advert Pulse
 * <p>
 * This software is the confidential and proprietary property of Advert Pulse
 * and is licensed, not sold. The application owner is Advert Pulse, and the
 * developer and maintainer is Bloggios. Only authorized Bloggios administrators
 * are permitted to copy, modify, distribute, or sublicense this software under
 * the terms set forth in this agreement.
 * <p>
 * You may not:
 * 1. Copy, modify, distribute, or sublicense this software without express
 *    written permission from Advert Pulse or Bloggios.
 * 2. Reverse engineer, decompile, disassemble, or otherwise attempt to derive
 *    the source code of the software.
 * 3. Modify this license in any way, including but not limited to altering its
 *    terms, even by Advert Pulse or any other entity, without express written
 *    permission from Bloggios administrators. Bloggios is the creator of this
 *    license and retains exclusive rights to update or modify it.
 * 4. Update or modify the license without written permission from Bloggios
 *    administrators.
 * <p>
 * The software is provided "as is," and Advert Pulse makes no warranties,
 * express or implied, regarding the software, including but not limited to any
 * warranties of merchantability, fitness for a particular purpose, or
 * non-infringement.
 * <p>
 * For inquiries regarding licensing, please contact support@bloggios.com.
 */
package com.example.branflu.enums;

/*
  Developer: Rohit Parihar
  Project: ap-user-service
  GitHub: github.com/rohit-zip
  File: ErrorData
 */

import lombok.Getter;

@Getter
public enum ErrorData {

    INTERNAL_ERROR("BRANFLU__ERROR-2001", "Internal error occurred at backend"),
    NAME_MANDATORY("BRANFLU__ERROR-2002", "Name mandatory" ),
    NAME_LIMIT_EXCEED("BRANFLU__ERROR-2003", "Name length limit exceeded"),
    PAYPAL_EMAIL_INVALID("BRANFLU__ERROR-2004", "PayPal email is invalid"),
    LINK_INVALID("BRANFLU__ERROR-2005", "Platform link is invalid"),;

    private final String code;
    private final String message;

    ErrorData(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
