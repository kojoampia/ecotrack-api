Let's nail down the data integrity layer. In 2026, a "Green Ledger" isn't just a database; it’s a legal record. If a company gets audited for their carbon footprint, your SQL schema is their primary defense.

We’ll implement a Multi-Tenant Schema using a "Shared Database, Separate Schema" or "Discriminator Column" approach. Given this is a Micro-SaaS, we’ll use the Discriminator Column with Postgres Row-Level Security (RLS) for maximum efficiency and security.

1. The "Green Ledger" SQL Schema
   We need to track not just the amount of carbon, but the Evidence (PDFs, IoT logs) and the Confidence Score of the data.

2. Spring Boot Integration (The "Expert" Touch)
   To make this work seamlessly with Spring Data JPA, we use a TenantContext to pass the tenant_id from the JWT directly into the Postgres session.
   This way, all queries automatically filter by tenant without needing to modify repository methods.

-- Core Ledger Table
CREATE TABLE carbon_ledger (
id UUID PRIMARY KEY,
tenant_id VARCHAR(50) NOT NULL, -- The Client ID
scope_type VARCHAR(10) NOT NULL, -- SCOPE_1, 2, or 3
activity_data DOUBLE PRECISION NOT NULL, -- e.g., 500.5
unit VARCHAR(10) NOT NULL, -- e.g., "kWh", "LITERS"
co2_equivalent_kg BIGINT NOT NULL,
evidence_url TEXT, -- Link to S3 bucket containing the invoice/log
confidence_score DECIMAL(3,2), -- 0.00 to 1.00
recorded_at TIMESTAMPTZ DEFAULT NOW(),
version INT DEFAULT 1 -- For Optimistic Locking
);

-- Enable Row Level Security
ALTER TABLE carbon_ledger ENABLE ROW LEVEL SECURITY;

-- Create a Policy: Users can only see data where tenant_id matches their session
CREATE POLICY tenant_isolation_policy ON carbon_ledger
USING (tenant_id = current_setting('app.current_tenant'));
