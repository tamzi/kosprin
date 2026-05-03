---
name: memoryIndex
description: How memory entries are structured and what belongs here.
type: memory
---

# Memory

Durable project facts shared by every assistant. This is **not** private per-assistant memory (that lives outside the repo). This directory is committed and versioned, so a fresh assistant joining the repo has the same lived context as one that has been here for months.

## What goes here

- Project-wide rules that apply to every change (commit format, push policy, hook bypass, …) — referenced by skills but stored here so they survive a skill rewrite.
- Architectural facts not derivable from the code (why a decision was made, what was rejected, what is in flight vs settled).
- Active state: which phase of [`/jiraboard.md`](../../jiraboard.md) is current; constraints not visible in the code.

## What does NOT go here

- Conventions you can infer by reading the code or `gradle/libs.versions.toml`.
- Git history — `git log` / `git blame` are authoritative.
- Debugging recipes — the fix is in the code; the commit message has the why.
- Anything already in `AGENTS.md`, `.ai/rules/`, `.ai/skills/`, or `.ai/agents/`.
- Ephemeral task state.

## Index

[`memoryIndex.md`](memoryIndex.md) is the index — one line per entry, under ~150 chars. Never put memory content in `memoryIndex.md` itself. Each entry is its own file with frontmatter.

## Entry format

```
---
name: <short id, matches filename>
description: <one line>
type: memory
---

# Title

The fact or rule, in one or two sentences.

**Why:** the reason — usually a past incident, constraint, or stakeholder ask.
**How to apply:** when and where this kicks in.
```

## Hygiene

- Update or remove an entry the moment it becomes wrong. Stale memory is worse than no memory.
- Verify memory before acting on it — a remembered file path may have been renamed; a remembered ticket may have been closed.
- Append an ISO date (`2026-05-03`) when an entry is added or last revised, in the body itself.
