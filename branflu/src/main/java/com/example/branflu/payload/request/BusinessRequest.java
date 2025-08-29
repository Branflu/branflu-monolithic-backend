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
package com.example.branflu.payload.request;

import com.example.branflu.enums.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessRequest extends UserRequest {
//    private String imageUrl;
    private String websiteUrl;
    private String Bio;
}
