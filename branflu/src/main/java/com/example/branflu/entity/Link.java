package com.example.branflu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID linkID;
    @Column(unique = true,nullable = false)
    private String url;
}
