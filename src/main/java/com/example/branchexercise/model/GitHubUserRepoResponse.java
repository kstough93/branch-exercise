package com.example.branchexercise.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubUserRepoResponse(
        String name,
        String url
) {}