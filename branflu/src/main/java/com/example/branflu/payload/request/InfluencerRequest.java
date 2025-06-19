package com.example.branflu.payload.request;

import com.example.branflu.enums.Category;
import com.example.branflu.enums.Platform;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InfluencerRequest extends UserRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String payPalEmail;

    @NotBlank
    private String password;

    @NotEmpty
    private List<Platform> platforms; // ✅ Change from List<String> to List<Platform>

    @NotBlank
    private Category category;

    @NotNull
    private List<LinkRequest> link; // keep this as List<String> unless you're changing request format
}
