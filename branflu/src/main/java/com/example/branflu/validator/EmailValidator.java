package com.example.branflu.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;
import java.util.regex.Pattern;

@Component
public class EmailValidator {
    private static final Logger log = LoggerFactory.getLogger(EmailValidator.class);

    public EmailValidator (){

    }


    public void isValidEmail(String email, Supplier<? extends RuntimeException> throwableSupplier) {
        log.info("EmailValidator >> isValidEmail -> {}", email);
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        if(!pattern.matcher(email).matches()){
            throw (RuntimeException) throwableSupplier.get();
        }
    }
}
