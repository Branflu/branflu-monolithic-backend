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

import com.example.branflu.entity.InstagramUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstagramUserRepository extends JpaRepository<InstagramUser, Long> {
    InstagramUser findByInstagramUserId(String instagramUserId);
}
