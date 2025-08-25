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
package com.example.branflu.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String resource,String fieldName,String fieldValue) {
        super(String.format("% not found with %s: '%s'",resource,fieldName,fieldValue));
    }
}
