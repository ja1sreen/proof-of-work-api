package com.jpro1.proof_of_work_api;

import com.jpro1.proof_of_work_api.GitHubModels.Event;
import com.jpro1.proof_of_work_api.GitHubModels.Repo;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class ScoreService {

    public ScoreResult calculate(String username, List<Repo> repos, List<Event> events, Instant now) {
        List<Repo> original = repos.stream()
                .filter(r -> !r.fork() && r.size() > 0)
                .toList();

        double activity = ratio(activeWeeks(events, now), 13);
        double originalWork = ratio(original.size(), 8);
        double projectQuality = original.isEmpty() ? 0
                : (double) original.stream().filter(this::looksFinished).count() / original.size();
        double collaboration = ratio(outsideContributions(username, events), 5);
        double diversity = ratio(distinctLanguages(original), 4);
        int popularity = original.stream().mapToInt(r -> r.stars() + r.forks()).sum();
        double community = Math.min(1.0, Math.log10(1 + popularity) / Math.log10(51));

        double total = 0.30 * activity
                + 0.20 * originalWork
                + 0.15 * projectQuality
                + 0.15 * collaboration
                + 0.10 * diversity
                + 0.10 * community;

        return new ScoreResult(username, pct(total), new ScoreResult.Breakdown(
                pct(activity), pct(originalWork), pct(projectQuality),
                pct(collaboration), pct(diversity), pct(community)));
    }

    private int activeWeeks(List<Event> events, Instant now) {
        Set<Long> weeks = new HashSet<>();
        for (Event e : events) {
            long days = ChronoUnit.DAYS.between(Instant.parse(e.createdAt()), now);
            if (days >= 0 && days < 91) {
                weeks.add(days / 7);
            }
        }
        return weeks.size();
    }

    private int outsideContributions(String username, List<Event> events) {
        String own = username.toLowerCase() + "/";
        return (int) events.stream()
                .filter(e -> e.type().equals("PullRequestEvent") || e.type().equals("IssuesEvent"))
                .filter(e -> e.payload() != null && "opened".equals(e.payload().action()))
                .filter(e -> !e.repo().name().toLowerCase().startsWith(own))
                .count();
    }

    private boolean looksFinished(Repo r) {
        boolean hasDescription = r.description() != null && !r.description().isBlank();
        boolean hasTopics = r.topics() != null && !r.topics().isEmpty();
        return hasDescription && hasTopics;
    }

    private int distinctLanguages(List<Repo> repos) {
        return (int) repos.stream()
                .map(Repo::language)
                .filter(l -> l != null)
                .distinct()
                .count();
    }

    private double ratio(double value, double target) {
        return Math.min(1.0, value / target);
    }

    private double pct(double fraction) {
        return Math.round(fraction * 1000) / 10.0;
    }
}