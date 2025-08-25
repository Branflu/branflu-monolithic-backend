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
