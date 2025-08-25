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
package com.example.branflu.exception;

import com.example.branflu.enums.ErrorData;

public class BadRequestException extends RuntimeException {
    private final ErrorData errorData;

    public BadRequestException(ErrorData errorData) {
        super(errorData.getMessage());  // 👈 Use the message here
        this.errorData = errorData;
    }

    public ErrorData getErrorData() {
        return errorData;
    }
}
