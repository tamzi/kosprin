---
name: kotlinSpringConventions
description: Module, package, naming, and DTO conventions for kosprin services.
type: skill
---

# Skill — Kotlin/Spring conventions

The conventions every JVM module follows. The full code-style rules live in [`../rules/codeStyle.md`](../rules/codeStyle.md); this is the operational summary.

## Package layout per service

```
com.kosprin.<service>
├── domain/          # entities, value objects, repository interfaces
├── application/     # use cases, services, orchestration
├── infrastructure/  # repository impls, kafka producers/consumers, external clients
└── interfaces/      # controllers (REST), event listeners (Kafka)
```

Cross-cutting concerns shared across services live under `com.kosprin.common.*` (see [`../memory/modulesLayout.md`](../memory/modulesLayout.md) for what's already there: `Outcome`, `ApiError`, `DomainEvent`, `EventHeaders`, `CorrelationIdFilter`, `GlobalExceptionHandler`, Kafka correlation interceptors).

## Build setup

Use the convention plugins under `buildLogic/`:
- `kosprin.kotlin-library` — pure Kotlin module (no Spring).
- `kosprin.spring-library` — Spring deps without web (e.g. `common/`).
- `kosprin.spring-service` — full Spring Boot service module.
- `kosprin.kafka-consumer` — Kafka consumer service module.

Pick by role; don't add Spring deps directly to a `build.gradle.kts`.

## Class / file conventions

- Files match class name in `PascalCase.kt`. One public class per file.
- Suffixes: `Controller`, `Service`, `Repository`, `RepositoryImpl`, `Entity`, `Dto`, `Request`, `Response`, `Event`, `Listener`, `Producer`, `Mapper`.
- DTOs are immutable `data class`. Validate with `@Valid` + Jakarta Bean Validation annotations.
- Use `Outcome<T>` from `common/` for service-layer returns instead of throwing for control flow. Controllers translate to HTTP via the shared `GlobalExceptionHandler` in `common/`.

## Things to avoid

- No `!!` operator unless a comment justifies why it can never be null.
- No `MutableList<T>` unless mutation is necessary in the same scope.
- No raw `findAll()` for unbounded collections — accept `Pageable` and return `Page<T>`.
- No raw `KafkaTemplate` — use the producer wrapper from `common/` so headers are populated correctly.
- No Domain types in `common/`. Domain belongs to the service that owns it.
- No JPA-style `@OneToMany` chains inside DTOs. Map explicitly with `from(entity)` / `toEntity()`.

## References

- [`../rules/codeStyle.md`](../rules/codeStyle.md) — the full code style rules.
- [`../rules/architecture.md`](../rules/architecture.md) — module boundary rules that constrain what can live where.
- `common/src/main/kotlin/com/kosprin/common/` — patterns to mirror.
