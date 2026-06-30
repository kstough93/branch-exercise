package com.example.branchexercise.mapper;

import com.example.branchexercise.model.GitHubUserRepoDto;
import com.example.branchexercise.model.GitHubUserRepoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GitHubUserRepoMapper {
    GitHubUserRepoDto[] toDto(GitHubUserRepoResponse[] userRepos);
}
