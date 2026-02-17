-- Manual RLS Verification Script
-- Run this in psql to manually verify Row-Level Security implementation

\echo '==================================='
\echo 'Multi-Tenancy RLS Verification'
\echo '==================================='
\echo ''

-- 1. Check which tables have RLS enabled
\echo '--- 1. Tables with RLS Enabled ---'
SELECT 
    schemaname,
    tablename,
    rowsecurity AS rls_enabled
FROM pg_tables 
WHERE schemaname = 'public' 
    AND tablename IN (
        'eco_product', 
        'eco_supplier', 
        'eco_importer', 
        'installation', 
        'emission_record', 
        'emission_report', 
        'compliance_report'
    )
ORDER BY tablename;

\echo ''

-- 2. List all RLS policies
\echo '--- 2. RLS Policies ---'
SELECT 
    schemaname,
    tablename,
    policyname,
    permissive,
    roles,
    cmd,
    qual
FROM pg_policies 
WHERE schemaname = 'public' 
    AND policyname LIKE 'tenant_%'
ORDER BY tablename, policyname;

\echo ''

-- 3. Check tenant_id columns and indexes
\echo '--- 3. Tenant ID Columns and Indexes ---'
SELECT 
    t.table_name,
    c.column_name,
    c.data_type,
    c.character_maximum_length,
    c.is_nullable,
    (SELECT COUNT(*) 
     FROM pg_indexes i 
     WHERE i.tablename = t.table_name 
       AND i.indexdef LIKE '%tenant_id%') as index_count
FROM information_schema.tables t
JOIN information_schema.columns c 
    ON t.table_name = c.table_name
WHERE t.table_schema = 'public'
    AND c.column_name = 'tenant_id'
    AND t.table_type = 'BASE TABLE'
ORDER BY t.table_name;

\echo ''

-- 4. Check indexes on tenant_id
\echo '--- 4. Indexes on tenant_id ---'
SELECT 
    schemaname,
    tablename,
    indexname,
    indexdef
FROM pg_indexes 
WHERE schemaname = 'public' 
    AND indexdef LIKE '%tenant_id%'
ORDER BY tablename;

\echo ''

-- 5. Sample data distribution by tenant
\echo '--- 5. Data Distribution by Tenant ---'

-- Products by tenant
\echo 'Products by tenant:'
SELECT 
    COALESCE(tenant_id, 'NULL') as tenant_id,
    COUNT(*) as product_count
FROM eco_product
GROUP BY tenant_id
ORDER BY tenant_id;

\echo ''

-- Suppliers by tenant
\echo 'Suppliers by tenant:'
SELECT 
    COALESCE(tenant_id, 'NULL') as tenant_id,
    COUNT(*) as supplier_count
FROM eco_supplier
GROUP BY tenant_id
ORDER BY tenant_id;

\echo ''

-- 6. Test tenant isolation with session variables
\echo '--- 6. Testing Tenant Isolation ---'

-- Insert test data if not exists
INSERT INTO eco_supplier (id, company_name, contact_email, tenant_id, created_date, last_modified_date) 
VALUES 
    (999901, 'RLS Test Supplier A', 'rls-test-a@example.com', 'tenant_test_a', NOW(), NOW()),
    (999902, 'RLS Test Supplier B', 'rls-test-b@example.com', 'tenant_test_b', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO eco_product (id, name, sku, tenant_id, supplier_id, created_date, last_modified_date) 
VALUES 
    (999901, 'RLS Test Product A1', 'RLS-TEST-A-001', 'tenant_test_a', 999901, NOW(), NOW()),
    (999902, 'RLS Test Product A2', 'RLS-TEST-A-002', 'tenant_test_a', 999901, NOW(), NOW()),
    (999903, 'RLS Test Product B1', 'RLS-TEST-B-001', 'tenant_test_b', 999902, NOW(), NOW()),
    (999904, 'RLS Test Product B2', 'RLS-TEST-B-002', 'tenant_test_b', 999902, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

\echo 'Test data inserted.'
\echo ''

-- Test Tenant A isolation
\echo 'Setting tenant context to tenant_test_a...'
BEGIN;
SET LOCAL app.current_tenant = 'tenant_test_a';
\echo 'Products visible to tenant_test_a:'
SELECT id, name, sku, tenant_id 
FROM eco_product 
WHERE sku LIKE 'RLS-TEST-%'
ORDER BY sku;
COMMIT;

\echo ''

-- Test Tenant B isolation
\echo 'Setting tenant context to tenant_test_b...'
BEGIN;
SET LOCAL app.current_tenant = 'tenant_test_b';
\echo 'Products visible to tenant_test_b:'
SELECT id, name, sku, tenant_id 
FROM eco_product 
WHERE sku LIKE 'RLS-TEST-%'
ORDER BY sku;
COMMIT;

\echo ''

-- Test without tenant context (should see all or none depending on RLS config)
\echo 'Without tenant context (should see all with bypass policy or none):'
SELECT id, name, sku, tenant_id 
FROM eco_product 
WHERE sku LIKE 'RLS-TEST-%'
ORDER BY sku;

\echo ''

-- 7. Check query execution plans
\echo '--- 7. Query Execution Plans (check for index usage) ---'
EXPLAIN (ANALYZE, BUFFERS) 
SELECT * FROM eco_product 
WHERE tenant_id = 'tenant_test_a';

\echo ''

-- 8. Verify RLS cannot be bypassed
\echo '--- 8. Security Test: Attempting Cross-Tenant Access ---'
BEGIN;
SET LOCAL app.current_tenant = 'tenant_test_a';
\echo 'Tenant A trying to access Tenant B data by ID:'
SELECT COUNT(*) as accessible_products
FROM eco_product 
WHERE id IN (999903, 999904);  -- These belong to tenant_test_b
\echo 'Expected: 0 (RLS should block access)'
COMMIT;

\echo ''

-- 9. Cleanup test data
\echo '--- 9. Cleanup Test Data ---'
DELETE FROM eco_product WHERE sku LIKE 'RLS-TEST-%';
DELETE FROM eco_supplier WHERE company_name LIKE 'RLS Test %';
\echo 'Test data cleaned up.'

\echo ''
\echo '==================================='
\echo 'Verification Complete!'
\echo '==================================='
\echo ''
\echo 'If RLS is working correctly, you should see:'
\echo '  - RLS enabled on all multi-tenant tables'
\echo '  - Policies exist for tenant isolation'
\echo '  - Tenant A only sees their 2 products'
\echo '  - Tenant B only sees their 2 products'  
\echo '  - Cross-tenant access returns 0 results'
\echo ''
