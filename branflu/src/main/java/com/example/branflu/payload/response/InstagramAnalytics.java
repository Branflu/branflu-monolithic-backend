package com.example.branflu.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InstagramAnalytics {

    @JsonProperty("followers_count")
    private Integer followersCount;

    @JsonProperty("media_count")
    private Integer mediaCount;

    private Double engagementRate;
}
