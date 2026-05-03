---
name: addDbMigration
description: Add a forward-only Flyway migration with focused verification.
type: skill
---

# Skill - Add DB Migration

Use this for schema changes in any service-owned Postgres database.

## Steps

1. Confirm the owning service; never modify another service's database directly.
2. Add the next versioned Flyway migration under the service resources.
3. Keep migrations forward-only; add a corrective migration instead of editing one already shared.
4. Add or update integration coverage that starts from an empty schema.

## Done condition

- The migration applies on a clean database.
- The related entity/repository test passes.
