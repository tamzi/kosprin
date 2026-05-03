---
name: docRules
description: Markdown filenames are camelCase; module READMEs under 100 lines; no copied source snippets; no stale dates or maintainer names.
type: memory
---

# Documentation rules

`.md` filenames are **camelCase**, except `README.md`, `AGENTS.md`, `CLAUDE.md`, and tool-mandated wrapper names such as `.github/copilot-instructions.md`. Module-level `README.md` files stay **under 100 lines** — long-form detail goes under `docs/`. **No copied source-code snippets** in `.md` files; link to the source file instead. Command examples and templates are allowed in `.ai/skills/` and `.ai/workflows/`. **No "Last Updated" dates** and **no maintainer / owner names** — `git log` knows both. ADRs and memory entries may include decision dates.

**Why:** docs that include code snippets rot the moment the source changes; "Last Updated" lines lie about reality and are never updated; maintainer names outlive employment. The 100-line cap forces summary, not exhaustion — long content belongs in a focused `docs/<area>/<topic>.md` file linked from the README.

**How to apply:** every doc change. The full procedure lives in [`../rules/documentationConventions.md`](../rules/documentationConventions.md). The pre-commit hook enforces filename casing and the 100-line cap on module READMEs.
