# CLAUDE.md

Project rules live in [`AGENTS.md`](AGENTS.md) and the [`agents/`](agents/) tree. That is the source of truth for every assistant.

## Claude-specific notes
- The `agents/skills/` directory is the project's playbook library. Treat the files there as procedural memory: when the user asks for one of those tasks, follow the linked file rather than re-deriving steps.
- Track work against [`/jiraboard.md`](jiraboard.md) ticket IDs (`KOS-*`). When implementing a sub-task, reference its ID in commits where useful, but do not bundle the ID into the subject line.
- Private memory (`~/.claude/projects/.../memory/`) is for personal preferences only — anything that should apply to other agents in this repo belongs in `agents/rules/` instead.

## Repo conventions
See [`agents/rules/`](agents/rules/) for the full list. Highlights:
- Past-tense atomic commits, no conventional-commits prefix, no body.
- One documentation `.md` change per commit.
- camelCase filenames for docs (with the usual `README.md` / `AGENTS.md` exceptions).
- Never push without explicit approval.
