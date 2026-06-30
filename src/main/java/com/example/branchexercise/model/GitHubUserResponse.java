package com.example.branchexercise.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubUserResponse(
        String login,
        String name,
        @JsonProperty("avatar_url") String avatarUrl,
        String location,
        String email,
        String url,
        @JsonProperty("created_at") ZonedDateTime createdAt
) {}