.PHONY: help up down reset logs ps psql redis-cli kafka-topics build test clean install-hooks validate-ai

help:
	@echo "Common dev tasks:"
	@echo "  make up           - bring up the local stack (postgres, redis, kafka, elasticsearch, keycloak)"
	@echo "  make down         - stop the local stack (keeps volumes)"
	@echo "  make reset        - stop the local stack and wipe all data volumes"
	@echo "  make logs         - tail logs from every container"
	@echo "  make ps           - show container status"
	@echo "  make psql         - open a psql shell against the postgres container"
	@echo "  make redis-cli    - open a redis-cli against the redis container"
	@echo "  make kafka-topics - list Kafka topics"
	@echo "  make build        - ./gradlew build"
	@echo "  make test         - ./gradlew test"
	@echo "  make clean        - ./gradlew clean"
	@echo "  make validate-ai  - validate AGENTS.md and the .ai/ knowledge graph"
	@echo "  make install-hooks - install repo-local git hooks"

up:
	docker compose up -d

down:
	docker compose down

reset:
	docker compose down -v

logs:
	docker compose logs -f

ps:
	docker compose ps

psql:
	docker compose exec postgres psql -U kosprin

redis-cli:
	docker compose exec redis redis-cli

kafka-topics:
	docker compose exec kafka /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list

build:
	./gradlew build

test:
	./gradlew test

clean:
	./gradlew clean

validate-ai:
	scripts/validateAiSetup.py

install-hooks:
	git config core.hooksPath .githooks
