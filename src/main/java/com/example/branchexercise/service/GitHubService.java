package com.example.branchexercise.service;

import com.example.branchexercise.model.GitHubUserRepoDto;
import com.example.branchexercise.model.GitHubUserDto;

import java.util.List;

public interface GitHubService {
    GitHubUserDto getGitHubUser(String userName);

    List<GitHubUserRepoDto> getGitHubUserRepos(String userName);
}
