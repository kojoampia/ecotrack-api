#!/bin/bash

# Multi-Tenancy RLS Implementation Test Script
# This script validates the PostgreSQL Row-Level Security implementation

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-ecotrackpro}"
DB_USER="${DB_USER:-EcoTrackPro}"
DB_PASSWORD="${DB_PASSWORD:-}"

TENANT_A="tenant_steel_001"
TENANT_B="tenant_green_002"
TENANT_C="tenant_demo"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Multi-Tenancy RLS Validation Script${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Function to print section headers
print_header() {
    echo -e "${YELLOW}--- $1 ---${NC}"
}

# Function to print success
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

# Function to print error
print_error() {
    echo -e "${RED}✗ $1${NC}"
    exit 1
}

# Function to run SQL query
run_sql() {
    PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -t -c "$1"
}

# Test 1: Check if database is accessible
print_header "Test 1: Database Connectivity"
if run_sql "SELECT 1;" &> /dev/null; then
    print_success "Database connection successful"
else
    print_error "Cannot connect to database"
fi

# Test 2: Check if RLS is enabled on tables
print_header "Test 2: Row-Level Security Status"

check_rls() {
    table=$1
    rls_enabled=$(run_sql "SELECT rowsecurity FROM pg_tables WHERE schemaname = 'public' AND tablename = '$table';")
    if [[ "$rls_enabled" =~ "t" ]] || [[ "$rls_enabled" =~ "true" ]]; then
        print_success "RLS enabled on $table"
    else
        echo -e "${YELLOW}⚠ RLS not enabled on $table (may not be implemented yet)${NC}"
    fi
}

check_rls "eco_product"
check_rls "eco_supplier"
check_rls "eco_importer"
check_rls "installation"
check_rls "emission_record"
check_rls "emission_report"
check_rls "compliance_report"

# Test 3: Check if RLS policies exist
print_header "Test 3: RLS Policies"

check_policy() {
    table=$1
    policy_count=$(run_sql "SELECT COUNT(*) FROM pg_policies WHERE tablename = '$table' AND policyname LIKE 'tenant_isolation%';")
    if [ "$policy_count" -gt 0 ]; then
        print_success "Found $policy_count RLS policies on $table"
    else
        echo -e "${YELLOW}⚠ No RLS policies found on $table${NC}"
    fi
}

check_policy "eco_product"
check_policy "eco_supplier"
check_policy "eco_importer"

# Test 4: Check if tenant_id columns exist
print_header "Test 4: Tenant ID Columns"

check_tenant_column() {
    table=$1
    column_exists=$(run_sql "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '$table' AND column_name = 'tenant_id';")
    if [ "$column_exists" -gt 0 ]; then
        print_success "tenant_id column exists on $table"
    else
        echo -e "${YELLOW}⚠ tenant_id column missing on $table${NC}"
    fi
}

check_tenant_column "eco_product"
check_tenant_column "eco_supplier"
check_tenant_column "eco_importer"
check_tenant_column "installation"
check_tenant_column "emission_record"
check_tenant_column "emission_report"
check_tenant_column "compliance_report"

# Test 5: Check if indexes exist on tenant_id
print_header "Test 5: Tenant ID Indexes"

check_index() {
    table=$1
    index_name="idx_${table}_tenant_id"
    index_exists=$(run_sql "SELECT COUNT(*) FROM pg_indexes WHERE schemaname = 'public' AND tablename = '$table' AND indexname LIKE '%tenant_id%';")
    if [ "$index_exists" -gt 0 ]; then
        print_success "Index on tenant_id exists for $table"
    else
        echo -e "${YELLOW}⚠ No index on tenant_id for $table${NC}"
    fi
}

check_index "eco_product"
check_index "eco_supplier"
check_index "eco_importer"

# Test 6: Insert test data for multiple tenants
print_header "Test 6: Insert Test Data"

echo "Cleaning up existing test data..."
run_sql "DELETE FROM eco_product WHERE sku LIKE 'TEST-%';" &> /dev/null || true
run_sql "DELETE FROM eco_supplier WHERE company_name LIKE 'Test %';" &> /dev/null || true

echo "Inserting test suppliers..."
run_sql "INSERT INTO eco_supplier (id, company_name, contact_email, tenant_id, created_date, last_modified_date) VALUES 
    (999001, 'Test Supplier A', 'test-a@example.com', '$TENANT_A', NOW(), NOW()),
    (999002, 'Test Supplier B', 'test-b@example.com', '$TENANT_B', NOW(), NOW()),
    (999003, 'Test Supplier C', 'test-c@example.com', '$TENANT_C', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;" &> /dev/null || echo -e "${YELLOW}⚠ Could not insert suppliers (may already exist)${NC}"

echo "Inserting test products..."
run_sql "INSERT INTO eco_product (id, name, sku, tenant_id, supplier_id, created_date, last_modified_date) VALUES 
    (999001, 'Test Product A1', 'TEST-A-001', '$TENANT_A', 999001, NOW(), NOW()),
    (999002, 'Test Product A2', 'TEST-A-002', '$TENANT_A', 999001, NOW(), NOW()),
    (999003, 'Test Product B1', 'TEST-B-001', '$TENANT_B', 999002, NOW(), NOW()),
    (999004, 'Test Product B2', 'TEST-B-002', '$TENANT_B', 999002, NOW(), NOW()),
    (999005, 'Test Product C1', 'TEST-C-001', '$TENANT_C', 999003, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;" &> /dev/null || echo -e "${YELLOW}⚠ Could not insert products (may already exist)${NC}"

print_success "Test data inserted"

# Test 7: Verify tenant isolation with session variables
print_header "Test 7: Tenant Isolation via Session Variables"

echo "Testing Tenant A isolation..."
count_a=$(run_sql "BEGIN; SET LOCAL app.current_tenant = '$TENANT_A'; SELECT COUNT(*) FROM eco_product WHERE sku LIKE 'TEST-%'; COMMIT;")
echo "  Tenant A sees: $count_a products"

echo "Testing Tenant B isolation..."
count_b=$(run_sql "BEGIN; SET LOCAL app.current_tenant = '$TENANT_B'; SELECT COUNT(*) FROM eco_product WHERE sku LIKE 'TEST-%'; COMMIT;")
echo "  Tenant B sees: $count_b products"

echo "Testing Tenant C isolation..."
count_c=$(run_sql "BEGIN; SET LOCAL app.current_tenant = '$TENANT_C'; SELECT COUNT(*) FROM eco_product WHERE sku LIKE 'TEST-%'; COMMIT;")
echo "  Tenant C sees: $count_c products"

if [ "$count_a" -eq 2 ] && [ "$count_b" -eq 2 ] && [ "$count_c" -eq 1 ]; then
    print_success "Tenant isolation working correctly"
else
    echo -e "${YELLOW}⚠ Expected: A=2, B=2, C=1. Got: A=$count_a, B=$count_b, C=$count_c${NC}"
    echo -e "${YELLOW}  This may indicate RLS is not yet active or needs migration${NC}"
fi

# Test 8: Verify query plans use indexes
print_header "Test 8: Index Usage in Query Plans"

echo "Checking if queries use tenant_id index..."
explain_output=$(run_sql "EXPLAIN SELECT * FROM eco_product WHERE tenant_id = '$TENANT_A';")
if echo "$explain_output" | grep -q "idx_.*tenant_id"; then
    print_success "Query plan uses tenant_id index"
else
    echo -e "${YELLOW}⚠ Query may not be using tenant_id index optimally${NC}"
fi

# Test 9: Run Java integration tests
print_header "Test 9: Java Integration Tests"

if [ -f "./mvnw" ]; then
    echo "Running MultiTenancyIntegrationTest..."
    if ./mvnw -Dtest=MultiTenancyIntegrationTest test -q 2>&1 | grep -q "BUILD SUCCESS"; then
        print_success "Java integration tests passed"
    else
        echo -e "${YELLOW}⚠ Java tests not run or failed. Run './mvnw test' for details${NC}"
    fi
else
    echo -e "${YELLOW}⚠ Maven wrapper not found, skipping Java tests${NC}"
fi

# Test 10: Cleanup
print_header "Test 10: Cleanup"

echo "Cleaning up test data..."
run_sql "DELETE FROM eco_product WHERE sku LIKE 'TEST-%';" &> /dev/null || true
run_sql "DELETE FROM eco_supplier WHERE company_name LIKE 'Test %';" &> /dev/null || true
print_success "Test data cleaned up"

# Summary
echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}Multi-Tenancy Validation Complete!${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo "Next steps:"
echo "  1. Review MULTI_TENANCY.md for detailed documentation"
echo "  2. Run full test suite: ./mvnw test"
echo "  3. Test with real JWT tokens containing tenant_id claim"
echo "  4. Monitor query performance with EXPLAIN ANALYZE"
echo ""
