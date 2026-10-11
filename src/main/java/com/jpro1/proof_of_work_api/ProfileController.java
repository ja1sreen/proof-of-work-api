package com.jpro1.proof_of_work_api;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    private final GitHubClient gitHubClient;

    public ProfileController(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    @GetMapping("/profile/{username}")
    public Map<String, Object> profile(@PathVariable String username) {
        Map<String, Object> user = gitHubClient.getUser(username);
        return Map.of(
                "username", user.get("login"),
                "publicRepos", user.get("public_repos"),
                "followers", user.get("followers"));
    }
}