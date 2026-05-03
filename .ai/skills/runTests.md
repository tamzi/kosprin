---
name: runTests
description: Run unit + integration tests for a single module or the whole tree.
type: skill
---

# Skill — Run tests

## Single module
```
./gradlew :metadata-service:test
```

## All modules
```
./gradlew test
```

## Integration tests (Testcontainers)
Once KOS-14.2 has landed, integration tests live under `src/integrationTest/kotlin` per module.

```
./gradlew :metadata-service:integrationTest
```

Testcontainers will spin up the Docker dependencies the test needs — Docker must be running.

## Before reporting tests as "passing"
- Confirm the run actually included the change you made. If you renamed a class, run a clean build first: `./gradlew clean test`.
- Check `build/reports/tests/test/index.html` for skipped tests. Skipped tests can hide regressions.
- For UI/dashboard work, type-checking and unit tests are not enough — load the page in a browser and exercise the change. If you cannot test the UI, say so explicitly rather than claiming success.

## Common failures
- **No such test class** — Gradle test cache stale. `./gradlew clean test`.
- **Testcontainers cannot pull image** — check `~/.docker/config.json` auth and disk space.
- **Port-in-use** during integration tests — another container or local service holds the port. `docker compose down` first.

## Coverage
JaCoCo is configured at the root (KOS-1.5). Aggregate report:
```
./gradlew jacocoTestReport
open build/reports/jacoco/jacocoTestReport/html/index.html
```
