# Build System

Kotlin multi-module Gradle build (Kotlin DSL). Tracked under epic KOS-1.

## Current state
- Root [`build.gradle.kts`](../../build.gradle.kts) declares `org.springframework.boot`, `io.spring.dependency-management`, and three Kotlin plugins **without `apply false`** despite a comment claiming otherwise. The plugins are therefore applied to the root project, which is not a Spring Boot app — this is the source of the "issue with gradle runs" noted in [`README.md`](../../README.md).
- All eight submodule `build.gradle.kts` files are empty (0 bytes).
- Versions for Spring Cloud, Kafka, and SpringDoc are stashed in `ext[...]` strings on the root `Project` — not consumable as plugin coordinates and brittle to refactor.

## Target state

### Root build script
```
plugins {
    id("org.springframework.boot") version "3.2.2" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
    kotlin("jvm") version "2.1.20" apply false
    kotlin("plugin.spring") version "2.1.20" apply false
    kotlin("plugin.jpa") version "2.1.20" apply false
}
```
…with shared config (Java toolchain, Detekt, JaCoCo) applied to `subprojects`.

### Version catalog
A `gradle/libs.versions.toml` file consumed by every module. One source of truth for Spring Boot, Spring Cloud, Kafka, Kotlin, Testcontainers.

### Convention plugins
Under `buildLogic/` (separate `includeBuild`):
- `kosprin.kotlin-library` — base Kotlin + test config for non-Spring modules (e.g. `common`).
- `kosprin.spring-service` — Spring Boot + Actuator + observability + JSON logging.
- `kosprin.kafka-consumer` — adds spring-kafka + dead-letter handling defaults.

Each service then becomes a one-liner:
```
plugins {
    id("kosprin.spring-service")
    id("kosprin.kafka-consumer")
}
```

### Settings
`settings.gradle.kts` should add:
```
dependencyResolutionManagement {
    repositories { mavenCentral() }
}
```
…and an `includeBuild("buildLogic")` block.

## Why convention plugins instead of `subprojects { ... }`
- Type-safe, debuggable Kotlin instead of stringly-typed config blocks.
- Plugins can be applied selectively — `common` shouldn't get the Spring Boot plugin, `gateway` shouldn't get JPA.
- IntelliJ resolves them like real plugins.

## Code quality gates
- **Detekt** at root with `config/detekt/detekt.yml`. Fails the build on warnings.
- **JaCoCo** aggregating module coverage. Threshold enforced in CI (KOS-14.5), not local builds.
- **ktlint** (or Detekt's formatting rules) wired into pre-commit.
