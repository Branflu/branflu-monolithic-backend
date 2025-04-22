package com.example.branflu.entity;

import com.example.branflu.enums.Category;
import com.example.branflu.enums.Platform;

import java.util.List;

public class Influencer extends User{
    private List<Platform> platforms;
    private Category category;
    private Link platformLink;
}
