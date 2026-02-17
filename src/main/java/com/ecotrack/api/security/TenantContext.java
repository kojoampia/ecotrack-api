package com.ecotrack.api.security;

/**
 * Thread-local context for storing the current tenant ID.
 * This is used to propagate tenant information from JWT to database queries.
 */
public final class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new InheritableThreadLocal<>();

    private TenantContext() {
        // Utility class
    }

    /**
     * Set the current tenant ID for this thread.
     *
     * @param tenantId the tenant ID to set
     */
    public static void setTenantId(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    /**
     * Get the current tenant ID for this thread.
     *
     * @return the current tenant ID, or null if not set
     */
    public static String getTenantId() {
        return CURRENT_TENANT.get();
    }

    /**
     * Clear the current tenant ID from this thread.
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
