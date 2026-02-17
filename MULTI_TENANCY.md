# Multi-Tenancy Implementation Guide

## Overview

This project implements **Row-Level Security (RLS)** based multi-tenancy using PostgreSQL. This is the gold standard for SaaS applications as it provides tenant isolation at the database layer, preventing data leakage even if there are bugs in application code.

## Architecture

### Components

1. **TenantContext** (`ecotrack-api/security/TenantContext.java`)

   - Thread-local storage for tenant ID
   - Used to propagate tenant information from JWT to database queries
   - Automatically cleaned up after each request

2. **TenantFilter** (`ecotrack-api/security/TenantFilter.java`)

   - Servlet filter that intercepts all authenticated requests
   - Extracts `tenant_id` from JWT claims
   - Sets PostgreSQL session variable: `SET LOCAL app.current_tenant = 'tenant_123'`
   - Ensures RLS policies can filter data by tenant

3. **SecurityUtils** (Updated)

   - Added `getCurrentTenantId()` method to extract tenant from JWT
   - Added `TENANT_ID_CLAIM` constant for JWT claim name

4. **SecurityConfiguration** (Updated)
   - Registers TenantFilter in the filter chain
   - Executes after authentication but before authorization

### Gateway (ecotrack-gw)

The gateway uses **Reactive WebFlux**, so the implementation is different:

1. **TenantContext** (`ecotrack-gw/security/TenantContext.java`)

   - Uses Reactor Context instead of ThreadLocal
   - Provides reactive-safe tenant propagation

2. **TenantWebFilter** (`ecotrack-gw/security/TenantWebFilter.java`)
   - Reactive WebFilter for tenant extraction
   - Adds `X-Tenant-ID` header to downstream service calls
   - Propagates tenant via Reactor Context

## Database Schema

### Tables with Tenant Isolation

The following tables have `tenant_id` column and RLS policies:

- `eco_product`
- `eco_supplier`
- `eco_importer`
- `installation`
- `emission_record`
- `emission_report`
- `compliance_report`

### RLS Policies

Each table has two policies:

1. **tenant*isolation_policy*[table]**: Main policy that filters by `app.current_tenant`

   ```sql
   CREATE POLICY tenant_isolation_policy_product ON eco_product
       FOR ALL
       USING (tenant_id = current_setting('app.current_tenant', true));
   ```

2. **tenant*isolation_bypass*[table]**: Bypass policy for database owner (for migrations)
   ```sql
   CREATE POLICY tenant_isolation_bypass_product ON eco_product
       FOR ALL
       TO CURRENT_USER
       USING (true)
       WITH CHECK (true);
   ```

### Indexes

Tenant-aware indexes for performance:

```sql
CREATE INDEX idx_product_tenant_id ON eco_product(tenant_id);
CREATE INDEX idx_supplier_tenant_id ON eco_supplier(tenant_id);
-- ... etc
```

## JWT Token Structure

Your JWT must include the `tenant_id` claim:

```json
{
  "sub": "user@example.com",
  "userId": 123,
  "tenant_id": "tenant_steel_001",
  "auth": ["ROLE_USER"],
  "iat": 1708099200,
  "exp": 1708185600
}
```

## How It Works

### Request Flow

1. **Client sends request** with JWT in `Authorization: Bearer <token>` header

2. **Spring Security authenticates** the JWT

3. **TenantFilter executes**:

   ```java
   String tenantId = extractTenantFromJwt(); // -> "tenant_steel_001"
   TenantContext.setTenantId(tenantId);
   setTenantInDatabase(tenantId); // -> SET LOCAL app.current_tenant = 'tenant_steel_001'
   ```

4. **Application code executes** (no changes required):

   ```java
   List<Product> products = productRepository.findAll();

   ```

5. **PostgreSQL applies RLS**:

   ```sql
   -- What JPA generates:
   SELECT * FROM eco_product;

   -- What PostgreSQL executes with RLS:
   SELECT * FROM eco_product
   WHERE tenant_id = current_setting('app.current_tenant', true);
   ```

6. **TenantFilter cleanup**:
   ```java
   finally {
       TenantContext.clear();
       clearTenantInDatabase(); // -> RESET app.current_tenant
   }
   ```

### Data Insertion

When creating new entities, you must set the `tenant_id`:

```java
Product product = new Product();
product.setName("Steel Ingot");
product.setTenantId(SecurityUtils.getCurrentTenantId().orElseThrow());
productRepository.save(product);
```

The RLS `WITH CHECK` clause ensures you can only insert data for your own tenant.

## Testing Multi-Tenancy

### Unit Tests

Mock the tenant context in your tests:

```java
@BeforeEach
void setup() {
  TenantContext.setTenantId("tenant_test_001");
}

@AfterEach
void cleanup() {
  TenantContext.clear();
}

```

### Integration Tests

Use different tenant IDs in fixtures:

```java
// Tenant A data
Supplier supplierA = new Supplier("ACME Corp", "acme@example.com", "tenant_steel_001");

// Tenant B data
Supplier supplierB = new Supplier("Widgets Inc", "widgets@example.com", "tenant_green_002");

// Test isolation
TenantContext.setTenantId("tenant_steel_001");
List<Supplier> results = supplierRepository.findAll();
assertThat(results).hasSize(1);
assertThat(results.get(0).getCompanyName()).isEqualTo("ACME Corp");
```

### Database Verification

Connect to PostgreSQL and verify RLS:

```sql
-- Check RLS is enabled
SELECT tablename, rowsecurity
FROM pg_tables
WHERE schemaname = 'public' AND tablename = 'eco_product';

-- Check policies
SELECT * FROM pg_policies WHERE tablename = 'eco_product';

-- Test policy manually
SET app.current_tenant = 'tenant_steel_001';
SELECT * FROM eco_product; -- Should only see tenant_steel_001 products
```

## Migration

### Liquibase Changelog

The RLS implementation is in:

```
src/main/resources/config/liquibase/changelog/20260216000001_add_rls_multi_tenancy.xml
```

It's automatically included in `master.xml`.

### Migrating Existing Data

If you have existing data without `tenant_id`, you need to populate it:

```sql
-- Example: Assign all existing products to a default tenant
UPDATE eco_product SET tenant_id = 'tenant_default' WHERE tenant_id IS NULL;

-- Then make it NOT NULL
ALTER TABLE eco_product ALTER COLUMN tenant_id SET NOT NULL;
```

## Security Considerations

### Critical Security Rules

1. **NEVER filter by tenant in Java code**

   - ❌ Bad: `productRepository.findByTenantId(tenantId)`
   - ✅ Good: `productRepository.findAll()` (RLS handles filtering)

2. **Always set tenant_id on creation**

   ```java
   product.setTenantId(SecurityUtils.getCurrentTenantId().orElseThrow());
   ```

3. **Validate tenant_id in DTOs**

   ```java
   @NotNull(message = "Tenant ID cannot be null")
   private String tenantId;

   ```

4. **Test tenant isolation**
   - Write tests that verify tenant A cannot see tenant B's data
   - Use `@PreAuthorize` for additional authorization checks

### Bypassing RLS (Admin Operations)

For super-admin operations that need to see all tenants:

```java
// Disable RLS for this session (use with extreme caution)
@PostAuthorize("hasRole('SUPER_ADMIN')")
public void adminOperation() {
  jdbcTemplate.execute("SET LOCAL row_security = OFF");
  // ... do admin work
  jdbcTemplate.execute("SET LOCAL row_security = ON");
}

```

## Performance

### Index Strategy

All tenant-aware queries use composite indexes:

```sql
CREATE INDEX idx_product_tenant_sku ON eco_product(tenant_id, sku);
CREATE INDEX idx_supplier_tenant_name ON eco_supplier(tenant_id, company_name);
```

### Query Planning

Check query plans to ensure indexes are used:

```sql
EXPLAIN ANALYZE
SELECT * FROM eco_product
WHERE tenant_id = current_setting('app.current_tenant', true);
```

### Connection Pooling

RLS works with connection pools because we use `SET LOCAL` which only affects the current transaction:

- Each transaction gets a fresh `app.current_tenant` value
- No cross-contamination between requests

## Troubleshooting

### No data returned

1. Check JWT has `tenant_id` claim:

   ```bash
   echo "your.jwt.token" | cut -d. -f2 | base64 -d | jq
   ```

2. Verify session variable is set:

   ```sql
   SHOW app.current_tenant;
   ```

3. Check RLS is enabled:
   ```sql
   SELECT tablename, rowsecurity FROM pg_tables WHERE tablename = 'eco_product';
   ```

### Wrong tenant data visible

1. Verify TenantFilter is registered:

   ```java
   // In SecurityConfiguration
   .addFilterAfter(tenantFilter, UsernamePasswordAuthenticationFilter.class)
   ```

2. Check filter order in debug logs:

   ```
   logging.level.com.ecotrack.api.security.TenantFilter=DEBUG
   ```

3. Verify database policies:
   ```sql
   SELECT * FROM pg_policies WHERE tablename = 'eco_product';
   ```

## References

- [PostgreSQL Row Security Policies](https://www.postgresql.org/docs/current/ddl-rowsecurity.html)
- [Spring Security Filter Chain](https://docs.spring.io/spring-security/reference/servlet/architecture.html)
- [Reactor Context](https://projectreactor.io/docs/core/release/reference/#context)
- [JHipster Security](https://www.ecopster.tech/security/)

## Demo Tenants

Test data includes these tenants:

- `tenant_steel_001`: Steel manufacturing company
- `tenant_green_002`: Green energy importer
- `tenant_demo`: Demo/sandbox tenant

See `EcoTrack_Pro_Project_v2026/demo/` for sample data.
