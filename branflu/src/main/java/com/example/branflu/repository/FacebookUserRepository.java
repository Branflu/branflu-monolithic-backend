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
package com.example.branflu.repository;

import com.example.branflu.entity.FacebookUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacebookUserRepository extends JpaRepository<FacebookUser, Long> {
    FacebookUser findByFacebookUserId(String facebookUserId);
}
