package com.example.branchexercise.mapper;

import com.example.branchexercise.model.GitHubUserDto;
import com.example.branchexercise.model.GitHubUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GitHubUserMapper {
    @Mapping(source = "login", target = "user_name")
    @Mapping(source = "name", target = "display_name")
    @Mapping(source = "avatarUrl", target = "avatar")
    @Mapping(source = "location", target = "geo_location")
    @Mapping(source = "createdAt", target = "created_at")
    GitHubUserDto toDto(GitHubUserResponse user);
}