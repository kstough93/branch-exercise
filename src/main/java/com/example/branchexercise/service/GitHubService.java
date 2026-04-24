package com.example.branchexercise.service;

import com.example.branchexercise.model.GitHubUserRepoDto;
import com.example.branchexercise.model.GitHubUserDto;

import java.util.List;

public interface GitHubService {
    public GitHubUserDto getGitHubUser(String userName);

    public List<GitHubUserRepoDto> getGitHubUserRepos(String userName);
}
