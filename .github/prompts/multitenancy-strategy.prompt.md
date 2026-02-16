Multi-Tenancy Strategy
To scale to 1,000+ companies, we’ll use PostgreSQL Row-Level Security (RLS). This is the gold standard for 2026 SaaS because it prevents "data leakage" even if there's a bug in your Java code.

The SQL: Every table gets a tenant_id.

The Logic: A TenantFilter in Spring Boot extracts the tenant_id from the JWT and sets it in the Postgres session:
SET app.current_tenant = 'company_abc_123';

To make this work seamlessly with Spring Data JPA, we use a TenantContext to pass the tenant_id from the JWT directly into the Postgres session.
