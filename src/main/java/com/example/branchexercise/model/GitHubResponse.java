package com.example.branchexercise.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GitHubResponse(
        @JsonProperty("user_name") String userName,
        @JsonProperty("display_name") String displayName,
        String avatar,
        @JsonProperty("geo_location") String geoLocation,
        String email,
        String url,
        @JsonProperty("created_at") String createdAt,
        List<GitHubUserRepoDto> repos
) {}