package com.example.branchexercise.service;

import com.example.branchexercise.configuration.CacheManagerConfig;
import com.example.branchexercise.model.GitHubResponse;
import com.example.branchexercise.model.GitHubUserDto;
import com.example.branchexercise.model.GitHubUserRepoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
class GitHubUserRepoServiceCacheIT {

    @Configuration
    @EnableCaching
    @Import({GitHubUserRepoService.class, CacheManagerConfig.class})
    static class TestConfig {}

    @MockitoBean
    GitHubService gitHubService;

    @Autowired
    GitHubUserRepoService service;

    @Autowired
    CacheManager cacheManager;

    private static final GitHubUserDto USER_DTO = new GitHubUserDto(
            "octocat", "The Octocat", "https://avatars.githubusercontent.com/u/583231?v=4",
            "San Francisco", null, "https://github.com/octocat", "2011-01-25T18:44:36Z"
    );

    private static final List<GitHubUserRepoDto> REPOS = List.of(
            new GitHubUserRepoDto("boysenberry-repo-1", "https://api.github.com/repos/octocat/boysenberry-repo-1")
    );

    @BeforeEach
    void clearCache() {
        cacheManager.getCache("userRepoCache").clear();
    }

    @Test
    void getGitHubUserRepos_should_return_cached_result_on_second_call() {
        when(gitHubService.getGitHubUser("octocat")).thenReturn(USER_DTO);
        when(gitHubService.getGitHubUserRepos("octocat")).thenReturn(REPOS);

        GitHubResponse first = service.getGitHubUserRepos("octocat");
        GitHubResponse second = service.getGitHubUserRepos("octocat");

        assertEquals(first, second);
        verify(gitHubService, times(1)).getGitHubUser("octocat");
        verify(gitHubService, times(1)).getGitHubUserRepos("octocat");
    }

    @Test
    void getGitHubUserRepos_should_call_service_for_different_usernames() {
        GitHubUserDto anotherUser = new GitHubUserDto(
                "torvalds", "Linus Torvalds", "https://avatars.githubusercontent.com/u/1024025?v=4",
                "Portland", null, "https://github.com/torvalds", "2011-09-03T15:26:22Z"
        );
        List<GitHubUserRepoDto> anotherRepos = List.of(
                new GitHubUserRepoDto("linux", "https://api.github.com/repos/torvalds/linux")
        );

        when(gitHubService.getGitHubUser("octocat")).thenReturn(USER_DTO);
        when(gitHubService.getGitHubUserRepos("octocat")).thenReturn(REPOS);
        when(gitHubService.getGitHubUser("torvalds")).thenReturn(anotherUser);
        when(gitHubService.getGitHubUserRepos("torvalds")).thenReturn(anotherRepos);

        service.getGitHubUserRepos("octocat");
        service.getGitHubUserRepos("torvalds");

        verify(gitHubService, times(1)).getGitHubUser("octocat");
        verify(gitHubService, times(1)).getGitHubUserRepos("octocat");
        verify(gitHubService, times(1)).getGitHubUser("torvalds");
        verify(gitHubService, times(1)).getGitHubUserRepos("torvalds");
    }
}