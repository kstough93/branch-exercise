package com.example.branchexercise.service;

import com.example.branchexercise.model.GitHubUserRepoDto;
import com.example.branchexercise.model.GitHubResponse;
import com.example.branchexercise.model.GitHubUserDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GitHubUserRepoService {
    private final GitHubService gitHubService;

    public GitHubUserRepoService(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @Cacheable(value = "userRepoCache", key = "#userName")
    public GitHubResponse getGitHubUserRepos(String userName) {

        GitHubUserDto gitHubUserDto = gitHubService.getGitHubUser(userName);
        List<GitHubUserRepoDto> gitHubUserRepos = gitHubService.getGitHubUserRepos(userName);

        return new GitHubResponse(
                gitHubUserDto.user_name(), gitHubUserDto.display_name(),  gitHubUserDto.avatar(),
                gitHubUserDto.geo_location(), gitHubUserDto.email(), gitHubUserDto.url(),
                gitHubUserDto.created_at(), gitHubUserRepos);
    }

}
