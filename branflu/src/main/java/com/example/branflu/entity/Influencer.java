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

    @ElementCollection(targetClass = Platform.class)
    @OneToMany(mappedBy = "influencer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InfluencerPlatform> platforms;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "link_id", referencedColumnName = "linkID", unique = true)
    private Link link;
}
