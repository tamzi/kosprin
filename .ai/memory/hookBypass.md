---
name: hookBypass
description: Never bypass git hooks; fix the underlying problem instead.
type: memory
---

# Hook bypass policy

`--no-verify` is **never** used in this repo. Pre-commit, commit-msg, and pre-push hooks are mandatory. When a hook fails, the cause is fixed and the operation re-tried — never bypassed.

**Why:** the hooks codify the rules the team actually relies on (commit format, atomic-commit shape, dep-update check, secret scan). Bypassing one silently degrades every later assumption that depends on it. There is no scenario where bypassing the hook is faster than fixing the cause — the cause is going to bite within hours either way.

**How to apply:** if a hook reports a failure, read its output, fix the cause, re-stage, create a **new** commit (never `--amend` unless the user explicitly asked). If the hook itself appears broken, surface that to the user before working around it.
