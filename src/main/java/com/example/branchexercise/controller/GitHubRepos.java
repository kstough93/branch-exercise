package com.example.branchexercise.controller;

import com.example.branchexercise.model.GitHubResponse;
import com.example.branchexercise.service.GitHubUserRepoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/githubrepos/v1")
public class GitHubRepos {
    private static final Logger logger = LogManager.getLogger(GitHubRepos.class);
    private final GitHubUserRepoService gitHubUserRepoService;

    public GitHubRepos(GitHubUserRepoService gitHubUserRepoService) {
        this.gitHubUserRepoService = gitHubUserRepoService;
    }

    @GetMapping("/{userName}")
    public ResponseEntity<GitHubResponse> getUserRepos(@PathVariable String userName) {
        logger.info("Retrieving GitHub information for user {}.", userName);

        GitHubResponse response = gitHubUserRepoService.getGitHubUserRepos(userName);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
}
