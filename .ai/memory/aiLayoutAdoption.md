---
name: aiLayoutAdoption
description: 2026-05-03 — adopted /.ai/ as the canonical directory for AI rules, skills, agents, workflows, and memory.
type: memory
---

# Adopted `/.ai/` as the canonical AI knowledge directory

**2026-05-03.** Migrated from the previous `agents/rules/` + `agents/skills/` layout to `/.ai/` with five subdirectories: `rules/`, `skills/`, `agents/`, `workflows/`, `memory/`. Adopted the YAML frontmatter contract (`name`, `description`, `type`, `uses`) for every file.

**Why:** the old `agents/` directory only carried rules and skills — there was no home for role personas, multi-skill flows, or shared durable memory. `.ai/` is dot-prefixed (signals "machine-readable, ignore by default" to most tooling), gives us five clearly-purposed subdirectories, and matches the convention emerging across multi-assistant repos in 2026. The frontmatter contract makes Claude Code skill matching usable and gives every agent a uniform shape to read.

**How to apply:** make changes under `.ai/`, not in assistant-specific local state. When adding a new entry, also update the relevant `README.md` index in the same commit.

Source: [`../README.md`](../README.md), the migration commit set on `improvements`, and the analogous setup in `~/jec/mimiSpring/myshamba/.ai/` which provided the template.
