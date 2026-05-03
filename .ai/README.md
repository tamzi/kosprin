# /.ai/

Vendor-neutral source of truth for AI assistants working in this repo (Claude Code, Cursor, Codex, Copilot, and successors). Every assistant-specific config file points here through a thin wrapper, so there is one place to update when conventions change.

| Assistant     | Wrapper                                                |
|---------------|--------------------------------------------------------|
| Claude Code   | [`/CLAUDE.md`](../CLAUDE.md)                           |
| Cursor        | [`/.cursor/rules/main.mdc`](../.cursor/rules/main.mdc) |
| OpenAI Codex / generic | [`/AGENTS.md`](../AGENTS.md)                  |
| GitHub Copilot| [`/.github/copilot-instructions.md`](../.github/copilot-instructions.md) |

Add a new assistant by creating its native config file and pointing it here. Wrappers stay short — they reference modules under `rules/`, `skills/`, `agents/`, `workflows/`, or `memory/` rather than duplicating content.

## Layout

```
.ai/
├── README.md       # this file
├── rules/          # constraints — true regardless of task; rarely change
├── skills/         # task playbooks — ordered steps for one recurring task
├── agents/         # specialised role definitions for delegation
├── workflows/      # multi-skill flows that chain agents and skills
└── memory/         # durable project context shared across assistants
```

## How to choose

| You need to…                                     | Look in     |
|--------------------------------------------------|-------------|
| know what is forbidden / mandatory               | `rules/`    |
| follow a recipe for one recurring task           | `skills/`   |
| pick the right reviewer / role for a task        | `agents/`   |
| run an end-to-end flow (plan → ship, prePush)    | `workflows/`|
| recall a past decision or term                   | `memory/`   |

## Why split rules vs skills vs agents vs workflows
- **Rules** are constraints that hold regardless of the task at hand. If you keep correcting an assistant on the same topic, the correction belongs here.
- **Skills** are recipes — one task, ordered steps. If you keep re-deriving the same sequence, write it down here.
- **Agents** are role personas — what a "reviewer" or "security auditor" should do, what they care about, what they refuse to ship. Use them when delegating or when adopting a checklist.
- **Workflows** chain skills and agents together for end-to-end work that spans more than one task (e.g. "feature delivery" or "pre-push checklist").
- **Memory** is the durable project context every assistant should share — decisions ledger, glossary, environment notes.

## File-format contract

Every entry under `rules/`, `skills/`, `agents/`, `workflows/`, and `memory/` starts with YAML frontmatter:

```
---
name: <short id, matches the filename without `.md`>
description: <one line — used by validators, adapters, and humans skimming>
type: rule | skill | agent | workflow | memory
uses: [optional, list of other entries this one composes]
---
```

After the frontmatter, the body is plain markdown. Keep each file under ~100 lines. No copied source-code snippets — link to source paths in the repo. Command examples and templates are allowed in skills and workflows. No "Last Updated" dates, no maintainer references.

## Updating
- Update the markdown directly. Wrappers do not need changes unless you add or remove a top-level file.
- One `.md` per commit, per the [documentation conventions](rules/documentationConventions.md).
- When you add a new entry, also add a one-line pointer to the relevant `README.md` index in the same commit (the only legal exception to the one-doc-per-commit rule).
- For memory entries, also update [`memory/memoryIndex.md`](memory/memoryIndex.md).

## Assistant adapter notes

Assistant-specific wrappers should stay thin and point at `.ai/`. Do not put project rules in local assistant state.

Claude Code local settings (`.claude/settings.local.json`, lock files, caches) are private machine state and ignored by git. If a committed adapter layer is added later, it must read from `.ai/` and the validator should be extended to check it.
