package com.example.branflu.entity;

import com.example.branflu.enums.Deliverables;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Campaign {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID campaignId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;
    @ElementCollection(targetClass = Deliverables.class)
    @Enumerated(EnumType.STRING)
    private List<Deliverables> deliverables;




}
