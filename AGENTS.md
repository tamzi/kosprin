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
- [`docs/tech/localDevelopment.md`](docs/tech/localDevelopment.md) — local stack, Docker Compose, environment setup.
- [`/jiraboard.md`](jiraboard.md) — single source of truth for all work, organised in priority bands.

## Rules
The rule modules below are the contract. Each is short and concrete.
- [`.ai/rules/architecture.md`](.ai/rules/architecture.md) — design constraints.
- [`.ai/rules/codeStyle.md`](.ai/rules/codeStyle.md) — Kotlin and TypeScript conventions.
- [`.ai/rules/commitConventions.md`](.ai/rules/commitConventions.md) — commit format and atomicity.
- [`.ai/rules/documentationConventions.md`](.ai/rules/documentationConventions.md) — docs naming, length, structure.
- [`.ai/rules/agentBehaviour.md`](.ai/rules/agentBehaviour.md) — what assistants must / must not do.

## Skills, agents, workflows, memory
The full library lives under [`/.ai/`](.ai/README.md). Pick a playbook by browsing the indexes:
- [`.ai/skills/README.md`](.ai/skills/README.md) — single-task playbooks (build, ship, secure, document).
- [`.ai/agents/README.md`](.ai/agents/README.md) — specialised agent role definitions for delegation.
- [`.ai/workflows/README.md`](.ai/workflows/README.md) — multi-skill flows that chain agents and skills.
- [`.ai/memory/README.md`](.ai/memory/README.md) — durable project context (decisions ledger, glossary).

If a task does not yet have a skill, use the closest one as the template, ship the skill alongside the work, and add it to the index.

## Hard constraints (TL;DR)
- **Never push** without explicit user approval. **Never** use `--no-verify` or `--force`.
- **Atomic commits**, past-tense subject (e.g. `Added metadata DTO`), no body, no conventional-commits prefix.
- **One `.md` per commit** — never bundle multiple doc changes.
- **Module READMEs < 100 lines.** Detail goes in `docs/`.
- **No copied source-code snippets in `.md` files** — link to source; command examples are allowed in playbooks.
- **camelCase** for `.md` filenames. Exceptions: `README.md`, `AGENTS.md`, `CLAUDE.md`.
- **Design system / shared types** — use `common/` types; do not redefine error envelopes, event headers, etc.
- **Secrets never in repo** — even in `.env` examples, use placeholders.

If a rule conflicts with what feels natural for a task, surface the conflict in your response — do not silently work around it.
