# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build all modules
./gradlew build

# Build skipping tests
./gradlew build -x test

# Run tests (all modules)
./gradlew test

# Run tests for a specific module
./gradlew :erp-domain:test
./gradlew :erp-infrastructure:test

# Run a single test class
./gradlew :erp-application:test --tests "com.mzrt.erp_lite.SomeServiceTest"

# Run the application (from erp-api module)
./gradlew :erp-api:bootRun

# Start local infrastructure (Postgres, MongoDB, Redis)
docker compose up -d

# Stop infrastructure
docker compose down
```

The application runs on **port 9090**. All infrastructure credentials are in `docker-compose.yml` and mirrored in `erp-api/src/main/resources/application.yaml`.

## Module Dependency Graph

```
erp-common
    └── erp-domain
            └── erp-application
                    └── erp-infrastructure ──┐
                                             └── erp-api  (bootable fat JAR)
erp-common ──────────────────────────────────┘
```

- `erp-common` — shared value objects, exceptions, and utility types
- `erp-domain` — domain aggregates, entities, value objects, and port interfaces (no Spring)
- `erp-application` — use cases / application services that implement or orchestrate domain ports
- `erp-infrastructure` — outbound adapters: JPA (PostgreSQL), MongoDB, Redis
- `erp-api` — Spring Boot entry point (`main`), `application.yaml`, and inbound REST adapters (controllers)

The `apply plugin: 'org.springframework.boot'` is **only in `erp-api/build.gradle`**, making it the sole deployable artifact. All other modules compile to plain JARs.

## Hexagonal Architecture Conventions

Ports live in `erp-domain` or `erp-application`; adapters live in `erp-infrastructure` (outbound) or `erp-api` (inbound). Never add Spring/framework annotations to `erp-domain`.

**Inbound flow:**
`erp-api` controller → `erp-application` use-case → `erp-domain` aggregate

**Outbound flow:**
`erp-domain` port interface ← implemented by → `erp-infrastructure` adapter

Infrastructure adapter package structure:
```
erp-infrastructure/src/main/java/com/mzrt/erp_lite/adapter/out/persistence/
    jpa/
        entity/       ← JPA @Entity classes (NOT domain objects)
        repository/   ← Spring Data JPA interfaces
    mongo/
        document/     ← MongoDB @Document classes
        repository/   ← Spring Data Mongo interfaces
```

## Polyglot Persistence Model

Three databases with intentionally separate concerns:

| Store | Use case | Tables / Collections |
|---|---|---|
| PostgreSQL 17 | Transactional data (write side) | `products`, `orders`, `order_products` |
| MongoDB 8 | Documents and read-model | `catalogs`, `product_documents`, `audit_logs` |
| Redis 7 | Catalog cache | — |

The `category_id` column on the `products` table is a plain `VARCHAR` referencing a MongoDB catalog item ID — there is no FK across databases.

## Tech Stack

- **Spring Boot 4.1.0**, **Java 25**
- **Lombok** — on all modules; use `@Builder`, `@Getter`/`@Setter`, `@NoArgsConstructor`/`@AllArgsConstructor`
- **MapStruct 1.6.3** — for mapping between JPA/document entities and domain objects; the annotation processor order (lombok → mapstruct) is managed via `lombok-mapstruct-binding`
- **JPA ddl-auto: validate** — schema is managed by the SQL scripts in `db/postgresql/`, never by Hibernate
- **MongoDB auto-index-creation: true** — indexes declared on `@Document` classes are created automatically

## Local Infrastructure

`docker-compose.yml` at the repo root provides:
- PostgreSQL on `5432` (db: `erp_db`, user: `postgresql`, pass: `secret`)
- MongoDB on `27017` (db: `erp_catalog_db`, user: `mongo`, pass: `secret`)
- Redis on `6379` (pass: `secret`)
- Redis Commander UI on `8081` (user: `mzrt`, pass: `secret`)

Init scripts run automatically on first container start: `db/postgresql/01-schema.sql`, `db/postgresql/02-data.sql`, `db/mongodb/init-mongo.js`.
