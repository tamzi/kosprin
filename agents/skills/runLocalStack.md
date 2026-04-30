# Skill — Run the local stack

Use this when the user wants to run kosprin locally, or when a service-level test needs real dependencies.

## Prerequisites
- JDK 21 on `PATH`
- Docker Desktop or Colima running
- Node 20+ (only needed for the dashboard)

## Step 1 — bring up infra
Once `docker-compose.yml` lands (KOS-3.1):

```
docker compose up -d
```

Brings up: PostgreSQL (5432), Redis (6379), Kafka (9092 + 29092 host listener), Elasticsearch (9200), Keycloak (8080).

If `docker-compose.yml` does not yet exist, the agent should not invent one — flag that KOS-3.1 is blocking and stop.

## Step 2 — start a single service
```
./gradlew :metadata-service:bootRun --args='--spring.profiles.active=dev'
```

Each service has its own port and DB. Do **not** run `./gradlew bootRun` at the root — the root project is not a Spring Boot app (KOS-1.1 deliberately keeps Spring Boot off the root).

## Step 3 — start the dashboard
```
cd dashboard
npm install
npm start
```

`npm start` runs webpack-dev-server on port 8081 (default). Dashboard expects the gateway on port 8080.

## Verifying
- `curl http://localhost:<port>/actuator/health` returns `{"status":"UP"}`.
- `docker compose exec kafka kafka-topics --bootstrap-server localhost:9092 --list` shows the four topics.
- Keycloak admin at `http://localhost:8080/auth` (default `admin/admin`, override via env).

## Failure modes
- **Kafka can't connect** on macOS — the compose file must publish both `PLAINTEXT://kafka:9092` (in-network) and `PLAINTEXT_HOST://localhost:29092` (host).
- **"Plugin not applied"** at the root — the build script is misconfigured (KOS-1.1). Use the per-module Gradle task instead.
- **Port already in use** — another service is on 5432/6379/9092/9200/8080. `lsof -i :<port>` to find the offender.
