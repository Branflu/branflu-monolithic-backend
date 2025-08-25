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

//package com.example.branflu.security;
//
//import com.example.branflu.entity.Business;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import org.springframework.security.core.GrantedAuthority;
//
//
//import java.util.Collection;
//
//@Getter
//@AllArgsConstructor
//public class AuthenticatedUser implements businessDetails {
//
//    private final Business business;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return business.getRole().getAuthorities(); // or return Collections.singleton(new SimpleGrantedAuthority(...));
//    }
//
//    @Override
//    public String getPassword() {
//        return business.getPassword();  // hashed password
//    }
//
//    @Override
//    public String getbusinessname() {
//        return business.getEmail();  // or any unique identifier
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true; // customize as per your requirements
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return business.isEnabled();  // adjust if needed
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;  // customize if needed
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return business.isEnabled();
//    }
//}
