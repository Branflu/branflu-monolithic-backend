package com.example.branflu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long linkID;

    @Column(unique = true, nullable = false)
    private String url;
}
