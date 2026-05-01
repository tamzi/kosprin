# Frontends

Two independently-deployable single-page apps consuming the same backend through the gateway.

## Why two
Real distributed systems separate consumer-facing UIs from operator/admin UIs because they have different stakeholders, deployment cadences, security models, and performance budgets. Bundling them into one frontend forces every change to ship to both audiences, and gives operators access to (or visibility into) end-user account flows they shouldn't touch.

The split also gives the template a place to demonstrate **polyglot frontends** without the complexity of microfrontends — see [Why not microfrontends](#why-not-microfrontends) below.

---

## dashboard-react — end-user content app

**Audience:** end users browsing or uploading content. Real-world parallel: the YouTube/Instagram client.

**Auth:** Keycloak OAuth, regular `user` and `creator` roles.

**Stack:** React 19 + TypeScript + MUI + webpack 5.

**Routes & content:**

| Route                  | Backend                  | Purpose                                          |
|------------------------|--------------------------|--------------------------------------------------|
| `/`                    | `feed-service`           | Personalised feed (cards: thumb, title, creator) |
| `/search`              | `search-service`         | Debounced full-text search over video metadata   |
| `/videos/:id`          | `metadata-service`       | Video detail page with playback stub             |
| `/upload`              | `metadata-service`       | Create-video form (title, description, tags)     |
| `/notifications`       | `notification-service`   | Bell + dropdown of user notifications            |
| `/profile`             | `metadata-service` + JWT | Current user info, owned content                 |

**Why React for this side:** large component-library ecosystem (MUI/Radix/etc.), good fit for content-heavy UIs, excellent TS inference.

**Source:** [`dashboard/`](../../dashboard/) (currently the React scaffold; will be renamed `dashboard-react/` when the Vue app lands).

---

## dashboard-vue — operator/analytics dashboard

**Audience:** operators, analysts, business stakeholders. Real-world parallel: the back-office tool every product builds in year two.

**Auth:** Keycloak OAuth, `moderator` and `admin` roles only.

**Stack:** Vue 3 + TypeScript + Vuetify (or Element Plus) + Vite or webpack 5.

**Routes & content:**

| Route          | Backend                            | Purpose                                          |
|----------------|------------------------------------|--------------------------------------------------|
| `/`            | `analytics-service`                | KPI cards: videos uploaded today, DAU, search QPS, error rate |
| `/content`     | `analytics-service`                | Top-N videos (bar chart), search-volume trend (line) |
| `/users`       | `analytics-service`                | Signup curve, retention cohorts                  |
| `/services`    | each service's `/actuator/health`  | Per-service health table with last-error counts  |
| `/kafka`       | Prometheus query / analytics       | Per-topic consumer-lag gauges                    |

**Why Vue for this side:** lighter runtime, simpler reactivity model — a common pick for chart-heavy admin tools. Smaller TS surface for the templates means snappy hot-reload during dashboard tweaks.

**Source:** [`dashboard-vue/`](../../dashboard-vue/) (to be created — see [KOS-19](../../jiraboard.md)).

---

## What is shared

| Concern              | Mechanism                                                  |
|----------------------|------------------------------------------------------------|
| Identity             | Same Keycloak realm `kosprin`; different role requirements |
| API surface          | Both go through the gateway at `/api/v1/...`               |
| Error envelope       | RFC 7807 (`ProblemDetail`) returned by every service       |
| Correlation tracing  | `X-Correlation-Id` header injected by the gateway          |
| OpenAPI specs        | Each service publishes `/v3/api-docs`; clients are codegen'd from these |

## What is not shared
- npm dependency tree (each app owns its `package.json` and `node_modules`).
- Build pipeline (separate webpack config; one app could move to Vite without affecting the other).
- Runtime (no module federation, no shared bundles, no cross-framework state).
- State management (React side uses Zustand; Vue side uses Pinia).
- Component library (MUI vs Vuetify).

## Routing at the gateway

The gateway is shared. It does not know or care which frontend issued a request — auth is JWT-based, and role checks happen per-route. Path prefixes:

| Prefix                | Routed to               | Required role     |
|-----------------------|-------------------------|-------------------|
| `/api/v1/feed/**`     | `feed-service`          | `user`            |
| `/api/v1/videos/**`   | `metadata-service`      | `user` for read, `creator` for write |
| `/api/v1/search/**`   | `search-service`        | `user`            |
| `/api/v1/notifications/**` | `notification-service` | `user`       |
| `/api/v1/analytics/**` | `analytics-service`    | `moderator`       |
| `/actuator/health/**` | each service            | `admin`           |

## Independent deployability

Each frontend has its own CI pipeline, its own static-asset bucket, its own CDN distribution, and its own hostname (e.g. `app.kosprin.local` vs `ops.kosprin.local`). A regression in one cannot block a release of the other. They can be owned by different teams without coordinating shipping cadences.

## Local development

```
docker compose up -d
cd dashboard          && npm install && npm start   # http://localhost:3000
cd dashboard-vue      && npm install && npm start   # http://localhost:3001
```

Both proxy `/api/*` to the gateway on port 8080.

## Why not microfrontends

Module Federation between React 19 and Vue 3 in a single shell costs ~1.5MB of dual framework runtime, introduces shared-dependency-hell, and only pays off when multiple frontend teams need independent deployability *into the same shell*. For this template — a learning reference — two separate apps deliver the same "polyglot frontend" demonstration with one-tenth the moving parts and zero runtime coupling. If a real product needs MF later, that's a deliberate decision; it shouldn't be the default.
