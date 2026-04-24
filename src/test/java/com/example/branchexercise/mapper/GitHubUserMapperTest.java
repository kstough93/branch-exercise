package com.example.branchexercise.mapper;

import com.example.branchexercise.model.GitHubUserDto;
import com.example.branchexercise.model.GitHubUserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class GitHubUserMapperTest {
    private final GitHubUserMapper gitHubUserMapper = Mappers.getMapper(GitHubUserMapper.class);

    @Test
    void should_map_valid_user_response() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/github_user_response.json")));
        GitHubUserResponse user = mapper.readValue(json, GitHubUserResponse.class);

        GitHubUserDto underTest = gitHubUserMapper.toDto(user);

        assertEquals(expectedUserDto(), underTest);
    }

    private GitHubUserDto expectedUserDto() {
        return new GitHubUserDto("octocat", "The Octocat",
                "https://avatars.githubusercontent.com/u/583231?v=4",
                "San Francisco", null, "https://api.github.com/users/octocat",
                "2011-01-25T18:44:36Z");
    }

}