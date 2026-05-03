---
name: tailServiceLogs
description: Tail local stack logs for a service with enough filtering to debug quickly.
type: skill
---

# Skill - Tail Service Logs

Use this when local services or containers are running and a failure needs runtime evidence.

## Steps

1. Identify the service or container name.
2. Tail only the relevant logs first; expand to the full stack if correlation IDs cross services.
3. Filter by correlation ID when available.
4. Capture the shortest useful excerpt in the response.

## Done condition

- The next debugging step is supported by log evidence.
