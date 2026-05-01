# Agent behaviour

Rules about how an assistant should operate, regardless of the task.

## Always
- **Finish what you start.** Complete every story in an active epic before opening a different epic. Within an epic, complete the current story end-to-end before starting another. If a story is genuinely blocked by a dependency, mark it `[~]` (or `[!]`) with a one-line reason and continue with the rest of the same epic; never silently skip a story to jump elsewhere.
- Read the relevant doc(s) under `docs/tech/` and `agents/rules/` before non-trivial changes.
- State assumptions out loud and ask when ambiguity could waste time.
- Prefer editing existing files to creating new ones.
- For ambiguous "what should we do about X?" questions, respond in 2–3 sentences with a recommendation, the main tradeoff, and an offer to elaborate. Do not start implementing.
- Match the scope of the response to the task. A simple question gets a direct answer, not headers and sections.
- When work has a natural follow-up after a soak window or feature flag rollout, mention it once at the end of the reply — do not stack reminders.

## Never
- Never push without explicit user approval. Never `--force` or `--no-verify`.
- Never amend commits unless asked.
- Never delete or rewrite branches, tags, or remote history without confirmation.
- Never commit secrets — even placeholder examples should use clearly fake values (`changeme`, `XXXX`).
- Never invent file paths, function names, or features that don't exist. If a memory or rule references something, verify it exists before recommending it.
- Never bundle multiple `.md` changes into one commit.

## When uncertain
- Surface the uncertainty in the response. Cheaper to ask than to undo.
- For irreversible operations (drop table, delete branch, rm -rf, force-push) the bar is "explicit user instruction in the same conversation". A previous approval does not generalise.

## Tooling
- Prefer dedicated tools to bash where one exists. (`Read` over `cat`, `Edit` over `sed`.)
- For broad codebase exploration, delegate to a search agent rather than scattering greps.
- Run multiple independent reads in parallel.

## Memory
- Repository-backed memory (this directory) is authoritative for project rules.
- Private memory (`~/.claude/...`) is for personal preferences only — anything other agents should also know belongs here.
- If a recalled memory conflicts with current code, trust the code. Update or remove the stale memory.
