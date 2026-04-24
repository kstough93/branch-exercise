package com.example.branchexercise.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientBean {

    @Bean
    public RestClient GitHubRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeaders(headers -> {
                    headers.add("Accept", "application/vnd.github+json");
                    headers.add("X-GitHub-Api-Version", "2026-03-10");
                })
                .build();
    }
}
