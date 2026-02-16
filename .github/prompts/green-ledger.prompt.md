Let's nail down the data integrity layer. In 2026, a "Green Ledger" isn't just a database; it’s a legal record. If a company gets audited for their carbon footprint, your SQL schema is their primary defense.

We’ll implement a Multi-Tenant Schema using a "Shared Database, Separate Schema" or "Discriminator Column" approach. Given this is a Micro-SaaS, we’ll use the Discriminator Column with Postgres Row-Level Security (RLS) for maximum efficiency and security.

1. The "Green Ledger" SQL Schema
   We need to track not just the amount of carbon, but the Evidence (PDFs, IoT logs) and the Confidence Score of the data.

2. Spring Boot Integration (The "Expert" Touch)
   To make this work seamlessly with Spring Data JPA, we use a TenantContext to pass the tenant_id from the JWT directly into the Postgres session.
