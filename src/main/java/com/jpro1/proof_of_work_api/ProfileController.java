package com.jpro1.proof_of_work_api;

import java.time.Instant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    private final GitHubClient gitHubClient;
    private final ScoreService scoreService;

    public ProfileController(GitHubClient gitHubClient, ScoreService scoreService) {
        this.gitHubClient = gitHubClient;
        this.scoreService = scoreService;
    }

    @GetMapping("/profile/{username}")
    public ScoreResult profile(@PathVariable String username) {
        var repos = gitHubClient.getRepos(username);
        var events = gitHubClient.getEvents(username);
        return scoreService.calculate(username, repos, events, Instant.now());
    }
}