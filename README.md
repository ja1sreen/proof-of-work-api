# GitHub Proof-of-Work API

A REST API that takes a GitHub username and returns a proof-of-work profile:
a 0-100 score with a breakdown of how it was calculated.

Built with Java 21 and Spring Boot. Status: in development.

## Why this exists
Finding hackathon teammates usually comes down to guesswork. This API
turns a GitHub profile into a structured, explainable signal, and it's meant
as the foundation for a teammate-matching tool later.

## Example

`GET /profile/{username}`

```json
{
  "username": "ja1sreen",
  "score": 41.3,
  "breakdown": {
    "activity": 38.5,
    "originalWork": 62.5,
    "projectQuality": 40.0,
    "collaboration": 20.0,
    "languageDiversity": 50.0,
    "communitySignal": 12.0
  }
}
```

## How the score works
Weighted sum of six components, each scaled 0-100:

| Component | Weight | What it measures |
|---|---|---|
| Activity | 30% | Active weeks in the last 90 days |
| Original work | 20% | Non-fork, non-empty repos |
| Project quality | 15% | Repos with a description and topics |
| Collaboration | 15% | PRs/issues opened on others' repos |
| Language diversity | 10% | Distinct languages used |
| Community signal | 10% | Stars and forks (log-scaled) |

The weights are v1 guesses and will be tuned using feedback from real users.

## Run locally
Requirements: JDK 21

```
git clone https://github.com/ja1sreen/proof-of-work-api.git
cd proof-of-work-api
.\mvnw spring-boot:run      # Windows
./mvnw spring-boot:run      # macOS/Linux
```

Then open `http://localhost:8080/profile/<github-username>`.

Optional: set a `GITHUB_TOKEN` environment variable to raise the GitHub API
rate limit from 60 to 5,000 requests per hour.

## Known limitations
- Only public GitHub activity is visible; private work isn't counted.
- The events API returns at most 100 recent events, so very active users
  may be under-scored.
- Commit activity can be gamed, so the score is a signal, not proof.

## Roadmap
- [x] Health endpoint
- [x] GitHub API integration
- [x] Scoring logic
- [ ] Error handling (unknown users, rate limits)
- [ ] PostgreSQL storage and caching
- [ ] Unit tests for the scoring logic
- [ ] Docker and deployment
- [ ] Live demo link
