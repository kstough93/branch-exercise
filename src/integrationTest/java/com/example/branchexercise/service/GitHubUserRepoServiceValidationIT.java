package com.example.branchexercise.service;

import com.example.branchexercise.configuration.CacheManagerConfig;
import com.example.branchexercise.model.GitHubUserDto;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
class GitHubUserRepoServiceValidationIT {

    @Configuration
    @EnableCaching
    @Import({GitHubUserRepoService.class, CacheManagerConfig.class})
    static class TestConfig {
        @Bean
        public MethodValidationPostProcessor methodValidationPostProcessor() {
            return new MethodValidationPostProcessor();
        }
    }

    @MockitoBean
    GitHubService gitHubService;

    @Autowired
    GitHubUserRepoService service;

    @Autowired
    CacheManager cacheManager;

    @BeforeEach
    void clearCache() {
        cacheManager.getCache("userRepoCache").clear();
    }

    @Test
    void getGitHubUserRepos_should_throw_when_username_is_blank() {
        assertThrows(ConstraintViolationException.class,
                () -> service.getGitHubUserRepos("   "));
    }

    @Test
    void getGitHubUserRepos_should_throw_when_username_exceeds_39_characters() {
        assertThrows(ConstraintViolationException.class,
                () -> service.getGitHubUserRepos("a".repeat(40)));
    }

    @Test
    void getGitHubUserRepos_should_accept_username_at_max_length() {
        String maxUsername = "a".repeat(39);
        when(gitHubService.getGitHubUser(maxUsername)).thenReturn(
                new GitHubUserDto(maxUsername, null, null, null, null, null, null));
        when(gitHubService.getGitHubUserRepos(maxUsername)).thenReturn(List.of());

        assertDoesNotThrow(() -> service.getGitHubUserRepos(maxUsername));
    }
}
