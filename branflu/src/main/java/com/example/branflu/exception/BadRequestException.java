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
