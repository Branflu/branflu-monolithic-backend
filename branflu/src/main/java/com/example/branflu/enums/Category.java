package com.example.branflu.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Category fromValue(String value) {
        for (Category category : Category.values()) {
            if (category.value.equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid category: " + value);
    }
}
