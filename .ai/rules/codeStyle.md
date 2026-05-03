---
name: codeStyle
description: Kotlin/JDK 21 + TypeScript conventions, naming, package layout, error handling, formatting.
type: rule
---

# Code style

## Kotlin
- Target JDK 21. Kotlin 2.1.20.
- Prefer immutable data: `val`, `data class`, `List<T>` over `MutableList<T>` unless mutation is necessary.
- No `!!` operator unless a comment justifies why it can never be null.
- Use `Result<T>` / `Outcome<T>` from `common/` for service-layer returns rather than throwing for control flow.
- Coroutines for async; do not mix with raw `CompletableFuture` in the same call chain.
- Package layout per service: `domain/`, `application/`, `infrastructure/`, `interfaces/`. Controllers live under `interfaces/`, repositories under `infrastructure/`.
- Tests: JUnit 5 + MockK + Kotest assertions. Test classes mirror the production package.

## TypeScript (dashboard)
- Strict mode on. No `any` without an inline justification.
- Functional React components, hooks-only state. Co-locate styles with components.
- API access through a single `apiClient` (axios). Components do not call `axios` directly.
- State management: Zustand stores grouped by feature, never one global mega-store.

## Formatting
- Kotlin: Detekt + ktlint defaults; pre-commit enforces.
- TypeScript: Prettier + ESLint; `npm run lint` and `npm run format` are the gates.

## Naming
- Files: Kotlin classes `PascalCase.kt`. TS components `PascalCase.tsx`. Markdown `camelCase.md` (exceptions: `README.md`, `AGENTS.md`, `CLAUDE.md`).
- Database: `snake_case` table and column names; Kotlin entity properties remain `camelCase`.
- Kafka topics: kebab-case (`video-processed`).
- Env vars: `SCREAMING_SNAKE_CASE` with a `KOSPRIN_` prefix for app-specific ones.

## Error handling
- Wrap external calls (DB, HTTP, Kafka) in service-layer methods that translate to `Outcome`.
- Controllers translate `Outcome` → HTTP via the shared `@ControllerAdvice` in `common/`.
- Kafka consumers must be idempotent and route poison messages to the topic's DLT.
