package com.example.branflu.payload.request;

import com.example.branflu.enums.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Calendar;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties
@ToString
public class BusinessRequest extends UserRequest{
    @NotBlank
    private Category category;

}
