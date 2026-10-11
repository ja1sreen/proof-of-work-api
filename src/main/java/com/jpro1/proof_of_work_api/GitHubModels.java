package com.jpro1.proof_of_work_api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GitHubModels {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Repo(
            String name,
            boolean fork,
            int size,
            String description,
            String language,
            List<String> topics,
            @JsonProperty("stargazers_count") int stars,
            @JsonProperty("forks_count") int forks) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Event(
            String type,
            @JsonProperty("created_at") String createdAt,
            EventRepo repo,
            Payload payload) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record EventRepo(String name) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Payload(String action) {}
}