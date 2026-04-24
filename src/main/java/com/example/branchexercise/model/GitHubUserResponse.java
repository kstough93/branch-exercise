package com.example.branchexercise.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubUserResponse(
        String login,
        Integer id,
        @JsonProperty("node_id") String nodeId,
        @JsonProperty("avatar_url") String avatarUrl,
        @JsonProperty("gravatar_id") String gravatarId, String url,
        @JsonProperty("html_url") String htmlUrl,
        @JsonProperty("followers_url") String followersUrl,
        @JsonProperty("following_url") String followingUrl,
        @JsonProperty("gists_url") String gistsUrl,
        @JsonProperty("starred_url") String starredUrl,
        @JsonProperty("subscriptions_url") String subscriptionsUrl,
        @JsonProperty("organizations_url") String organizationsUrl,
        @JsonProperty("repos_url") String reposUrl,
        @JsonProperty("events_url") String eventsUrl,
        @JsonProperty("received_events_url") String receivedEventsUrl, String type,
        @JsonProperty("user_view_type") String userViewType,
        @JsonProperty("site_admin") Boolean siteAdmin, String name, String company,
        String blog,
        String location,
        String email,
        String hireable,
        String bio,
        @JsonProperty("twitter_username") Object twitterUsername,
        @JsonProperty("public_repos") Integer publicRepos,
        @JsonProperty("public_gists") Integer publicGists,
        Integer followers,
        Integer following,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt) {}
