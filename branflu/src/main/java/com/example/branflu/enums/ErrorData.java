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
    PAYPAL_EMAIL_EXIST("BRANFLU__ERROR-2004", "PayPal email already exist"),
    LINK_INVALID("BRANFLU__ERROR-2005", "Platform link is invalid"),
    PASSWORD_MANDATORY("BRANFLU__ERROR-2006","Password is mandatory"),
    PASSWORD_INVALID("BRANFLU__2007","Password must be 8-64 characters long and include at least one uppercase, one lowercase, one number, and one special character");

    private final String code;
    private final String message;

    ErrorData(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
