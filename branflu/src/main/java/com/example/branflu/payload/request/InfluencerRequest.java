package com.example.branflu.payload.request;

import com.example.branflu.entity.Link;
import com.example.branflu.enums.Category;
import com.example.branflu.enums.Platform;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
@ToString
public class InfluencerRequest extends UserRequest{
    @NotBlank
    private List<Platform> platforms;
    @NotBlank
    private Category category;
    @NotBlank
    private Link link;
}
