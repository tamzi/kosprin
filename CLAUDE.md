# CLAUDE.md

Project rules live in [`AGENTS.md`](AGENTS.md) and the [`/.ai/`](.ai/README.md) tree. That is the source of truth for every assistant.

## Claude-specific notes
- The `.ai/skills/` directory is the project's playbook library. Treat the files there as procedural memory: when the user asks for one of those tasks, follow the linked file rather than re-deriving steps.
- The `.ai/agents/` directory defines specialised role personas (reviewer, securityAuditor, releaseManager, etc.). When the work matches a role, adopt that agent's checklist before acting.
- The `.ai/workflows/` directory chains skills into end-to-end flows (feature delivery, pre-push, security review). Use them when a task spans more than one skill.
- The `.ai/memory/` directory is the durable project memory shared by every assistant — decisions ledger, glossary, environment notes. Update it when you learn something other agents will also need.
- Track work against [`/jiraboard.md`](jiraboard.md) ticket IDs (`KOS-*`). When implementing a sub-task, reference its ID in commits where useful, but do not bundle the ID into the subject line.
- Private memory (`~/.claude/projects/.../memory/`) is for personal preferences only — anything that should apply to other agents in this repo belongs in `.ai/rules/` or `.ai/memory/` instead.

## Repo conventions
See [`.ai/rules/`](.ai/rules/) for the full list. Highlights:
- Atomic commits, past-tense subjects, no conventional-commits prefix, no body.
- One documentation `.md` change per commit.
- Module READMEs stay under 100 lines.
- No copied source-code snippets in `.md` files; command examples are allowed in playbooks.
- camelCase filenames for docs (with the usual `README.md` / `AGENTS.md` exceptions).
- Secrets never go in the repo, even as examples.
- Shared envelopes, event headers, and result types come from `common/`.
- Never push without explicit approval. Never use `--no-verify` or `--force`.
