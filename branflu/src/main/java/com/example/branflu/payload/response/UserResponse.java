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
package com.example.branflu.payload.response;

import com.example.branflu.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
@ToString
public class UserResponse {
    private UUID userId;
    private String name;
    private String payPalEmail;
    private Role role;
    private Date createdAt;
    private String token;
}