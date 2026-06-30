package com.example.branchexercise.service;

import com.example.branchexercise.exception.ServiceUnavailableException;
import com.example.branchexercise.exception.TooManyRequestsException;
import com.example.branchexercise.exception.UserNotFoundException;
import com.example.branchexercise.mapper.GitHubUserMapper;
import com.example.branchexercise.mapper.GitHubUserMapperImpl;
import com.example.branchexercise.mapper.GitHubUserRepoMapper;
import com.example.branchexercise.mapper.GitHubUserRepoMapperImpl;
import com.example.branchexercise.model.GitHubUserDto;
import com.example.branchexercise.model.GitHubUserRepoDto;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class GitHubServiceImplTest {
    private MockRestServiceServer mockServer;
    private GitHubServiceImpl service;
    private final GitHubUserMapper gitHubUserMapper;
    private final GitHubUserRepoMapper gitHubUserRepoMapper;

    GitHubServiceImplTest() {
        this.gitHubUserMapper = new GitHubUserMapperImpl();
        this.gitHubUserRepoMapper = new GitHubUserRepoMapperImpl();
    }

    @BeforeEach
    void setUp() {
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(
                JsonMapper.builder().addModule(new JavaTimeModule()).build());

        RestClient.Builder clientBuilder = RestClient.builder()
                .baseUrl("https://api.github.com")
                .messageConverters(converters -> {
                    converters.removeIf(c -> c instanceof MappingJackson2HttpMessageConverter);
                    converters.add(jacksonConverter);
                });
        mockServer = MockRestServiceServer.bindTo(clientBuilder).build();
        service = new GitHubServiceImpl(clientBuilder.build(), gitHubUserMapper, gitHubUserRepoMapper);
    }

    // --- getGitHubUser ---

    @Test
    void getGitHubUser_should_return_user_dto_on_success() throws IOException {
        String json = new String(getClass().getClassLoader()
                .getResourceAsStream("github_user_response.json").readAllBytes());

        mockServer.expect(requestTo("https://api.github.com/users/octocat"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        GitHubUserDto result = service.getGitHubUser("octocat");

        assertNotNull(result);
        assertEquals("octocat", result.userName());
        assertEquals("The Octocat", result.displayName());
        assertEquals("https://avatars.githubusercontent.com/u/583231?v=4", result.avatar());
        assertEquals("San Francisco", result.geoLocation());
        assertEquals("Tue, 25 Jan 2011 18:44:36 GMT", result.createdAt());
        mockServer.verify();
    }

    @Test
    void getGitHubUser_should_throw_user_not_found_exception_on_404() {
        mockServer.expect(requestTo("https://api.github.com/users/unknown"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(UserNotFoundException.class, () -> service.getGitHubUser("unknown"));
        mockServer.verify();
    }

    @Test
    void getGitHubUser_should_throw_too_many_requests_exception_on_429() {
        mockServer.expect(requestTo("https://api.github.com/users/octocat"))
                .andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS));

        assertThrows(TooManyRequestsException.class, () -> service.getGitHubUser("octocat"));
        mockServer.verify();
    }

    @Test
    void getGitHubUser_should_throw_service_unavailable_exception_on_503() {
        mockServer.expect(requestTo("https://api.github.com/users/octocat"))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));

        assertThrows(ServiceUnavailableException.class, () -> service.getGitHubUser("octocat"));
        mockServer.verify();
    }

    @Test
    void getGitHubUserRepos_should_return_repo_dtos_on_success() throws IOException {
        String json = new String(getClass().getClassLoader()
                .getResourceAsStream("github_user_repos_response.json").readAllBytes());

        mockServer.expect(requestTo("https://api.github.com/users/octocat/repos"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        List<GitHubUserRepoDto> result = service.getGitHubUserRepos("octocat");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("boysenberry-repo-1", result.getFirst().name());
        assertEquals("https://api.github.com/repos/octocat/boysenberry-repo-1", result.getFirst().url());
        mockServer.verify();
    }

    @Test
    void getGitHubUserRepos_should_throw_user_not_found_exception_on_404() {
        mockServer.expect(requestTo("https://api.github.com/users/unknown/repos"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(UserNotFoundException.class, () -> service.getGitHubUserRepos("unknown"));
        mockServer.verify();
    }

    @Test
    void getGitHubUserRepos_should_throw_too_many_requests_exception_on_429() {
        mockServer.expect(requestTo("https://api.github.com/users/octocat/repos"))
                .andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS));

        assertThrows(TooManyRequestsException.class, () -> service.getGitHubUserRepos("octocat"));
        mockServer.verify();
    }

    @Test
    void getGitHubUserRepos_should_throw_service_unavailable_exception_on_503() {
        mockServer.expect(requestTo("https://api.github.com/users/octocat/repos"))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));

        assertThrows(ServiceUnavailableException.class, () -> service.getGitHubUserRepos("octocat"));
        mockServer.verify();
    }
}
