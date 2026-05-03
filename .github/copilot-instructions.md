# GitHub Copilot instructions

The canonical project rules live in [`/AGENTS.md`](../AGENTS.md) and the [`/.ai/`](../.ai/README.md) directory. Read those before any non-trivial change.

## Hard constraints
- Never push without explicit user approval. Never use `--no-verify` or `--force`.
- Atomic commits. Past-tense subject (e.g. `Added metadata DTO`). No body. No conventional-commits prefix.
- One `.md` change per commit.
- Module READMEs under 100 lines; detail goes in `docs/`.
- No copied source-code snippets in `.md` files — link to source instead; command examples are allowed in playbooks.
- camelCase for `.md` filenames (exceptions: `README.md`, `AGENTS.md`, `CLAUDE.md`).
- Secrets never in repo, even as examples — use placeholders (`changeme`, `XXXX`).
- Shared envelopes, event headers, and result types come from `common/`.

## Where to look
- Architecture: [`/docs/tech/systemdesign.md`](../docs/tech/systemdesign.md)
- Per-module summary: [`/docs/tech/services.md`](../docs/tech/services.md)
- Build system: [`/docs/tech/buildSystem.md`](../docs/tech/buildSystem.md)
- Messaging: [`/docs/tech/messaging.md`](../docs/tech/messaging.md)
- Backlog: [`/jiraboard.md`](../jiraboard.md) — single source of truth, P0–P9 priority bands.

## Rule modules
- [`/.ai/rules/architecture.md`](../.ai/rules/architecture.md)
- [`/.ai/rules/codeStyle.md`](../.ai/rules/codeStyle.md)
- [`/.ai/rules/commitConventions.md`](../.ai/rules/commitConventions.md)
- [`/.ai/rules/documentationConventions.md`](../.ai/rules/documentationConventions.md)
- [`/.ai/rules/agentBehaviour.md`](../.ai/rules/agentBehaviour.md)

## Catalogs (browse the index, then drill in)
- Skills (single-task playbooks): [`/.ai/skills/README.md`](../.ai/skills/README.md)
- Agents (specialised role definitions): [`/.ai/agents/README.md`](../.ai/agents/README.md)
- Workflows (multi-skill flows): [`/.ai/workflows/README.md`](../.ai/workflows/README.md)
- Memory (decisions, glossary): [`/.ai/memory/README.md`](../.ai/memory/README.md)

If a rule conflicts with what the task asks for, surface the conflict — don't silently work around it.
