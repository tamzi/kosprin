---
name: commitFormat
description: Commit subjects are past tense, no prefix, no body, no period; atomic; under 72 characters.
type: memory
---

# Commit format

All commit subjects in this repo are **past tense**, **no conventional-commits prefix**, **no period**, **no body**, and **under 72 characters**. Each commit is atomic — 1–3 files unless tightly coupled (a class and its test). One `.md` per commit; never bundle two doc files.

**Why:** the project's `commit-msg` git hook enforces this, and the team relies on the past-tense convention to read history at a glance. Conventional-commits prefixes were rejected because they add noise without adding signal — the verb already conveys intent. Atomic commits make `git revert` and `git bisect` precise.

**How to apply:** every commit. The full procedure (staging, hook handling, examples) lives in [`../skills/commitChanges.md`](../skills/commitChanges.md). The hard rule is also in [`../rules/commitConventions.md`](../rules/commitConventions.md).
