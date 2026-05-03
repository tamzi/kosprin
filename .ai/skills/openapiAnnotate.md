---
name: openapiAnnotate
description: Keep REST endpoints discoverable through accurate OpenAPI metadata.
type: skill
---

# Skill - OpenAPI Annotate

Use this when adding or changing a REST endpoint.

## Steps

1. Describe operation intent, auth expectation, success response, and expected failures.
2. Keep schema names aligned with DTO names.
3. Verify `/v3/api-docs` includes the endpoint when the service can run.
4. Update aggregation docs only when gateway-level OpenAPI routing changes.

## Done condition

- Generated API docs match the implemented endpoint and validation behaviour.
