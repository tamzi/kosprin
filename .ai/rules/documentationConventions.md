---
name: documentationConventions
description: camelCase .md filenames, 100-line cap on module READMEs, no copied source snippets, no stale dates.
type: rule
---

# Documentation conventions

## Filenames
- camelCase for `.md` files. Exceptions: `README.md`, `AGENTS.md`, `CLAUDE.md`, and tool-mandated wrapper names such as `.github/copilot-instructions.md`.
- One topic per file. If a file grows past ~200 lines, split it.

## Length
- Module `README.md` files are under 100 lines. They describe the module's purpose, public API, and link out to the deeper docs in `docs/`.
- `docs/` is where detail lives. Long-form architecture, runbooks, ADRs.

## Structure
- Lead with a one-paragraph "what is this and why does it exist" preamble.
- Use tables for status / matrix data (topics, services, ports). Easier to scan than prose.
- No multi-paragraph "background" sections — link to the source if more depth is needed.

## What not to write
- No copied source-code snippets in `.md` files. Link to the source file instead. Snippets rot the moment the source changes. Command examples and templates are allowed in `.ai/skills/` and `.ai/workflows/`.
- No "Last Updated" dates. Use `git log` if you need history. ADRs and memory entries may include decision dates.
- No maintainer / owner names. Files outlive employment.
- No trailing emoji or section ornamentation unless the user asked.

## Index
`docs/README.md` is the navigation index for the docs tree. Add new docs to it.

## Source of truth
- Architecture diagrams live in `docs/tech/systemdesign.md` (mermaid, not images).
- Project-level rules live in `.ai/rules/`.
- Task playbooks live in `.ai/skills/`.
- Active work and improvement backlog live in `jiraboard.md` (single source of truth, organised by priority bands P0–P9).
