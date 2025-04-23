package com.example.branflu.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Business extends User {
    // Inherits category from User
}
