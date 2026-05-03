---
name: auditSecrets
description: Deep secret-leak triage after a scanner hit, including rotation and history plan.
type: skill
---

# Skill - Audit Secrets

Use this when gitleaks or review suggests secret material may be present.

## Steps

1. Preserve the scanner output and identify the exact file, line, and commit range.
2. Classify the finding as real, placeholder, fixture, or false positive.
3. For real secrets, stop feature work, rotate the credential, and plan history cleanup with the user.
4. For false positives, add the narrowest ignore entry with a reason.
5. Record accepted risk or rotation notes in [`../memory/decisions.md`](../memory/decisions.md).

## Done condition

- No real secret remains usable.
- The branch has a clear fix, ignore, or history-cleanup plan.
