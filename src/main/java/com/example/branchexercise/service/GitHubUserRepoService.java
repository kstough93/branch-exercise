package com.example.branchexercise.service;

import com.example.branchexercise.model.GitHubUserRepoDto;
import com.example.branchexercise.model.GitHubResponse;
import com.example.branchexercise.model.GitHubUserDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class GitHubUserRepoService {
    private final GitHubService gitHubService;

    public GitHubUserRepoService(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @Cacheable(value = "userRepoCache", key = "#userName")
    public GitHubResponse getGitHubUserRepos(@NotBlank @Size(max = 39) String userName) {

        GitHubUserDto gitHubUserDto = gitHubService.getGitHubUser(userName);
        List<GitHubUserRepoDto> gitHubUserRepos = gitHubService.getGitHubUserRepos(userName);

        return new GitHubResponse(
                gitHubUserDto.userName(), gitHubUserDto.displayName(), gitHubUserDto.avatar(),
                gitHubUserDto.geoLocation(), gitHubUserDto.email(), gitHubUserDto.url(),
                gitHubUserDto.createdAt(), gitHubUserRepos);
    }

}
