# Documentation conventions

## Filenames
- camelCase for `.md` files. Exceptions: `README.md`, `AGENTS.md`, `CLAUDE.md`.
- One topic per file. If a file grows past ~200 lines, split it.

## Length
- Module `README.md` files are under 100 lines. They describe the module's purpose, public API, and link out to the deeper docs in `docs/`.
- `docs/` is where detail lives. Long-form architecture, runbooks, ADRs.

## Structure
- Lead with a one-paragraph "what is this and why does it exist" preamble.
- Use tables for status / matrix data (topics, services, ports). Easier to scan than prose.
- No multi-paragraph "background" sections — link to the source if more depth is needed.

## What not to write
- No code snippets in `.md` files. Link to the source file instead. Snippets rot the moment the source changes.
- No "Last Updated" dates. Use `git log` if you need history.
- No maintainer / owner names. Files outlive employment.
- No trailing emoji or section ornamentation unless the user asked.

## Index
`docs/README.md` is the navigation index for the docs tree. Add new docs to it.

## Source of truth
- Architecture diagrams live in `docs/tech/systemdesign.md` (mermaid, not images).
- Project-level rules live in `agents/rules/`.
- Task playbooks live in `agents/skills/`.
- Active work and improvement backlog live in `jiraboard.md` and `docs/improvements.md`.
