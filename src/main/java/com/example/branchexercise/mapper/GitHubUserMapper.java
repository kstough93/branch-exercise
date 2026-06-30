package com.example.branchexercise.mapper;

import com.example.branchexercise.model.GitHubUserDto;
import com.example.branchexercise.model.GitHubUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface GitHubUserMapper {
    @Mapping(source = "login", target = "userName")
    @Mapping(source = "name", target = "displayName")
    @Mapping(source = "avatarUrl", target = "avatar")
    @Mapping(source = "location", target = "geoLocation")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "formatCreatedAt")
    GitHubUserDto toDto(GitHubUserResponse user);

    @Named("formatCreatedAt")
    default String formatCreatedAt(ZonedDateTime createdAt) {
        if (createdAt == null) return null;
        return DateTimeFormatter.RFC_1123_DATE_TIME
                .format(createdAt.withZoneSameInstant(ZoneId.of("GMT")));
    }
}