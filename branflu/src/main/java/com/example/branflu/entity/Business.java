package com.example.branflu.entity;

import jakarta.persistence.Entity;
import com.example.branflu.enums.Category;
import lombok.Getter;

@Entity
@Getter
public class Business extends User{
private Category category;
}
