# Local Development

Goal: a contributor can clone the repo and have a complete stack running in under five minutes. Tracked under epic KOS-3.

## Prerequisites
- JDK 21
- Docker Desktop / Colima
- Node 20+ (for the dashboard)
- Git

## Bring up dependencies
Once `docker-compose.yml` lands (KOS-3.1):

```
docker compose up -d
```

Brings up: PostgreSQL (5432), Redis (6379), Kafka (9092), Elasticsearch (9200), Keycloak (8080).

## Run a service
Each service has its own port and DB. Run with the dev profile so it picks up `application-dev.yml`:

```
./gradlew :metadata-service:bootRun --args='--spring.profiles.active=dev'
```

`./gradlew bootRun` at the root will only work once KOS-1.1 is done — currently the root applies the Spring Boot plugin, which is misconfigured for a multi-module setup.

## Run the dashboard
```
cd dashboard
npm install
npm start
```

`npm start` requires KOS-12.1 (the `package.json` has no `scripts` block yet).

## Common tasks

| Task                         | Command                                       |
|------------------------------|-----------------------------------------------|
| Build everything             | `./gradlew build`                             |
| Run tests for one module     | `./gradlew :metadata-service:test`            |
| Run all tests                | `./gradlew test`                              |
| Detekt                       | `./gradlew detekt`                            |
| Tail Kafka topic             | `docker compose exec kafka kafka-console-consumer --topic videos --bootstrap-server localhost:9092` |
| Open Postgres                | `docker compose exec postgres psql -U kosprin` |
| Reset local data             | `docker compose down -v`                      |

## Troubleshooting
- **"plugin not applied"** at the root — KOS-1.1 not done; run only per-module Gradle tasks for now.
- **Kafka can't connect** on macOS — ensure `KAFKA_ADVERTISED_LISTENERS` includes both `PLAINTEXT://kafka:9092` (for in-network) and `PLAINTEXT_HOST://localhost:29092` (for host).
- **Keycloak admin** at `http://localhost:8080`, default `admin/admin` (override via env).
