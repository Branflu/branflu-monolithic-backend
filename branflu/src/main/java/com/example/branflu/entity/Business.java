package com.example.branflu.entity;

import com.example.branflu.enums.Category;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Business extends User {
//    private String imageUrl;
    private String websiteUrl;
    private String Bio;
    private Category category;
}
