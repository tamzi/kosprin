# agents/

Source of truth for AI-assistant rules and task playbooks. Vendor-neutral by design — every supported assistant points back here through a thin wrapper:

| Assistant     | Wrapper                          |
|---------------|----------------------------------|
| Claude Code   | [`/CLAUDE.md`](../CLAUDE.md)     |
| Cursor        | [`/.cursor/rules/main.mdc`](../.cursor/rules/main.mdc) |
| OpenAI Codex / generic | [`/AGENTS.md`](../AGENTS.md) |
| GitHub Copilot | (planned) `.github/copilot-instructions.md` |

Add a new assistant by creating its native config file and pointing it here. Do not duplicate rule content into the wrappers — wrappers should be short and reference the relevant module under `rules/` or `skills/`.

## Layout

```
agents/
├── README.md                # this file
├── rules/                   # what an assistant must / must not do
│   ├── architecture.md
│   ├── codeStyle.md
│   ├── commitConventions.md
│   ├── documentationConventions.md
│   └── agentBehaviour.md
└── skills/                  # how to perform routine tasks
    ├── runLocalStack.md
    ├── runTests.md
    ├── addNewService.md
    └── addKafkaTopic.md
```

## Why split rules vs skills
- **Rules** are constraints: things that are true regardless of the task at hand. Rarely change.
- **Skills** are recipes: ordered steps for a specific recurring task. Updated as the codebase evolves.

When you find yourself re-deriving the same sequence of steps in a session, it probably belongs in `skills/`. When you find yourself correcting an assistant on the same topic twice, it probably belongs in `rules/`.

## Updating
- Update the markdown under `rules/` or `skills/`. Wrappers do not need changes unless you add or remove a top-level file.
- One `.md` per commit, per the project documentation conventions.
