---
name: springTestPatterns
description: Choose the right Spring test scope for controllers, services, repositories, and integrations.
type: skill
uses: [runTests]
---

# Skill - Spring Test Patterns

Use this when adding tests around Spring code.

## Steps

1. Use plain unit tests for pure application logic.
2. Use MVC slice tests for controller validation and error mapping.
3. Use repository or integration tests for database, Kafka, Redis, Keycloak, or Elasticsearch wiring.
4. Run the narrow module task first, then broader [runTests](runTests.md) coverage when shared behaviour changed.

## Done condition

- The test scope matches the boundary being verified.
