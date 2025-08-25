package com.example.branflu.enums;

import lombok.Getter;

@Getter
public enum Role {
    INFLUENCER("Influencer"),
    BUSINESS("Business");

    private final String value;
    Role(String value){
        this.value=value;
    }
}
