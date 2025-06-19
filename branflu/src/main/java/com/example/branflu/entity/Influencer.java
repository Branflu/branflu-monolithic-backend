package com.example.branflu.entity;

import com.example.branflu.enums.Platform;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Influencer extends User {


    @OneToMany(mappedBy = "influencer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InfluencerPlatform> platforms;


}
