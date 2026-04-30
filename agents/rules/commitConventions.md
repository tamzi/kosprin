# Commit conventions

## Format
```
Added UTC timezone handling strategy
```

- Past tense ("Added", "Fixed", "Removed"), not imperative or present.
- Capitalised first letter.
- No trailing period.
- No body.
- No conventional-commits prefix (`feat:`, `fix:`, etc.) — the verb conveys the intent.
- Subject line under 72 characters.

## Bad examples
| Bad | Why |
|-----|-----|
| `feat: add timezone utility` | conventional-commits prefix |
| `fix memory leak` | wrong tense, not capitalised |
| `Fixed memory leak.` | trailing period |
| `Fixed memory leak in database\n\nDetails about ...` | has body |

## Atomicity
- Each commit changes 1–3 files unless the files are tightly coupled (a class and its test).
- Split by dependency layer: resources → models → utilities → state → UI → integration.
- One documentation `.md` change per commit. Never bundle two doc files in the same commit.
- A bug fix and the test that proves it can land together. A feature and a refactor cannot.

## Don't
- Do not amend commits unless the user explicitly asks. Hook failures should produce a new commit, not an amended one.
- Do not run `git push --no-verify` or `git push --force`. Fix the hook failure instead.
- Do not push at all without explicit user approval.

## When ticket IDs help
Reference `KOS-*` ticket IDs in the commit only when it materially helps history search. Otherwise omit them — the verb and noun should be enough.
