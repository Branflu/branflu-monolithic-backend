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
package com.example.branflu.transformer;

import com.example.branflu.entity.Business;
import com.example.branflu.enums.Role;
import com.example.branflu.payload.request.BusinessRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BusinessRequestToBusinessTransformer {
    private final ModelMapper modelMapper;
    public Business transform(BusinessRequest request, Business existingBusiness) {
        modelMapper.map(request, existingBusiness);
        existingBusiness.setRole(Role.BUSINESS); // fix here
        return existingBusiness;
    }

    public Business transform(BusinessRequest businessRequest){
        return modelMapper.map(businessRequest,Business.class);


    }

}
