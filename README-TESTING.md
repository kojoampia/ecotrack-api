# Multi-Tenancy Testing Guide

This document describes how to test the Row-Level Security (RLS) multi-tenancy implementation.

## Test Files

### 1. Integration Test Suite

**File**: `src/test/java/com/ecotrack/api/security/MultiTenancyIntegrationTest.java`

Comprehensive JUnit 5 test suite that validates:

- TenantContext storage and retrieval
- Tenant isolation at repository level
- Cross-tenant data access prevention
- Thread-safety of tenant context
- CRUD operations with tenant filtering

**Run with**:

```bash
./mvnw test -Dtest=MultiTenancyIntegrationTest
```

### 2. Shell Script Validator

**File**: `test-multitenancy.sh`

Automated bash script that validates:

- Database connectivity
- RLS status on all tables
- RLS policy existence
- Tenant ID columns and indexes
- Data isolation with session variables
- Query plan optimization
- Java test execution

**Run with**:

```bash
chmod +x test-multitenancy.sh
./test-multitenancy.sh
```

**Environment variables**:

```bash
DB_HOST=localhost \
DB_PORT=5432 \
DB_NAME=ecotrackpro \
DB_USER=EcoTrackPro \
DB_PASSWORD=your_password \
./test-multitenancy.sh
```

### 3. SQL Verification Script

**File**: `verify-rls.sql`

Interactive PostgreSQL script that:

- Lists all RLS-enabled tables
- Shows all RLS policies
- Displays tenant_id indexes
- Tests tenant isolation manually
- Analyzes query execution plans
- Performs security breach tests

**Run with**:

```bash
psql -h localhost -U EcoTrackPro -d ecotrackpro -f verify-rls.sql
```

## Test Scenarios

### Scenario 1: Basic Tenant Isolation

**Goal**: Verify each tenant can only see their own data

**Steps**:

1. Start application with test profile
2. Run MultiTenancyIntegrationTest
3. Check that Test 5, 6, 7 pass (tenant isolation tests)

**Expected**: Each tenant sees only their own products and suppliers

### Scenario 2: Cross-Tenant Security

**Goal**: Ensure tenants cannot access other tenants' data

**Steps**:

1. Set tenant context to Tenant A
2. Query for Tenant B's products by ID
3. Verify zero results returned

**Expected**: RLS prevents access even with known IDs

### Scenario 3: Database-Level Enforcement

**Goal**: Verify RLS works at database layer, not just application

**Steps**:

1. Connect directly to PostgreSQL
2. Run verify-rls.sql
3. Check Section 6 output

**Expected**: Session variables control data visibility

### Scenario 4: Performance Validation

**Goal**: Ensure queries use tenant_id indexes

**Steps**:

1. Run verify-rls.sql
2. Check Section 7 (query execution plans)
3. Look for "Index Scan" on idx\_\*\_tenant_id

**Expected**: PostgreSQL uses indexes for tenant filtering

### Scenario 5: JWT Integration

**Goal**: Test full request flow with JWT tokens

**Steps**:

1. Generate JWT with `tenant_id` claim:

```json
{
  "sub": "user@tenant-a.com",
  "tenant_id": "tenant_steel_001",
  "auth": ["ROLE_USER"]
}
```

2. Make API request with JWT in Authorization header
3. Verify TenantFilter extracts tenant_id and sets session variable

**Tools**: Use Postman or curl:

```bash
curl -H "Authorization: Bearer YOUR_JWT" \
     http://localhost:8080/api/products
```

### Scenario 6: Thread Safety

**Goal**: Verify tenant context doesn't leak between threads

**Steps**:

1. Run MultiTenancyIntegrationTest#testTenantContextThreadSafety
2. Verify each thread maintains separate context

**Expected**: No cross-contamination between threads

## Test Data

### Pre-defined Tenants

The tests use these tenant IDs:

- `tenant_steel_001` - Steel manufacturing company
- `tenant_green_002` - Green energy importer
- `tenant_demo` - Demo/sandbox tenant

### Sample Products

Each tenant gets test products:

- Tenant A: SKU-A-001, SKU-A-002
- Tenant B: SKU-B-001, SKU-B-002
- Tenant C: SKU-C-001

## Debugging Failed Tests

### RLS Not Enabled

**Symptom**: All tenants see all data

**Solution**:

```sql
-- Check if migration ran
SELECT * FROM databasechangelog
WHERE id = '20260216000001-3';

-- Manually enable RLS
ALTER TABLE eco_product ENABLE ROW LEVEL SECURITY;
```

### No Policies Found

**Symptom**: Tests fail with "no matching policies"

**Solution**:

```sql
-- Check policies exist
SELECT * FROM pg_policies WHERE tablename = 'eco_product';

-- Manually create policy
CREATE POLICY tenant_isolation_policy_product ON eco_product
    FOR ALL
    USING (tenant_id = current_setting('app.current_tenant', true));
```

### TenantFilter Not Executing

**Symptom**: Session variable not set, queries return no data

**Solution**:

1. Check SecurityConfiguration has TenantFilter registered
2. Verify JWT contains `tenant_id` claim
3. Add debug logging:

```java
logging.level.com.ecotrack.api.security.TenantFilter=DEBUG
```

### Index Not Used

**Symptom**: Slow queries, full table scans

**Solution**:

```sql
-- Check if index exists
SELECT * FROM pg_indexes
WHERE tablename = 'eco_product'
  AND indexname LIKE '%tenant%';

-- Create index if missing
CREATE INDEX idx_product_tenant_id ON eco_product(tenant_id);

-- Analyze table
ANALYZE eco_product;
```

## Continuous Integration

### Add to CI Pipeline

```yaml
# .github/workflows/test.yml
- name: Test Multi-Tenancy RLS
  run: |
    ./mvnw test -Dtest=MultiTenancyIntegrationTest
    ./test-multitenancy.sh
```

### Docker Compose Testing

```yaml
# docker-compose.test.yml
services:
  db:
    image: postgres:16
    environment:
      POSTGRES_PASSWORD: test_password

  api:
    build: .
    depends_on:
      - db
    command: ./mvnw test -Dtest=MultiTenancyIntegrationTest
```

## Performance Benchmarks

### Expected Query Times

With proper indexes:

- Single product by tenant_id: < 1ms
- List all products for tenant: < 10ms (< 1000 products)
- Complex joins with RLS: < 50ms

### Load Testing

```bash
# Test with multiple concurrent tenants
ab -n 1000 -c 10 \
   -H "Authorization: Bearer TENANT_A_JWT" \
   http://localhost:8080/api/products
```

## Security Checklist

Before deploying to production:

- [ ] RLS enabled on all multi-tenant tables
- [ ] Policies tested for SELECT, INSERT, UPDATE, DELETE
- [ ] Indexes on all tenant_id columns
- [ ] TenantFilter registered in SecurityConfiguration
- [ ] JWT tokens include tenant_id claim
- [ ] Cross-tenant access returns 403 or empty results
- [ ] Thread safety verified with concurrent requests
- [ ] Query performance meets SLA requirements
- [ ] Bypass policies limited to database owner only
- [ ] Audit logging captures tenant context

## Troubleshooting Reference

| Issue              | Diagnostic Command                                                 | Solution                       |
| ------------------ | ------------------------------------------------------------------ | ------------------------------ |
| RLS not working    | `SELECT rowsecurity FROM pg_tables WHERE tablename='eco_product';` | Run Liquibase migration        |
| No policies        | `SELECT * FROM pg_policies WHERE tablename='eco_product';`         | Apply 20260216000001 changelog |
| Slow queries       | `EXPLAIN ANALYZE SELECT * FROM eco_product WHERE tenant_id='X';`   | Create missing indexes         |
| Filter not running | Check application logs for TenantFilter                            | Verify SecurityConfiguration   |
| Wrong tenant data  | `SHOW app.current_tenant;`                                         | Check JWT claim extraction     |

## Additional Resources

- [MULTI_TENANCY.md](MULTI_TENANCY.md) - Full implementation guide
- [PostgreSQL RLS Documentation](https://www.postgresql.org/docs/current/ddl-rowsecurity.html)
- [Spring Security Filter Chain](https://docs.spring.io/spring-security/reference/servlet/architecture.html)

## Support

For issues or questions:

1. Check logs: `tail -f logs/spring.log`
2. Run diagnostics: `./test-multitenancy.sh`
3. Review policies: `psql -f verify-rls.sql`
