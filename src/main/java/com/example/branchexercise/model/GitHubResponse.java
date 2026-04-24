package com.example.branchexercise.model;

import java.util.List;

public record GitHubResponse (
        String user_name, // login
        String display_name, // name
        String avatar, // avatar_url
        String geo_location, // location
        String email, // email
        String url, //url
        String created_at, // created_at
        List<GitHubUserRepoDto> repos
){}
