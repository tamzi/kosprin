
# System Design Document

# about
This is a fictitious distributed system (like a mini YouTube/Instagram backend) at scale with the following components:

* **API Gateway & Load Balancer:** This is mostly the Entry point for clients, routing requests to microservices and handling various other concerns (authentication, rate limiting, etc). If needed, it can be separated into multiple services.
* **IAM (Identity Access Management):** Using Keycloak 26.2.1 for user identity management (login, JWT token issuance) and securing service endpoints.
* **Metadata Service:** A service for managing metadata of content (e.g. video info), backed by PostgreSQL and cached with Redis.
* **Video Processing Service:** Processes uploaded videos (simulated processing like generating thumbnails), using a work queue (Kafka) for asynchronous tasks.
* **Search Service (Aggregator):** Indexes and searches content using Elasticsearch (search index) and Redis for caching frequent queries.
* **Notification Service:** Sends notifications (e.g. emails or messages) to users based on events (like new content or activities), via an event queue.
* **Feed Generation Service:** Creates personalized content feeds for users (e.g. news feed of videos) by aggregating events and data, using Kafka and database storage.
* **CDN/Static Content:** Serves static media (images/videos) via a content delivery network or static server, decoupled from core services.
* **Data Warehousing & Analytics:** An offline data processing component (simulated with a simple analytics service) that consumes events to produce reports and metrics stored in a data warehouse, which we’ll visualize on an analytics dashboard.
* **Observability:** Integration of Prometheus (v3.3) for metrics monitoring (with Grafana dashboards) and using centralized logging/tracing tools (mentioning their roles).
* **Frontend Analytics Dashboard:** A simple front-end written in TypeScript to display system metrics or analytical data by interacting with the backend services’ APIs.

# Architecture design
## High-Level Architecture
Use [mermaid chart](https://www.mermaidchart.com/) to draw / modify the architecture diagram

```mermaid
flowchart TB
%% ──────────────── CLUSTERS ────────────────
subgraph EXTERNAL["Client & Edge"]
  direction TB
  C["Client\n(Web / Mobile)"]
  LB["Load Balancer"]
  CDN["CDN &\nStatic Content"]
end

subgraph GATEWAY["Entry Layer"]
  direction TB
  GW["API Gateway\n(Spring Cloud Gateway)"]
  KC["Keycloak\n(Auth Server)"]
end

subgraph CORE["Business Micro-services"]
  direction TB
  MS_META["Metadata Service"]
  MS_VIDEO["Video Processing Service"]
  MS_SEARCH["Search Service"]
  MS_FEED["Feed Generation Service"]
  MS_NOTIF["Notification Service"]
  MS_ANALYTICS["Analytics Service"]
end

subgraph MESSAGING["Kafka Cluster"]
  direction TB
  topicVideos(("topic: videos"))
  topicProcessed(("topic: video-processed"))
  topicNotifs(("topic: notifications"))
  topicAnalytics(("topic: analytics"))
end

subgraph DATA["State & Indexes"]
  direction LR
  PG_META[("PostgreSQL\nvideoapp_db")]
  REDIS_META[("Redis\nCache")]
  ES[("Elasticsearch\nvideos index")]
  PG_FEED[("PostgreSQL\nfeed_db")]
  PG_ANALYTICS[("PostgreSQL\nanalytics_db")]
end

subgraph OBS["Observability Stack"]
  direction TB
  PROM["Prometheus"]
  GRAF["Grafana"]
end

subgraph INFRA["Supporting Infrastructure"]
  direction TB
  MESSAGING
end

%% ──────────────── EDGES ────────────────
C --- CDN
C -- HTTPS --> LB
LB --> GW

GW <-- "OIDC (JWT)" --> KC

GW --> MS_META
GW --> MS_SEARCH
GW --> MS_FEED
GW --> MS_ANALYTICS

MS_META  -- produce  --> topicVideos
topicVideos -- consume --> MS_VIDEO
topicVideos -- consume --> MS_SEARCH
topicVideos -- consume --> MS_FEED

MS_VIDEO -- produce  --> topicProcessed
topicProcessed -- consume --> MS_META

MS_FEED  -- produce  --> topicNotifs
MS_FEED  -- produce  --> topicAnalytics
topicNotifs -- consume --> MS_NOTIF

MS_SEARCH -- produce  --> topicAnalytics
topicAnalytics -- consume --> MS_ANALYTICS

MS_META      -- JPA   --> PG_META
MS_META      -- cache --> REDIS_META
MS_SEARCH    -- index --> ES
MS_SEARCH    -- cache --> REDIS_META
MS_FEED      -- JPA   --> PG_FEED
MS_ANALYTICS -- JPA   --> PG_ANALYTICS

PROM -- "scrape /actuator/prometheus" --> MS_META
PROM -- scrape --> MS_VIDEO
PROM -- scrape --> MS_SEARCH
PROM -- scrape --> MS_FEED
PROM -- scrape --> MS_NOTIF
PROM -- scrape --> MS_ANALYTICS
PROM -- scrape --> GW
PROM --> GRAF

%% ──────────────── STYLES ────────────────
classDef svc fill:#1f78c1,stroke:#ffffff,color:#ffffff;
classDef datastore fill:#6a3d9a,stroke:#ffffff,color:#ffffff;

class C,LB,GW,KC,MS_META,MS_VIDEO,MS_SEARCH,MS_FEED,MS_NOTIF,MS_ANALYTICS svc;
class CDN,topicVideos,topicProcessed,topicNotifs,topicAnalytics,PG_META,REDIS_META,ES,PG_FEED,PG_ANALYTICS datastore;

%% Highlight core microservices
style MS_META      fill:#FF6D00
style MS_VIDEO     fill:#FF6D00
style MS_SEARCH    fill:#FF6D00
style MS_FEED      fill:#FF6D00
style MS_NOTIF     fill:#FF6D00
style MS_ANALYTICS fill:#FF6D00
```
