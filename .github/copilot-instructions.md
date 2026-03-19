# EcoTrack API: AI Coding Agent Instructions

**Project**: EcoTrack API – Carbon compliance and emission tracking backend  
**Stack**: Spring Boot 3.4.5 · Java 21 · PostgreSQL 17 · JHipster 8.11  
**Domain**: Environmental/regulatory compliance (CBAM, Scope 1/2/3 emissions)  
**Base package**: `com.ecotrack.api`  
**Main class**: `EcotrackApiApp` (`com.ecotrack.api.EcotrackApiApp`)

---

## Architecture Overview

### Core Principles

- **JHipster-generated** Spring Boot application following JHipster conventions (entity generation, `*Resource` controllers, `*ServiceImpl` delegation, `EntityMapper` base interface)
- **Stateless JWT auth** — no server-side sessions; `SessionCreationPolicy.STATELESS`
- **Shared-database multi-tenancy** via PostgreSQL Row-Level Security (RLS) — tenant isolation enforced at the DB level, not in Java
- **DTO-first API layer** — controllers never expose JPA entities directly; all go through MapStruct mappers
- **Interface-segregated services** — public service interfaces in `service/`, implementations in `service/impl/`

### Key Packages & Responsibilities

| Package            | Responsibility                                                                                |
| ------------------ | --------------------------------------------------------------------------------------------- |
| `domain/`          | JPA entities extending `AbstractAuditingEntity`, enumerations, JPA converters                 |
| `repository/`      | Spring Data JPA repository interfaces                                                         |
| `service/`         | Service interfaces (CRUD contracts + business logic)                                          |
| `service/impl/`    | Service implementations (`@Service`, `@Transactional`)                                        |
| `service/carbon/`  | **Carbon Calculation Engine** — Strategy + Template Method patterns                           |
| `service/dto/`     | Data Transfer Objects with Jakarta Validation annotations                                     |
| `service/mapper/`  | MapStruct `@Mapper` interfaces extending `EntityMapper<D, E>`                                 |
| `service/utils/`   | Utility POJOs (e.g., `RecordMetadata`)                                                        |
| `web/rest/`        | REST controllers (`*Resource`), error handling, view models                                   |
| `web/rest/errors/` | RFC 7807 Problem Details: `ExceptionTranslator`, `BadRequestAlertException`, `ErrorConstants` |
| `web/rest/vm/`     | View Models: `LoginVM`, `ManagedUserVM`, `KeyAndPasswordVM`                                   |
| `web/websocket/`   | STOMP WebSocket handlers                                                                      |
| `config/`          | Spring configuration classes (Security, Cache, DB, WebSocket, etc.)                           |
| `security/`        | Auth utilities, `TenantFilter`, `TenantContext`, `AuthoritiesConstants`                       |
| `aop/logging/`     | `LoggingAspect` — method-level debug logging (dev profile only)                               |
| `management/`      | `SecurityMetersService` — Micrometer counters for invalid JWT tokens                          |

---

## Multi-Tenancy & Security

### Critical: Row-Level Security (RLS)

The system uses a **shared-database, shared-schema** model with PostgreSQL RLS:

```
JWT (tenant_id claim)
  → TenantFilter (servlet filter, runs after Spring Security auth filter)
    → TenantContext.setTenantId() — InheritableThreadLocal for app layer
    → PostgreSQL: SELECT set_config('app.current_tenant', ?, true) — transaction-scoped
      → RLS policies: WHERE tenant_id = current_setting('app.current_tenant')
  → finally: clears both TenantContext and DB session variable
```

**Rules:**

- Do NOT apply tenant filtering in Java — rely on PostgreSQL RLS policies
- `TenantFilter` returns **401 Unauthorized** if an authenticated request lacks a `tenant_id` JWT claim
- Unauthenticated (public) endpoints pass through without tenant context
- `TenantContext` uses `InheritableThreadLocal` — tenant ID propagates to `@Async` child threads
- Connection handling uses `DataSourceUtils.getConnection()` / `releaseConnection()` for proper Spring-managed pooling
- The Liquibase migration `20260216000001_add_rls_multi_tenancy.xml` defines the RLS policies

### Tenant ID Enforcement Across Entities

| Enforcement              | Entities                                                                                  |
| ------------------------ | ----------------------------------------------------------------------------------------- |
| `@NotBlank` (required)   | `Supplier`, `SupplierType`, `Importer`, `AiBudget`, `ProductEmission`, `EmissionEvidence` |
| `@NotNull` (required)    | `EmissionRecord`, `AuditTrail`, `EmissionReport`, `ComplianceReport`                      |
| Optional (no annotation) | `Product`, `Installation`                                                                 |
| **No `tenantId`**        | `Lead` (public/marketing), `PriceTier` (global pricing), `User`, `Authority`              |

### Authentication

- **JWT** via Spring Security OAuth2 Resource Server (HS512 symmetric key)
- Secret: `jhipster.security.authentication.jwt.base64-secret`
- Custom JWT claims: `auth` (authorities), `userId` (numeric), `tenant_id` (string)
- `AuthenticateController` encodes JWT using Spring's `JwtEncoder` and returns `id_token` in body + `Authorization` header
- `SecurityJwtConfiguration` allows JWT in **URI query params** for WebSocket handshake support
- Method-level security: `@EnableMethodSecurity(securedEnabled = true)`

### Roles (`AuthoritiesConstants`)

| Constant    | Value            | Purpose                         |
| ----------- | ---------------- | ------------------------------- |
| `ADMIN`     | `ROLE_ADMIN`     | System administrator            |
| `USER`      | `ROLE_USER`      | Regular authenticated user      |
| `IMPORTER`  | `ROLE_IMPORTER`  | CBAM importer (domain-specific) |
| `SUPPLIER`  | `ROLE_SUPPLIER`  | CBAM supplier (domain-specific) |
| `ANONYMOUS` | `ROLE_ANONYMOUS` | Unauthenticated                 |

### Public Endpoints (no auth required)

| Endpoint                             | Method    | Purpose                                                  |
| ------------------------------------ | --------- | -------------------------------------------------------- |
| `/api/authenticate`                  | GET, POST | JWT login + auth check                                   |
| `/api/register`                      | POST      | User self-registration                                   |
| `/api/activate`                      | GET       | Account activation                                       |
| `/api/account/reset-password/init`   | POST      | Request password reset                                   |
| `/api/account/reset-password/finish` | POST      | Complete password reset                                  |
| `/api/notify-me`                     | POST      | Public lead capture (idempotent — 201 new, 200 existing) |
| `/management/health/**`              | GET       | Health checks                                            |
| `/management/info`                   | GET       | App info                                                 |
| `/management/prometheus`             | GET       | Metrics scrape                                           |

### Protected Endpoints

- `/api/admin/**` → `ROLE_ADMIN` only
- `/api/**` → authenticated
- `/websocket/**` → authenticated
- `/v3/api-docs/**` → `ROLE_ADMIN` only
- `/management/**` → `ROLE_ADMIN` only

---

## Domain Model

### Base Class: `AbstractAuditingEntity<T>`

`@MappedSuperclass` with `@EntityListeners(AuditingEntityListener.class)`. All entities inherit:

| Field              | Type      | Notes                                                      |
| ------------------ | --------- | ---------------------------------------------------------- |
| `createdBy`        | `String`  | `@CreatedBy`, non-updatable, length 50                     |
| `createdDate`      | `Instant` | `@CreatedDate`, non-updatable, defaults to `Instant.now()` |
| `lastModifiedBy`   | `String`  | `@LastModifiedBy`, length 50                               |
| `lastModifiedDate` | `Instant` | `@LastModifiedDate`, defaults to `Instant.now()`           |

Audit fields are `@JsonIgnoreProperties(allowGetters = true)` — serialized on reads, ignored on writes.

### Entity Relationship Map

```
Supplier ─┬── @OneToMany ──► Product         (mappedBy="supplier")
           └── @OneToMany ──► Installation    (mappedBy="supplier")
Supplier ── @ManyToOne ──► SupplierType

EmissionReport ─┬── @OneToMany (cascade ALL, orphanRemoval) ──► EmissionRecord
                 └── @OneToMany (cascade ALL, orphanRemoval) ──► EmissionEvidence
```

**Loose-coupled string ID references** (NOT JPA foreign keys):

- `EmissionRecord` → `supplierId`, `installationId`, `productEmissionId` (all `String`)
- `ComplianceReport` → `emissionReportId` (`String`)
- `ProductEmission` → `productId` (`String`)

### Key Entities

| Entity             | Table                  | ID Type      | Notable Fields                                                                               |
| ------------------ | ---------------------- | ------------ | -------------------------------------------------------------------------------------------- |
| `Product`          | `eco_product`          | `Long`       | `sku` (unique), `totalCarbonFootprint` (BigDecimal), supplier FK                             |
| `Supplier`         | `eco_supplier`         | `Long`       | `companyName`, `contactEmail`, owns Products + Installations                                 |
| `SupplierType`     | `eco_supplier_type`    | `Long`       | `name`, `category` — referenced by Supplier                                                  |
| `Installation`     | `installation`         | `Long`       | `installationName`, `country`, `unlocode` (UN/LOCODE)                                        |
| `EmissionRecord`   | `emission_record`      | `Long`       | **`carbonGrams` (Long)**, `scope` (enum), `confidenceScore`, `metadata` (JSON via converter) |
| `EmissionEvidence` | `emission_evidence`    | `Long`       | `evidenceType` (enum), file metadata (`fileName`, `filePath`, `checksum`)                    |
| `EmissionReport`   | `emission_report`      | `Long`       | **Aggregate root** — owns EmissionRecords + EmissionEvidence with cascade                    |
| `ComplianceReport` | `compliance_report`    | `Long`       | `reportType`, `status`, per-scope emission totals (BigDecimal)                               |
| `Importer`         | `eco_importer`         | `Long`       | CBAM-specific: `eoriNumber`, `vatNumber`, `sector` (enum)                                    |
| `Tenant`           | `eco_tenant`           | **`String`** | Manual ID (not generated), `industry` (Sector enum), `region`                                |
| `ProductEmission`  | `eco_product_emission` | `Long`       | CBAM: `cnCode` (Combined Nomenclature), `quantity`, `productId` (string ref)                 |
| `AiBudget`         | `eco_ai_budget`        | `Long`       | AI usage tracking: `amount`, `consumed`, `purchased`                                         |
| `AuditTrail`       | `audit_trail`          | `Long`       | Generic change log: `action` (enum), `entityType`, `entityId`, `oldValue`/`newValue` (TEXT)  |
| `Lead`             | `eco_lead`             | `Long`       | Marketing: `email` (unique), `source`, no tenantId                                           |
| `PriceTier`        | `eco_price_tier`       | `Long`       | `features` (`@ElementCollection` → `eco_price_tier_features` table), no tenantId             |

### Enumerations

| Enum           | Values                                                                                                                                                                   | Used By                              |
| -------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ------------------------------------ |
| `Scope`        | `SCOPE_1`, `SCOPE_2`, `SCOPE_3`                                                                                                                                          | `EmissionRecord.scope`               |
| `Sector`       | `IRON_AND_STEEL`, `ALUMINUM`, `CEMENT`, `FERTILIZERS`, `ELECTRICITY`, `HYDROGEN`, `GLASS`, `CHEMICALS`, `MANUFACTURING` (each has a `label`)                             | `Tenant.industry`, `Importer.sector` |
| `ReportStatus` | `DRAFT`, `PENDING_REVIEW`, `APPROVED`, `SUBMITTED`, `ACCEPTED`, `REJECTED`                                                                                               | `ComplianceReport.status`            |
| `ReportType`   | `CBAM_DECLARATION`, `VERIFICATION_REPORT`, `INTERNAL_AUDIT`, `REGULATORY_FILING`                                                                                         | `ComplianceReport.reportType`        |
| `EvidenceType` | `PDF_INVOICE`, `IOT_SENSOR_LOG`, `CALCULATION_SPREADSHEET`, `THIRD_PARTY_CERTIFICATE`, `UTILITY_BILL`, `TRANSPORT_MANIFEST`, `SUPPLIER_DECLARATION`, `REGULATORY_REPORT` | `EmissionEvidence.evidenceType`      |
| `AuditAction`  | `CREATE`, `UPDATE`, `DELETE`, `VERIFY`, `SUBMIT`, `APPROVE`, `REJECT`                                                                                                    | `AuditTrail.action`                  |

### JPA Converters

- `RecordMetadataConverter` — `AttributeConverter<RecordMetadata, String>` using Jackson `ObjectMapper` for JSON↔POJO. Used by `EmissionRecord.metadata` and `EmissionReport.metadata`.

### Cross-Cutting Entity Patterns

- **ID generation**: `@GeneratedValue(SEQUENCE)` everywhere except `Tenant` (manual `String` ID)
- **L2 cache**: All entities annotated with `@Cache(CacheConcurrencyStrategy.READ_WRITE)`; collections on `Supplier` are also cache-annotated
- **BigDecimal**: Used for monetary/carbon values (precision 21, scale 2); exception: `EmissionRecord.carbonGrams` is `Long` (integer grams), `uncertaintyFactor` uses scale 4
- **Fluent setters**: JHipster-style `entity.field(value)` returning `this`
- **`@JsonIgnoreProperties`**: On relationships to prevent infinite recursion

---

## Carbon Calculation Engine

### Design Patterns

The calculation engine uses **Strategy** + **Template Method**:

```
CarbonCalculationResource (REST)
  → CarbonCalculatorService (orchestrator, @Transactional)
    → Map<Scope, EmissionCalculationStrategy> (auto-collected @Components)
      → AbstractEmissionCalculationStrategy.compute() (shared formula)
        → Scope1DirectEmissionStrategy
        → Scope2IndirectEmissionStrategy (has default EU grid factor)
        → Scope3SupplyChainEmissionStrategy
    → CarbonCalculationMapper (MapStruct, enriches DTOs)
    → EmissionRecordService / EmissionReportService (persistence delegation)
```

### Core Formula

$$E = \text{activityData} \times \text{emissionFactor} \times (1 - \text{efficiencyRatio})$$

Result rounded to `Long` grams via `HALF_UP`.

### Scope Strategies

| Strategy                            | Scope     | Emission Factor                                   | Calculation Method String            |
| ----------------------------------- | --------- | ------------------------------------------------- | ------------------------------------ |
| `Scope1DirectEmissionStrategy`      | `SCOPE_1` | Required from request                             | `SCOPE_1_DIRECT:E=A*EF*(1-ER)`       |
| `Scope2IndirectEmissionStrategy`    | `SCOPE_2` | Falls back to `DEFAULT_2026_EU_GRID_FACTOR = 125` | `_2026_EU_GRID` or `_CUSTOM_FACTOR`  |
| `Scope3SupplyChainEmissionStrategy` | `SCOPE_3` | Required from request                             | `SCOPE_3_SUPPLY_CHAIN:E=A*EF*(1-ER)` |

### `EmissionComputation` (Output Value Object)

Java `record` with: `carbonGrams`, `emissionFactorUsed`, `efficiencyRatioApplied`, `calculationMethod`.

### Carbon Calculation Endpoints

| Method | Path                                       | Action                                                |
| ------ | ------------------------------------------ | ----------------------------------------------------- |
| POST   | `/api/carbon-calculations/estimate`        | Calculate only — no persistence (200 OK)              |
| POST   | `/api/carbon-calculations/`                | Calculate + persist as `EmissionRecord` (201 Created) |
| POST   | `/api/carbon-calculations/emission-report` | Calculate + persist as `EmissionReport` (201 Created) |

### Validation Rules (in `CarbonCalculatorService`)

- `tenantId` required, `scope` required
- `activityData > 0`, `emissionFactor > 0` (if present), `0 ≤ efficiencyRatio ≤ 1`
- Report requests additionally require `supplierId`
- Failures throw `IllegalArgumentException`, caught by controller as `BadRequestAlertException`

### Transaction Boundaries

- `CarbonCalculatorService`: class-level `@Transactional` (read-write)
- `calculate()` (estimate): overridden with `@Transactional(readOnly = true)`
- `calculateAndSaveEmissionRecord()` / `calculateToEmissionReport()`: inherit class-level read-write

---

## Service Layer Patterns

### Standard CRUD Pattern (all entities follow this)

1. **Service interface** (`service/XxxService.java`) — declares `save`, `update`, `partialUpdate`, `findAll(Pageable)`, `findOne`, `delete`
2. **Service impl** (`service/impl/XxxServiceImpl.java`) — `@Service`, `@Transactional` (class-level R/W), read operations marked `@Transactional(readOnly = true)`
3. **Repository** (`repository/XxxRepository.java`) — Spring Data JPA interface
4. **Mapper** (`service/mapper/XxxMapper.java`) — MapStruct `@Mapper(componentModel = "spring")` extending `EntityMapper<D, E>`

### `EntityMapper<D, E>` Base Interface

Generic MapStruct contract providing: `toEntity`, `toDto`, list variants, and `partialUpdate` with `@BeanMapping(nullValuePropertyMappingStrategy = IGNORE)`.

### Error Handling

- `ExceptionTranslator` (`@ControllerAdvice`) — catches all exceptions and maps to **RFC 7807 Problem Details**
- `BadRequestAlertException` — extends `ErrorResponseException` with `message` and `params` properties
- `MethodArgumentNotValidException` → field errors list
- `ConcurrencyFailureException` → concurrency error key
- Custom exceptions: `InvalidPasswordException`, `EmailAlreadyUsedException`, `UsernameAlreadyUsedException`

---

## Observability & Monitoring

### Metrics

- **Prometheus**: enabled via Micrometer (`micrometer-registry-prometheus-simpleclient`); actuator at `/management/prometheus`
- **JWT security counters**: `security.authentication.invalid-tokens` with dimensions: `invalid-signature`, `expired`, `unsupported`, `malformed` (tracked by `SecurityMetersService`)
- **Percentile histograms**: enabled on all metrics (60s step)

### Logging

- **Logback** with `CRLFLogConverter` for CRLF normalization
- **AOP LoggingAspect** (dev profile only): `@AfterThrowing` logs exceptions (full stack in dev, cause-only in prod); `@Around` logs method entry/exit at DEBUG level
- **Dev logging**: DEBUG for ROOT, JHipster, Hibernate SQL, `com.ecotrack.api`
- **Prod logging**: INFO across all packages

### Custom Actuator Endpoints

Exposed: `health`, `prometheus`, `loggers`, `liquibase`, `caches`, `ecometrics`, `ecoopenapigroups`

---

## Configuration

### Environment Profiles

| Profile    | Port    | Database                                                                  | Liquibase Contexts | Logging | CORS                         |
| ---------- | ------- | ------------------------------------------------------------------------- | ------------------ | ------- | ---------------------------- | ------------------------- |
| `dev`      | `5080`  | PostgreSQL @ `localhost:5432/ecotrackApi` (`${DB_USER}`/`${DB_PASSWORD}`) | `dev, faker`       | DEBUG   | Enabled for `localhost:8100` |
| `prod`     | `5080`  | PostgreSQL @ `localhost:5432/ecotrackApi` (env vars)                      | `prod`             | INFO    | Disabled                     |
| `test`     | `10344` | Testcontainers PostgreSQL 17.4                                            | `test`             | —       | —                            |
| `api-docs` | —       | —                                                                         | —                  | —       | —                            | Enables SpringDoc OpenAPI |

### Caching

- **Ehcache** (JSR-107) as Hibernate L2 cache
- Dev: 1h TTL, 100 max entries; Prod: 1h TTL, 1000 max entries
- Pre-configured caches: `USERS_BY_LOGIN_CACHE`, `USERS_BY_EMAIL_CACHE`, `User`, `Authority`, `User.authorities`
- `PrefixedKeyGenerator` includes Git/build properties for cache key versioning across deployments

### WebSocket

- STOMP over SockJS at `/websocket/tracker`
- Simple in-memory broker on `/topic`
- Handshake captures client IP address
- Anonymous fallback for missing principals during handshake

### Mail

- `@Async` email via `JavaMailSender` + Thymeleaf templates (`mail/activationEmail`, `mail/creationEmail`, `mail/passwordResetEmail`)
- Config from `JHipsterProperties.getMail()`

---

## Dependency & Version Management

### Key Versions (from `pom.xml`)

| Dependency         | Version                    |
| ------------------ | -------------------------- |
| Spring Boot        | 3.4.5                      |
| Java               | 21                         |
| JHipster Framework | 8.11.0                     |
| MapStruct          | 1.6.3                      |
| Ehcache            | 3.10.8                     |
| SpringDoc OpenAPI  | 2.8.8                      |
| ArchUnit           | 1.4.0                      |
| Checkstyle         | 10.23.1                    |
| Jib                | 3.4.5                      |
| Spotless           | 2.44.4                     |
| Testcontainers     | managed by Spring Boot BOM |

### Key Dependencies

- **Web server**: Undertow (Tomcat excluded)
- **DTO mapping**: MapStruct `@Mapper` with `mapstruct-processor` annotation processor
- **Validation**: Jakarta Validation (`spring-boot-starter-validation`) — `@NotNull`, `@NotBlank`, `@Email`, `@Size`, etc.
- **OpenAPI**: `springdoc-openapi-starter-webmvc-api` auto-generates `/v3/api-docs`
- **Security**: `spring-boot-starter-oauth2-resource-server` + `spring-boot-starter-security` + `spring-security-data` + `spring-security-messaging`
- **Database**: PostgreSQL driver, HikariCP pool, Hibernate ORM (with jcache + jpamodelgen), Liquibase
- **Caching**: Ehcache 3 (Jakarta classifier) via JSR-107 `cache-api`
- **Serialization**: Jackson (`datatype-hibernate6`, `datatype-hppc`, `datatype-jsr310`, `module-jaxb-annotations`)
- **Testing**: Testcontainers (PostgreSQL), ArchUnit, Spring Security Test

---

## File Locations & Examples

All paths are relative to `src/main/java/com/ecotrack/api/`:

| Pattern           | Location                                                     | Purpose                                      |
| ----------------- | ------------------------------------------------------------ | -------------------------------------------- |
| Entity            | `domain/Product.java`                                        | JPA entity + validations                     |
| Enumeration       | `domain/enumeration/Scope.java`                              | Enum constants                               |
| JPA Converter     | `domain/converter/RecordMetadataConverter.java`              | JSON↔POJO attribute conversion              |
| Repository        | `repository/ProductRepository.java`                          | Spring Data JPA interface                    |
| Service interface | `service/ProductService.java`                                | Business logic contract                      |
| Service impl      | `service/impl/ProductServiceImpl.java`                       | Implementation with `@Transactional`         |
| Carbon strategy   | `service/carbon/Scope1DirectEmissionStrategy.java`           | Scope-specific calculation                   |
| DTO               | `service/dto/ProductDTO.java`                                | Transfer object with validation              |
| Mapper            | `service/mapper/ProductMapper.java`                          | MapStruct `@Mapper` extending `EntityMapper` |
| Base mapper       | `service/mapper/EntityMapper.java`                           | Generic mapper interface                     |
| REST controller   | `web/rest/ProductResource.java`                              | HTTP endpoints (delegate to service)         |
| Error handling    | `web/rest/errors/ExceptionTranslator.java`                   | RFC 7807 `@ControllerAdvice`                 |
| View model        | `web/rest/vm/LoginVM.java`                                   | Request-only POJO                            |
| Security filter   | `security/TenantFilter.java`                                 | RLS tenant propagation                       |
| Config            | `config/SecurityConfiguration.java`                          | Security filter chain                        |
| Integration test  | `src/test/java/.../web/rest/ProductResourceIT.java`          | MockMvc + Testcontainers                     |
| Unit test         | `src/test/java/.../service/CarbonCalculatorServiceTest.java` | Mockito, no Spring context                   |

### Liquibase Migrations

Located at `src/main/resources/config/liquibase/changelog/`. Naming convention: `YYYYMMDD000NNN_description.xml`.

Master changelog at `src/main/resources/config/liquibase/master.xml` includes all changelogs sequentially. Seed data in `src/main/resources/config/liquibase/data/` (`user.csv`, `authority.csv`, `user_authority.csv`).

---

## Testing

### Test Infrastructure

- **`@IntegrationTest`** — custom composed annotation combining `@SpringBootTest` + `@EmbeddedSQL` (Testcontainers PostgreSQL 17.4)
- **Database**: Testcontainers with tmpfs-backed, reusable `PostgreSQLContainer`; wired via `SqlTestContainersSpringContextCustomizerFactory` in `spring.factories`
- **REST tests**: `@AutoConfigureMockMvc` + `@WithMockUser` + `@Transactional` for automatic rollback
- **Unit tests**: `@ExtendWith(MockitoExtension.class)` with `@Mock` — no Spring context (e.g., `CarbonCalculatorServiceTest`)
- **Architecture tests**: ArchUnit (`TechnicalStructureTest`) validates package dependencies
- **JUnit 5 config**: 15s default timeout, 60s for `@BeforeAll`, `SpringBootTestClassOrderer` for context sharing
- **Test port**: `10344`

### Running Tests

```bash
./mvnw verify                            # All tests (unit + integration)
./mvnw test                              # Unit tests only
./mvnw verify -Pfailsafe                 # Integration tests only
./mvnw checkstyle:check                  # Code style validation
```

---

## CI/CD & Deployment

### Jenkins Pipeline

- **Trigger**: `pollSCM('H H * * *')` — daily
- **Build**: `./mvnw clean package jib:dockerBuild -DskipTests` (Jib plugin, production profile)
- **Push**: Tags `2026.01.${BUILD_NUMBER}` + `latest` to private registry `docker.jojoaddison.net`
- **Cleanup**: `cleanWs()` after each build

### Docker

- **Multi-stage Dockerfile**: `eclipse-temurin:21-jdk` (build) → `eclipse-temurin:21-jre` (runtime)
- **Jib plugin**: Alternative containerization via `./mvnw jib:dockerBuild` (no Dockerfile needed)
- **Image**: `ecotrackapi`
- **Maven profiles**: `-Pdev`, `-Pprod`, `-Pwar`, `-Pe2e`, `-Papi-docs`, `-Pno-liquibase`

### Build Commands

```bash
./mvnw                                   # Dev mode (default goal: spring-boot:run)
./mvnw -Pprod clean verify               # Production build with tests
./mvnw -Pprod clean verify jib:dockerBuild  # Production Docker image
./mvnw spotless:apply                    # Auto-format code
```

---

## Common Pitfalls & Solutions

1. **RLS tenant leakage**: The `TenantFilter` always clears context in `finally`. Never bypass this filter or set `app.current_tenant` manually outside of it. If adding new public endpoints, ensure they are listed in `SecurityConfiguration.permitAll()` — otherwise the filter will reject requests missing `tenant_id`.

2. **Tenant ID inconsistency**: Some entities have `tenantId` as optional (`Product`, `Installation`) while others require it. When creating new entities, decide upfront and use `@NotBlank` for required tenancy. Always include `tenantId` in DTOs.

3. **String ID cross-references**: `EmissionRecord`, `ComplianceReport`, and `ProductEmission` use **string IDs** (not JPA FKs) for cross-aggregate references. When writing queries or reports that join these, use explicit string-based lookups — there are no JPA relationship traversals.

4. **Carbon value types**: `EmissionRecord.carbonGrams` is `Long` (integer grams). All other carbon values (`totalCarbonFootprint`, scope emissions on `ComplianceReport`) are `BigDecimal(21,2)`. Be careful with type conversions.

5. **Database migrations**: Liquibase changelogs follow `YYYYMMDD000NNN_description.xml` naming. The `master.xml` includes changelogs sequentially plus has JHipster needle comments for code generation. If Liquibase hangs on startup, check for lock entries in `DATABASECHANGELOGLOCK` table.

6. **Cache invalidation**: Entity caches use `READ_WRITE` concurrency strategy. The `PrefixedKeyGenerator` namespaces keys by build version — redeploys get fresh caches automatically.

7. **Missing strategies**: If adding a new `Scope` enum value, you MUST create a corresponding `EmissionCalculationStrategy` `@Component` that returns the new scope from `supportsScope()`. The `CarbonCalculatorService` auto-collects strategies via constructor injection into `Map<Scope, Strategy>`.

8. **Async tenant propagation**: `TenantContext` uses `InheritableThreadLocal`, so `@Async` methods inherit the tenant. However, if using custom thread pools or `CompletableFuture.supplyAsync()` with a non-inheriting executor, tenant context will be lost.

---

## References

- [Spring Boot 3.4 Reference](https://docs.spring.io/spring-boot/docs/3.4.5/reference/html/)
- [JHipster 8.x Documentation](https://www.jhipster.tech/documentation-archive/v8.11.0)
- [PostgreSQL Row-Level Security](https://www.postgresql.org/docs/17/ddl-rowsecurity.html)
- [MapStruct 1.6 Reference](https://mapstruct.org/documentation/1.6/reference/html/)
