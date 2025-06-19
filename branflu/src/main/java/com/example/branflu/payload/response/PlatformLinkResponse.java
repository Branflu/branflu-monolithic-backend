package com.example.branflu.payload.response;

import com.example.branflu.enums.Platform;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PlatformLinkResponse {
    private Platform platform;
    private String url;
    private Long audienceCount;
    private Long averageViews;
    private Double engagementRate;

}
