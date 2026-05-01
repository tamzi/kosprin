# Agent Rules — kosprin

Canonical entry point for any AI coding assistant working in this repo (Claude Code, Cursor, Codex, Copilot, etc.). All assistant-specific config files (`CLAUDE.md`, `.cursor/rules/*`, `.github/copilot-instructions.md`, …) point here so there is **one source of truth**.

## Project at a glance
- Kotlin/Spring-Boot multi-module template emulating a mini YouTube/Instagram backend.
- Modules under `*-service/`, gateway under `gateway/`, shared lib under `common/`, frontend under `dashboard/`.
- Status: **scaffold** — most services are not yet implemented. Track work in [`/jiraboard.md`](jiraboard.md).

## Authoritative documents
Read these before making non-trivial changes:
- [`docs/tech/systemdesign.md`](docs/tech/systemdesign.md) — architecture diagram and rationale.
- [`docs/tech/services.md`](docs/tech/services.md) — per-module summary.
- [`docs/tech/messaging.md`](docs/tech/messaging.md) — Kafka topics + idempotency conventions.
- [`docs/tech/buildSystem.md`](docs/tech/buildSystem.md) — Gradle setup and conventions.
- [`/jiraboard.md`](jiraboard.md) — single source of truth for all work, organised in priority bands.

## Rules
The rule modules below are the contract. Each is short and concrete.
- [`agents/rules/architecture.md`](agents/rules/architecture.md) — design constraints.
- [`agents/rules/codeStyle.md`](agents/rules/codeStyle.md) — Kotlin and TypeScript conventions.
- [`agents/rules/commitConventions.md`](agents/rules/commitConventions.md) — commit format and atomicity.
- [`agents/rules/documentationConventions.md`](agents/rules/documentationConventions.md) — docs naming, length, structure.
- [`agents/rules/agentBehaviour.md`](agents/rules/agentBehaviour.md) — what assistants must / must not do.

## Skills (task playbooks)
Step-by-step playbooks for routine tasks. Use them instead of re-deriving the steps each time.
- [`agents/skills/runLocalStack.md`](agents/skills/runLocalStack.md) — bring up Postgres + Redis + Kafka + ES + Keycloak.
- [`agents/skills/runTests.md`](agents/skills/runTests.md) — run unit + integration tests for a single module or the whole tree.
- [`agents/skills/addNewService.md`](agents/skills/addNewService.md) — cookie-cutter steps for a new service module.
- [`agents/skills/addKafkaTopic.md`](agents/skills/addKafkaTopic.md) — register a topic and wire producer/consumer correctly.
- [`agents/skills/checkDependencyUpdates.md`](agents/skills/checkDependencyUpdates.md) — run before every push to bump outdated deps.

## Hard constraints (TL;DR)
- **Never push** without explicit user approval. **Never** use `--no-verify` or `--force`.
- **Atomic commits**, past-tense subject (e.g. `Added metadata DTO`), no body, no conventional-commits prefix.
- **One `.md` per commit** — never bundle multiple doc changes.
- **Module READMEs < 100 lines.** Detail goes in `docs/`.
- **No code snippets in `.md` files** — link to source.
- **camelCase** for `.md` filenames. Exceptions: `README.md`, `AGENTS.md`, `CLAUDE.md`.
- **Design system / shared types** — use `common/` types; do not redefine error envelopes, event headers, etc.
- **Secrets never in repo** — even in `.env` examples, use placeholders.

If a rule conflicts with what feels natural for a task, surface the conflict in your response — do not silently work around it.
