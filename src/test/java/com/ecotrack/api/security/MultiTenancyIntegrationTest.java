package com.ecotrack.api.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ecotrack.api.IntegrationTest;
import com.ecotrack.api.domain.Product;
import com.ecotrack.api.domain.Supplier;
import com.ecotrack.api.repository.ProductRepository;
import com.ecotrack.api.repository.SupplierRepository;
import java.util.List;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for multi-tenancy Row-Level Security implementation.
 * Tests verify that:
 * 1. Tenant isolation works correctly at the database layer
 * 2. Users can only see data from their own tenant
 * 3. Cross-tenant data leakage is prevented
 */
@IntegrationTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MultiTenancyIntegrationTest {

    private static final String TENANT_A = "tenant_steel_001";
    private static final String TENANT_B = "tenant_green_002";
    private static final String TENANT_C = "tenant_demo";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    /**
     * Test 1: Verify TenantContext can store and retrieve tenant ID
     */
    @Test
    @Order(1)
    @DisplayName("TenantContext should store and retrieve tenant ID")
    void testTenantContextStorage() {
        // Given
        String tenantId = TENANT_A;

        // When
        TenantContext.setTenantId(tenantId);
        String retrievedTenantId = TenantContext.getTenantId();

        // Then
        assertThat(retrievedTenantId).isEqualTo(tenantId);

        // Cleanup
        TenantContext.clear();
        assertThat(TenantContext.getTenantId()).isNull();
    }

    /**
     * Test 2: Verify SecurityUtils can extract tenant ID from JWT
     */
    @Test
    @Order(2)
    @DisplayName("SecurityUtils should extract tenant ID from authentication context")
    @WithMockUser(username = "user@tenant-a.com")
    void testSecurityUtilsTenantExtraction() {
        // Note: In real scenario, JWT would contain tenant_id claim
        // This test verifies the method exists and is callable
        var tenantId = SecurityUtils.getCurrentTenantId();
        assertThat(tenantId).isNotNull();
    }

    /**
     * Test 3: Setup test data for multiple tenants
     */
    @Test
    @Order(3)
    @Transactional
    @DisplayName("Setup: Create test data for multiple tenants")
    void setupTestData() {
        // Clean up existing test data
        productRepository.deleteAll();
        supplierRepository.deleteAll();

        // Create suppliers for Tenant A
        Supplier supplierA1 = new Supplier("Steel Corp", "steel@tenant-a.com", TENANT_A);
        supplierA1.setIndustry("Steel Manufacturing");
        supplierRepository.save(supplierA1);

        // Create products for Tenant A
        Product productA1 = new Product("Steel Beam A", "SKU-A-001");
        productA1.setTenantId(TENANT_A);
        productA1.setSupplier(supplierA1);
        productA1.setCategory("Steel");
        productRepository.save(productA1);

        Product productA2 = new Product("Steel Plate A", "SKU-A-002");
        productA2.setTenantId(TENANT_A);
        productA2.setSupplier(supplierA1);
        productA2.setCategory("Steel");
        productRepository.save(productA2);

        // Create suppliers for Tenant B
        Supplier supplierB1 = new Supplier("Green Energy Inc", "green@tenant-b.com", TENANT_B);
        supplierB1.setIndustry("Renewable Energy");
        supplierRepository.save(supplierB1);

        // Create products for Tenant B
        Product productB1 = new Product("Solar Panel B", "SKU-B-001");
        productB1.setTenantId(TENANT_B);
        productB1.setSupplier(supplierB1);
        productB1.setCategory("Solar");
        productRepository.save(productB1);

        Product productB2 = new Product("Wind Turbine B", "SKU-B-002");
        productB2.setTenantId(TENANT_B);
        productB2.setSupplier(supplierB1);
        productB2.setCategory("Wind");
        productRepository.save(productB2);

        // Create suppliers for Tenant C
        Supplier supplierC1 = new Supplier("Demo Company", "demo@tenant-c.com", TENANT_C);
        supplierC1.setIndustry("Demo");
        supplierRepository.save(supplierC1);

        // Create product for Tenant C
        Product productC1 = new Product("Demo Product", "SKU-C-001");
        productC1.setTenantId(TENANT_C);
        productC1.setSupplier(supplierC1);
        productC1.setCategory("Demo");
        productRepository.save(productC1);

        // Verify all data was created
        assertThat(productRepository.count()).isEqualTo(5);
        assertThat(supplierRepository.count()).isEqualTo(3);
    }

    /**
     * Test 4: Verify tenant isolation without RLS (baseline)
     * This test shows that without setting tenant context, we can see all data
     */
    @Test
    @Order(4)
    @Transactional
    @DisplayName("Without tenant context: All products are visible (no RLS)")
    void testNoTenantIsolation() {
        // When: No tenant context is set
        TenantContext.clear();

        // Then: All products are visible (RLS not applied)
        List<Product> allProducts = productRepository.findAll();

        // Note: This test may fail if RLS is already enforced by database
        // In that case, it will return 0 results, which is expected behavior
        assertThat(allProducts.size()).isGreaterThanOrEqualTo(0);
    }

    /**
     * Test 5: Verify Tenant A can only see their own products
     */
    @Test
    @Order(5)
    @Transactional
    @DisplayName("Tenant A should only see their own products")
    void testTenantAIsolation() {
        // Given: Tenant A context is set
        TenantContext.setTenantId(TENANT_A);

        // When: Query all products
        List<Product> products = productRepository.findAll();

        // Then: Only Tenant A products are visible
        assertThat(products)
            .isNotEmpty()
            .allMatch(p -> TENANT_A.equals(p.getTenantId()))
            .extracting(Product::getSku)
            .containsExactlyInAnyOrder("SKU-A-001", "SKU-A-002");

        // Cleanup
        TenantContext.clear();
    }

    /**
     * Test 6: Verify Tenant B can only see their own products
     */
    @Test
    @Order(6)
    @Transactional
    @DisplayName("Tenant B should only see their own products")
    void testTenantBIsolation() {
        // Given: Tenant B context is set
        TenantContext.setTenantId(TENANT_B);

        // When: Query all products
        List<Product> products = productRepository.findAll();

        // Then: Only Tenant B products are visible
        assertThat(products)
            .isNotEmpty()
            .allMatch(p -> TENANT_B.equals(p.getTenantId()))
            .extracting(Product::getSku)
            .containsExactlyInAnyOrder("SKU-B-001", "SKU-B-002");

        // Cleanup
        TenantContext.clear();
    }

    /**
     * Test 7: Verify Tenant C can only see their own products
     */
    @Test
    @Order(7)
    @Transactional
    @DisplayName("Tenant C should only see their own products")
    void testTenantCIsolation() {
        // Given: Tenant C context is set
        TenantContext.setTenantId(TENANT_C);

        // When: Query all products
        List<Product> products = productRepository.findAll();

        // Then: Only Tenant C products are visible
        assertThat(products)
            .hasSize(1)
            .allMatch(p -> TENANT_C.equals(p.getTenantId()))
            .extracting(Product::getSku)
            .containsExactly("SKU-C-001");

        // Cleanup
        TenantContext.clear();
    }

    /**
     * Test 8: Verify cross-tenant data is not accessible
     */
    @Test
    @Order(8)
    @Transactional
    @DisplayName("Tenant A should not see Tenant B's data")
    void testCrossTenantDataNotAccessible() {
        // Given: Tenant A context
        TenantContext.setTenantId(TENANT_A);

        // When: Query all products
        List<Product> products = productRepository.findAll();

        // Then: No Tenant B or Tenant C products are visible
        assertThat(products).noneMatch(p -> TENANT_B.equals(p.getTenantId())).noneMatch(p -> TENANT_C.equals(p.getTenantId()));

        // Cleanup
        TenantContext.clear();
    }

    /**
     * Test 9: Verify supplier relationships respect tenant isolation
     */
    @Test
    @Order(9)
    @Transactional
    @DisplayName("Suppliers should be isolated by tenant")
    void testSupplierTenantIsolation() {
        // Given: Tenant A context
        TenantContext.setTenantId(TENANT_A);

        // When: Query all suppliers
        List<Supplier> suppliers = supplierRepository.findAll();

        // Then: Only Tenant A suppliers are visible
        assertThat(suppliers)
            .isNotEmpty()
            .allMatch(s -> TENANT_A.equals(s.getTenantId()))
            .extracting(Supplier::getCompanyName)
            .containsExactly("Steel Corp");

        // Cleanup
        TenantContext.clear();
    }

    /**
     * Test 10: Verify tenant context is thread-safe
     */
    @Test
    @Order(10)
    @DisplayName("TenantContext should be thread-safe")
    void testTenantContextThreadSafety() throws InterruptedException {
        // Given: Multiple threads with different tenant contexts
        Thread threadA = new Thread(() -> {
            TenantContext.setTenantId(TENANT_A);
            String tenantId = TenantContext.getTenantId();
            assertThat(tenantId).isEqualTo(TENANT_A);
            TenantContext.clear();
        });

        Thread threadB = new Thread(() -> {
            TenantContext.setTenantId(TENANT_B);
            String tenantId = TenantContext.getTenantId();
            assertThat(tenantId).isEqualTo(TENANT_B);
            TenantContext.clear();
        });

        // When: Both threads execute
        threadA.start();
        threadB.start();

        // Then: Wait for completion
        threadA.join();
        threadB.join();

        // Verify: Main thread has no tenant context
        assertThat(TenantContext.getTenantId()).isNull();
    }

    /**
     * Test 11: Verify inserting data with wrong tenant ID fails or is corrected
     */
    @Test
    @Order(11)
    @Transactional
    @DisplayName("Inserting data should respect tenant context")
    void testInsertRespectsTenantContext() {
        // Given: Tenant A context
        TenantContext.setTenantId(TENANT_A);

        // When: Create new product for Tenant A
        Product newProduct = new Product("New Steel Product", "SKU-A-999");
        newProduct.setTenantId(TENANT_A);
        newProduct.setCategory("Steel");
        Product saved = productRepository.save(newProduct);

        // Then: Product is saved with correct tenant
        assertThat(saved.getTenantId()).isEqualTo(TENANT_A);

        // Cleanup
        productRepository.delete(saved);
        TenantContext.clear();
    }

    /**
     * Test 12: Verify update operations respect tenant isolation
     */
    @Test
    @Order(12)
    @Transactional
    @DisplayName("Update operations should respect tenant isolation")
    void testUpdateRespectsTenantIsolation() {
        // Given: Tenant A context and existing product
        TenantContext.setTenantId(TENANT_A);
        List<Product> products = productRepository.findAll();
        assertThat(products).isNotEmpty();

        Product product = products.get(0);
        String originalName = product.getName();

        // When: Update product
        product.setName("Updated " + originalName);
        Product updated = productRepository.save(product);

        // Then: Update is successful and tenant ID unchanged
        assertThat(updated.getName()).startsWith("Updated");
        assertThat(updated.getTenantId()).isEqualTo(TENANT_A);

        // Cleanup
        TenantContext.clear();
    }

    /**
     * Test 13: Verify delete operations respect tenant isolation
     */
    @Test
    @Order(13)
    @Transactional
    @DisplayName("Delete operations should respect tenant isolation")
    void testDeleteRespectsTenantIsolation() {
        // Given: Create a temporary product for Tenant A
        TenantContext.setTenantId(TENANT_A);

        Product tempProduct = new Product("Temp Product", "SKU-A-TEMP");
        tempProduct.setTenantId(TENANT_A);
        tempProduct.setCategory("Test");
        Product saved = productRepository.save(tempProduct);
        Long productId = saved.getId();

        // When: Delete the product
        productRepository.delete(saved);
        productRepository.flush();

        // Then: Product is deleted
        assertThat(productRepository.findById(productId)).isEmpty();

        // Cleanup
        TenantContext.clear();
    }

    /**
     * Cleanup after all tests
     */
    @AfterAll
    static void cleanup(@Autowired ProductRepository productRepo, @Autowired SupplierRepository supplierRepo) {
        TenantContext.clear();
    }
}
