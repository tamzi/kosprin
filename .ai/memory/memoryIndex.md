---
name: memoryIndex
description: Index of durable project memory entries.
type: memory
---

# Memory Index

Index of durable project memory. One line per entry. Never store memory content in this file — each entry has its own file alongside.

## Project rules

- [Commit format](commitFormat.md) — past tense, no prefix, no body, atomic, < 72 chars.
- [Push policy](pushPolicy.md) — agents push only when explicitly asked; never `--no-verify` / `-f`.
- [Hook bypass policy](hookBypass.md) — never bypass git hooks; fix the cause.
- [Documentation rules](docRules.md) — camelCase `.md`, < 100 lines, no snippets, no dates.

## Project shape

- [Modules layout](modulesLayout.md) — common + 6 services + gateway + dashboard.
- [Runtime config](runtimeConfig.md) — service ports, infra ports, profiles.
- [Persistence stack](persistenceStack.md) — Postgres per service + Elasticsearch + Redis.
- [Messaging stack](messagingStack.md) — Kafka topics + idempotency conventions.

## Decisions ledger

- [Decisions ledger](decisions.md) — append-only index of ADRs, accepted residual risks, pinned trade-offs, and release notes.
- [Adopted `.ai/` layout](aiLayoutAdoption.md) — 2026-05-03; replaces the old `agents/` tree.

## Glossary

- [Glossary](glossary.md) — domain terms and acronyms (KOS, sacrament, DLT, STRIDE, …).

Note: the active priority band is **not** stored here. It is read directly from [`/jiraboard.md`](../../jiraboard.md) — duplicating it would just create a second source of truth that goes stale.
