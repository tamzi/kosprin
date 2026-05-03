---
name: addRestEndpoint
description: Add a REST endpoint with DTOs, validation, OpenAPI, tests, and shared error handling.
type: skill
uses: [kotlinSpringConventions, springTestPatterns, openapiAnnotate]
---

# Skill - Add REST Endpoint

Use this for a new externally reachable HTTP path in a service or gateway route.

## Steps

1. Confirm the owning service and route shape from [`../../docs/tech/services.md`](../../docs/tech/services.md).
2. Add immutable request/response DTOs and validate external input at the controller boundary.
3. Keep service logic behind an application service returning `Outcome<T>` from `common/`.
4. Annotate the endpoint through [openapiAnnotate](openapiAnnotate.md).
5. Add tests through [springTestPatterns](springTestPatterns.md).

## Done condition

- Validation, error mapping, and OpenAPI output are present.
- Tests cover success and at least one failure path.
