package com.example.branflu.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String resource,String fieldName,String fieldValue) {
        super(String.format("% not found with %s: '%s'",resource,fieldName,fieldValue));
    }
}
