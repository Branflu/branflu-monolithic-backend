package com.example.branflu.payload.request;

import com.example.branflu.enums.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Calendar;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
@ToString
public class BusinessRequest extends UserRequest{
    @NotBlank
    private Category category;

}
