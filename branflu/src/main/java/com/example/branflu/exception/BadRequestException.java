package com.example.branflu.exception;

import com.example.branflu.enums.ErrorData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@NoArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private ErrorData errorData;

    public BadRequestException(ErrorData errorData) {
        this.setErrorData(errorData);
    }



}
