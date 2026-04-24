package com.example.branchexercise.service;

import com.example.branchexercise.exception.ServiceUnavailableException;
import com.example.branchexercise.exception.TooManyRequestsException;
import com.example.branchexercise.exception.UserNotFoundException;
import com.example.branchexercise.mapper.GitHubUserMapper;
import com.example.branchexercise.mapper.GitHubUserRepoMapper;
import com.example.branchexercise.model.GitHubUserRepoDto;
import com.example.branchexercise.model.GitHubUserDto;
import com.example.branchexercise.model.GitHubUserRepoResponse;
import com.example.branchexercise.model.GitHubUserResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;

@Service
public class GitHubServiceImpl implements GitHubService {
    private static final Logger logger = LogManager.getLogger(GitHubServiceImpl.class);

    private final RestClient restClient;

    public GitHubServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public GitHubUserDto getGitHubUser(String userName) {
        GitHubUserMapper gitHubUserMapper = Mappers.getMapper(GitHubUserMapper.class);

        logger.info("Retrieving GitHub user info for {}", userName);

        GitHubUserResponse gitHubUserResponse = restClient
                .get()
                .uri("/users/{userName}", userName)
                .retrieve()
                .onStatus(status -> status.value() == 429, (request, response) -> {
                    throw new TooManyRequestsException("Too many requests, please try again later, Http Status: " + response.getStatusCode());
                })
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new UserNotFoundException("GitHub error: " + response.getStatusCode());
                })
                .onStatus(status -> status.value() == 503, (request, response) -> {
                    throw new ServiceUnavailableException("GitHub Service is temporarily unavailable, please try again later, Http Status: " + response.getStatusCode());
                })
                .body(GitHubUserResponse.class);

        return gitHubUserMapper.toDto(gitHubUserResponse);
    }

    @Override
    public List<GitHubUserRepoDto> getGitHubUserRepos(String userName) {
        GitHubUserRepoMapper gitHubUserRepoMapper = Mappers.getMapper(GitHubUserRepoMapper.class);

        logger.info("Retrieving GitHub user repo infor for {}", userName);

        GitHubUserRepoResponse[] gitHubUserRepoResponse = restClient
                .get()
                .uri("/users/{userName}/repos", userName)
                .retrieve()
                .onStatus(status -> status.value() == 429, (request, response) -> {
                    throw new TooManyRequestsException("Too many requests, please try again later, Http Status: " + response.getStatusCode());
                })
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new UserNotFoundException("GitHub error: " + response.getStatusCode());
                })
                .onStatus(status -> status.value() == 503, (request, response) -> {
                    throw new ServiceUnavailableException("GitHub Service is temporarily unavailable, please try again later, Http Status: " + response.getStatusCode());
                })
                .body(GitHubUserRepoResponse[].class);

        return List.of(gitHubUserRepoMapper.toDto(gitHubUserRepoResponse));
    }
}
