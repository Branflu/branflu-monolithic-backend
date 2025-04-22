package com.example.branflu.enums;

import lombok.Getter;

@Getter
public enum Category {
    FASHION("Fashion"),
    BEAUTY("Beauty"),
    TECH("Tech"),
    GAMING("Gaming"),
    FITNESS("Fitness"),
    FOOD("Food"),
    TRAVEL("Travel"),
    EDUCATION("Education"),
    MUSIC("Music"),
    ENTERTAINMENT("Entertainment"),
    FINANCE("Finance"),
    ART("Art"),
    PHOTOGRAPHY("Photography"),
    PETS("Pets"),
    PARENTING("Parenting"),
    LIFESTYLE("Lifestyle"),
    DIY("DIY & Crafts"),
    SPORTS("Sports");

    private final String value;

    Category(String value) {
        this.value = value;
    }
}
