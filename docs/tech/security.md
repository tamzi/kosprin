# Security & IAM

## Identity provider
Keycloak 26.2.1 is the source of truth for user identity. The repo will not implement its own auth.

- **Realm**: `kosprin`
- **Clients**:
  - `kosprin-gateway` — public, authorization-code + PKCE flow for the dashboard.
  - `kosprin-services` — confidential, client-credentials flow for service-to-service calls.
- **Roles**: `user`, `creator`, `moderator`, `admin`. Mapped onto Spring authorities via `realm_access.roles`.

## Token validation
- The gateway validates JWTs using Keycloak's JWKS endpoint and forwards the bearer token downstream.
- Each service is configured as an OAuth2 resource server. Issuer URI is the only required config.
- Public endpoints (health, OpenAPI) are explicitly allow-listed; everything else requires a valid token.

## Service-to-service
Two options, decide per call:
1. **Forward the user token** when the call is made on behalf of an authenticated user.
2. **Client credentials** when the call is system-initiated (e.g. nightly analytics rollup).

## Network
- mTLS between services in production (KOS-15.4) — easiest path is a service mesh (Istio/Linkerd).
- The gateway terminates TLS at the edge.
- Internal services are not directly reachable from outside the cluster.

## Secrets
- Never check secrets into the repo.
- Local: `.env` files (gitignored) consumed by `docker-compose`.
- Production: Kubernetes Secrets sourced from External Secrets Operator (KOS-17.4).

## Threat model checkpoints
- OWASP Top 10 review per service before that service ships.
- Dependency-Check + Trivy scans gate every CI build (KOS-15.2).
- Audit log for admin actions sinks to the `analytics` topic with a separate retention policy.

## Why no custom auth service
Building auth correctly is a multi-quarter effort (token revocation, MFA, social login, password reset, federation, audit). Keycloak gives all of that for free; we only invest in integration.
