package com.example.branchexercise.model;

public record GitHubUserDto(
        String user_name,
        String display_name,
        String avatar,
        String geo_location,
        String email,
        String url,
        String created_at
) {}
