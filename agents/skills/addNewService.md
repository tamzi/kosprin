# Skill — Add a new service

Use this playbook when the user asks to add a new microservice (e.g. `comment-service`, `recommendation-service`).

## 1. Decide the role
Before writing any code, determine:
- **Sync or async dominant?** Pure consumer/worker → no `spring-boot-starter-web`.
- **Persistent state?** Postgres → JPA + Flyway. Search → Elasticsearch. Cache only → Redis.
- **Topics produced and consumed.** Document them in [`docs/tech/messaging.md`](../../docs/tech/messaging.md) before implementing.

## 2. Register the module
Add the module name to `settings.gradle.kts`:
```kotlin
listOf(
    "analytics-service",
    "comment-service",   // new
    ...
)
```

## 3. Create the build script
Copy the closest-matching existing service's `build.gradle.kts` as a starting point:

| Role                       | Copy from              |
|----------------------------|------------------------|
| REST + Postgres + Kafka    | `metadata-service`     |
| Worker + Kafka only        | `video-service`        |
| REST + Elasticsearch       | `search-service`       |
| REST + Postgres + Kafka    | `feed-service`         |
| Worker + dispatch          | `notification-service` |

Trim dependencies the new service does not need.

## 4. Wire the gateway
Add a route to `gateway/` so the new service is reachable:
- Path predicate (`/api/v1/<resource>/**`).
- Forward to `lb://<service-name>` if discovery is in use, else direct URI.
- Include the standard auth/rate-limit/correlation-ID filters.

## 5. Documentation
- Add a row to [`docs/tech/services.md`](../../docs/tech/services.md).
- Add a `<service>/README.md` (under 100 lines) describing the service's role and linking to deeper docs.
- Update [`docs/README.md`](../../docs/README.md) if a new doc was added.
- Add a Jira-board epic (`KOS-N`) for the implementation work.

## 6. Tests
- Unit tests for any non-trivial logic.
- One end-to-end test per public endpoint or each consumed topic, using Testcontainers.

## 7. Commit
- Atomic commits per the rules. Suggested split:
  1. `Added comment-service module to gradle settings`
  2. `Added comment-service build script`
  3. `Added comment-service skeleton`
  4. `Added comment-service docs`
