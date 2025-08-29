/*
 * Copyright (c) 2025 ATHARV GAWANDE. All rights reserved.
 * Proprietary and confidential.
 */
package com.example.branflu.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Deliverables {
    INSTAGRAM_POST("Instagram Post"),
    INSTAGRAM_REEL("Instagram reel"),
    INSTAGRAM_STORY("Instagram Story"),
    YOUTUBE_VIDEO("YouTube Video"),
    YOUTUBE_SHORT("YouTube Short"),;


    private final String value;

    Deliverables(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Deliverables fromValue(String value) {
        for (Deliverables d : Deliverables.values()) {
            if (d.value.equalsIgnoreCase(value)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Invalid deliverable: " + value);
    }
}
