package com.example.branchexercise.mapper;

import com.example.branchexercise.model.GitHubUserRepoDto;
import com.example.branchexercise.model.GitHubUserRepoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class GitHubUserRepoMapperTest {
    private final GitHubUserRepoMapper gitHubUserRepoMapper = Mappers.getMapper(GitHubUserRepoMapper.class);

    @Test
    void should_map_valid_user_repo_response() throws IOException {
        GitHubUserRepoDto[] expected = expectedUserRepoDto();
        ObjectMapper mapper = new ObjectMapper();
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/github_user_repos_response.json")));
        GitHubUserRepoResponse[] repos = mapper.readValue(json, GitHubUserRepoResponse[].class);

        GitHubUserRepoDto[] underTest = gitHubUserRepoMapper.toDto(repos);

        assertEquals(expected[0].name(), underTest[0].name());
        assertEquals(expected[0].url(), underTest[0].url());
        assertEquals(expected.length, underTest.length);
    }

    private GitHubUserRepoDto[] expectedUserRepoDto() {
        return new GitHubUserRepoDto[]{
                new GitHubUserRepoDto("boysenberry-repo-1","https://api.github.com/repos/octocat/boysenberry-repo-1"),
                new GitHubUserRepoDto("git-consortium","https://github.com/octocat/git-consortium"),
                new GitHubUserRepoDto("hello-worId","https://github.com/octocat/hello-worId"),
                new GitHubUserRepoDto("Hello-World","https://github.com/octocat/Hello-World"),
                new GitHubUserRepoDto("linguist","https://github.com/octocat/linguist"),
                new GitHubUserRepoDto("octocat.github.io","https://github.com/octocat/octocat.github.io"),
                new GitHubUserRepoDto("Spoon-Knife","https://github.com/octocat/Spoon-Knife"),
                new GitHubUserRepoDto("test-repo1","https://github.com/octocat/test-repo1")
        };
    }
}