# Local Development

Goal: a contributor can clone the repo and have a complete stack running in under five minutes. Tracked under epic KOS-3.

## Prerequisites
- JDK 21
- Docker Desktop / Colima
- Node 20+ (for the dashboard)
- Git
- GNU Make (optional, for the `make` shortcuts)

## Bring up dependencies
```
make up
# or, equivalently:
docker compose up -d
```

Brings up: PostgreSQL (5432), Redis (6379), Kafka (9092 in-network, 29092 from host), Elasticsearch (9200), Keycloak (8080). Kafka topics (`videos`, `video-processed`, `notifications`, `analytics`) are created automatically by the `kafka-init` one-shot container.

Postgres comes with three pre-created per-service databases: `videoapp_db`, `feed_db`, `analytics_db` (see [`infra/postgres-init/`](../../infra/postgres-init/)).

## Run a service
Each service has its own port and DB. Run with the dev profile so it picks up `application-dev.yml`:

```
./gradlew :metadata-service:bootRun --args='--spring.profiles.active=dev'
```

## Run the dashboard
```
cd dashboard
npm install
npm start
```

## Common tasks

| Task                         | Command                                                                                              |
|------------------------------|------------------------------------------------------------------------------------------------------|
| Bring up infra               | `make up`                                                                                            |
| Stop infra                   | `make down`                                                                                          |
| Wipe data volumes            | `make reset`                                                                                         |
| Tail container logs          | `make logs`                                                                                          |
| Open psql shell              | `make psql`                                                                                          |
| Open redis-cli               | `make redis-cli`                                                                                     |
| List Kafka topics            | `make kafka-topics`                                                                                  |
| Build everything             | `./gradlew build` (or `make build`)                                                                  |
| Run tests for one module     | `./gradlew :metadata-service:test`                                                                   |
| Run all tests                | `./gradlew test` (or `make test`)                                                                    |
| Detekt                       | `./gradlew detekt`                                                                                   |
| Consume a Kafka topic        | `docker compose exec kafka kafka-console-consumer.sh --topic videos --bootstrap-server localhost:9092` |

## Default credentials (dev only)
- Postgres: `kosprin` / `changeme`
- Keycloak admin: `admin` / `admin`

Override via `POSTGRES_USER`, `POSTGRES_PASSWORD`, `KEYCLOAK_ADMIN`, `KEYCLOAK_ADMIN_PASSWORD` in a `.env` file or shell env.

## Troubleshooting
- **Kafka can't connect from the host** on macOS — make sure your client uses port `29092` (not 9092). Inside the docker network use `kafka:9092`.
- **Elasticsearch refuses to start** — Docker Desktop's default memory may be too low. Bump to ≥4GB.
- **Keycloak slow first boot** — start-dev mode rebuilds the realm; allow ~30s on first up.
- **Realm is empty** — the `kosprin` realm is not yet seeded. KOS-3.3 (realm export) is pending KOS-5 design.
