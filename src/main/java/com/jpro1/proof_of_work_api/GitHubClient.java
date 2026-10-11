package com.jpro1.proof_of_work_api;

import com.jpro1.proof_of_work_api.GitHubModels.Event;
import com.jpro1.proof_of_work_api.GitHubModels.Repo;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GitHubClient {

    private final RestClient restClient;

    public GitHubClient() {
        RestClient.Builder builder = RestClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader("Accept", "application/vnd.github+json");
        String token = System.getenv("GITHUB_TOKEN");
        if (token != null && !token.isBlank()) {
            builder.defaultHeader("Authorization", "Bearer " + token);
        }
        this.restClient = builder.build();
    }

    public List<Repo> getRepos(String username) {
        return restClient.get()
                .uri("/users/{u}/repos?per_page=100&type=owner", username)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Repo>>() {});
    }

    public List<Event> getEvents(String username) {
        return restClient.get()
                .uri("/users/{u}/events/public?per_page=100", username)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Event>>() {});
    }
}