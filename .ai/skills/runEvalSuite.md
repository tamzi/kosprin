---
name: runEvalSuite
description: Eval harness pass for AI-backed features — recall/precision/cost regression gate before merge.
type: skill
---

# Skill — Run the eval suite

For AI-backed features (RAG retrievers, summarisers, classifiers, agentic flows), unit tests are necessary but not sufficient. An eval suite measures behaviour on representative inputs and gates regressions before they ship.

This skill assumes the eval harness lives at `tools/evals/` (KOS-* TBD — promote a ticket when the first AI-backed feature lands).

## When to run

- Before merging any change that touches an AI-backed code path: prompts, model versions, retriever weights, tool definitions, system instructions, eval inputs, embedding pipeline.
- On the [weeklyMaintenance](../workflows/weeklyMaintenance.md) cycle.
- Before promoting an image to staging.

## Steps

1. **Pick the suite** matching the surface that changed:
   - `tools/evals/retrieval/` — recall@k, precision@k, latency, cost-per-query.
   - `tools/evals/summary/` — faithfulness (LLM-as-judge), length compliance, refusal rate.
   - `tools/evals/agent/` — task-completion rate, average tool calls, average tokens.
2. **Run baseline first** if you have not yet, so the diff is meaningful:
   ```
   tools/evals/run.sh --suite <name> --tag baseline
   ```
3. **Run the candidate**:
   ```
   tools/evals/run.sh --suite <name> --tag candidate
   ```
4. **Diff the report**:
   ```
   tools/evals/diff.sh baseline candidate
   ```
   Look at: per-metric delta, per-input regressions (which inputs got worse and by how much), cost-per-100-runs delta.
5. **Decide**:
   - **All metrics improved or flat** → ship.
   - **One metric regressed > 2%** → investigate. Don't average it away with the wins.
   - **Cost regressed > 10%** → either justify with a quality win, or tune.
6. **Append the result** to `tools/evals/runs.csv` (append-only) and link the row from the PR description.
7. **If a regression ships intentionally** (e.g. quality wins justify cost) — record the trade in [`../memory/decisions.md`](../memory/decisions.md) so the next agent does not "fix" the regression by reverting.

## Done condition

- A baseline → candidate diff exists and is linked from the PR.
- No regression > 2% on a tracked metric is unexplained.
- The PR description has a one-line summary: `evals: recall +0.4%, latency +12ms, cost flat`.

## Common failures

- **Eval inputs leaked into prompts** — your LLM-as-judge is now scoring its own data. Hold out 20% of inputs from any prompt-tuning loop.
- **Judge model drift** — pin the judge model version. When it ships a new release, re-run the baseline before comparing.
- **Cost numbers off by a factor of N** — pricing changed. Update `tools/evals/pricing.json`.

## Composes with
- [evalSteward](../agents/evalSteward.md) — owns the harness, the inputs, the regression policy.
- [featureDelivery](../workflows/featureDelivery.md) — verify step for AI-backed features.
- [weeklyMaintenance](../workflows/weeklyMaintenance.md) — scheduled drift check.
