package com.jpro1.proof_of_work_api;

import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GitHubClient {

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://api.github.com")
            .defaultHeader("Accept", "application/vnd.github+json")
            .build();

    public Map<String, Object> getUser(String username) {
        return restClient.get()
                .uri("/users/{username}", username)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
}