package com.ecotrack.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter that extracts tenant_id from JWT and sets it in PostgreSQL session.
 * This enables Row-Level Security (RLS) policies to enforce multi-tenancy at the database layer.
 */
@Component
public class TenantFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TenantFilter.class);
    private static final String TENANT_CLAIM = "tenant_id";

    private final DataSource dataSource;

    public TenantFilter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String tenantId = extractTenantFromJwt();
            if (tenantId != null) {
                TenantContext.setTenantId(tenantId);
                setTenantInDatabase(tenantId);
                log.debug("Set tenant context to: {}", tenantId);
            } else if (isAuthenticatedRequest()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing tenant context");
                return;
            }
            filterChain.doFilter(request, response);
        } finally {
            // Always clear tenant context after request
            TenantContext.clear();
            clearTenantInDatabase();
        }
    }

    /**
     * Extract tenant_id from JWT claims.
     *
     * @return the tenant ID from JWT, or null if not present
     */
    private String extractTenantFromJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String tenantId = jwt.getClaimAsString(TENANT_CLAIM);
            if (tenantId != null && !tenantId.isEmpty()) {
                return tenantId;
            }
        }
        return null;
    }

    private boolean isAuthenticatedRequest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String);
    }

    /**
     * Set the tenant_id in the PostgreSQL session using SET LOCAL.
     * This enables RLS policies to filter data by tenant.
     *
     * @param tenantId the tenant ID to set
     */
    private void setTenantInDatabase(String tenantId) {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement("SELECT set_config('app.current_tenant', ?, true)")) {
            statement.setString(1, tenantId);
            statement.execute();
            log.trace("Set PostgreSQL session variable app.current_tenant = {}", tenantId);
        } catch (SQLException e) {
            log.error("Failed to set tenant in database session", e);
            // Don't fail the request, but log the error
            // In production, you might want to throw a custom exception here
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    /**
     * Clear the tenant_id from the PostgreSQL session.
     */
    private void clearTenantInDatabase() {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement statement = connection.prepareStatement("SELECT set_config('app.current_tenant', '', true)")) {
            statement.execute();
            log.trace("Cleared PostgreSQL session variable app.current_tenant");
        } catch (SQLException e) {
            log.trace("Failed to clear tenant from database session (this is normal at end of transaction)", e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }
}
