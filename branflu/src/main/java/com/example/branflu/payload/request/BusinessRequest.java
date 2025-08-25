package com.example.branflu.payload.request;

import com.example.branflu.enums.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessRequest extends UserRequest {
//    private String imageUrl;
    private String websiteUrl;
    private String Bio;
    private Category category;
}
