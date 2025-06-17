package com.example.branflu.payload.request;

import com.example.branflu.entity.Link;
import com.example.branflu.enums.Category;
import com.example.branflu.enums.Platform;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessRequest extends UserRequest {
    @NotBlank       private String name;
    @NotBlank       private String payPalEmail;
    @NotBlank       private String password;
    @NotBlank       private Category category;
}
