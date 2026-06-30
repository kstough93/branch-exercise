package com.example.branchexercise.model;

public record GitHubUserDto(
        String userName,
        String displayName,
        String avatar,
        String geoLocation,
        String email,
        String url,
        String createdAt
) {}