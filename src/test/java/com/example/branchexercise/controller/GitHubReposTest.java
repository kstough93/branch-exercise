package com.example.branchexercise.controller;

import com.example.branchexercise.exception.ServiceUnavailableException;
import com.example.branchexercise.exception.TooManyRequestsException;
import com.example.branchexercise.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import com.example.branchexercise.model.GitHubResponse;
import com.example.branchexercise.model.GitHubUserRepoDto;
import com.example.branchexercise.service.GitHubUserRepoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GitHubReposController.class)
class GitHubReposTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    GitHubUserRepoService gitHubUserRepoService;

    private static final String BASE_URL = "/githubrepos/v1";

    private static final GitHubResponse GITHUB_RESPONSE = new GitHubResponse(
            "octocat", "The Octocat",
            "https://avatars.githubusercontent.com/u/583231?v=4",
            "San Francisco", null, "https://github.com/octocat",
            "Tue, 25 Jan 2011 18:44:36 GMT",
            List.of(new GitHubUserRepoDto("boysenberry-repo-1",
                    "https://api.github.com/repos/octocat/boysenberry-repo-1"))
    );

    @Test
    void getUserRepos_should_return_200_with_body_on_success() throws Exception {
        when(gitHubUserRepoService.getGitHubUserRepos("octocat")).thenReturn(GITHUB_RESPONSE);

        mockMvc.perform(get(BASE_URL + "/octocat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name").value("octocat"))
                .andExpect(jsonPath("$.display_name").value("The Octocat"))
                .andExpect(jsonPath("$.created_at").value("Tue, 25 Jan 2011 18:44:36 GMT"))
                .andExpect(jsonPath("$.repos[0].name").value("boysenberry-repo-1"));
    }

    @Test
    void getUserRepos_should_return_404_when_user_not_found() throws Exception {
        when(gitHubUserRepoService.getGitHubUserRepos("unknown"))
                .thenThrow(new UserNotFoundException("GitHub error: 404 NOT_FOUND"));

        mockMvc.perform(get(BASE_URL + "/unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("User Not Found"))
                .andExpect(jsonPath("$.url").value(BASE_URL + "/unknown"));
    }

    @Test
    void getUserRepos_should_return_429_when_rate_limited() throws Exception {
        when(gitHubUserRepoService.getGitHubUserRepos("octocat"))
                .thenThrow(new TooManyRequestsException("Too many requests, please try again later, Http Status: 429 TOO_MANY_REQUESTS"));

        mockMvc.perform(get(BASE_URL + "/octocat"))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.status").value(429))
                .andExpect(jsonPath("$.title").value("Too Many Requests"))
                .andExpect(jsonPath("$.url").value(BASE_URL + "/octocat"));
    }

    @Test
    void getUserRepos_should_return_503_when_github_unavailable() throws Exception {
        when(gitHubUserRepoService.getGitHubUserRepos("octocat"))
                .thenThrow(new ServiceUnavailableException("GitHub Service is temporarily unavailable, please try again later, Http Status: 503 SERVICE_UNAVAILABLE"));

        mockMvc.perform(get(BASE_URL + "/octocat"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503))
                .andExpect(jsonPath("$.title").value("Service Unavailable"))
                .andExpect(jsonPath("$.url").value(BASE_URL + "/octocat"));
    }

    @Test
    void getUserRepos_should_return_422_when_username_is_missing() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.title").value("Username path variable is required."));
    }

    @Test
    void getUserRepos_should_return_400_when_username_is_invalid() throws Exception {
        when(gitHubUserRepoService.getGitHubUserRepos(any()))
                .thenThrow(new ConstraintViolationException("must not be blank", new HashSet<>()));

        mockMvc.perform(get(BASE_URL + "/invalid-user"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Invalid Request"));
    }

    @Test
    void getUserRepos_should_return_500_on_unexpected_exception() throws Exception {
        when(gitHubUserRepoService.getGitHubUserRepos("octocat"))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get(BASE_URL + "/octocat"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred."));
    }
}