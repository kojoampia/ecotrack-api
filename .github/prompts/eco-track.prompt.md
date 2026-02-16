---
agent: You're an expert software engineer and architect with mastery in java, springboot, microservices, jhipster, bash, sql, no-sql, typescript, angular, angular-material scss, bootstrap. You're friendly, casual and crisp.
---

The Concept: "EcoTrack App"
Specialized Carbon Compliance & Supply Chain Ledger
With global carbon taxes and "Green Passports" for products becoming mandatory in 2026, small-to-medium manufacturing firms are struggling to report their Scope 3 emissions. Most existing tools are either "too enterprise" (SAP/Oracle) or just simple spreadsheets.

The Tech Stack Strategy
Backend: JHipster-generated Spring Boot microservices. I’d use MongoDB for the flexible "Product Passport" schemas (NoSQL) and PostgreSQL for the core transactional and audit data (SQL).

Frontend: Angular 19+ with Material Design. I’d use high-density data tables and custom SCSS for a "Clean Tech" dark mode aesthetic.

Automation: Bash scripts for CI/CD pipelines and automated data ingestion from legacy factory CSV/IoT dumps.

Why this works in 2026
The Regulatory Moat: New laws require "Immutable Audit Trails." A Spring Boot backend with Spring Security and a robust SQL audit log is perfect for this.

The Integration Play: These factories use old systems. I can build "Connectors" (Microservices) that ingest data from legacy SQL databases via JDBC.

High LTV (Lifetime Value): Once a company puts their compliance data in your hands, they rarely switch.

Key Features
Automated Reporting: Generate "Audit-Ready" PDFs using JasperReports or a specialized microservice.

Supplier Portal: A lightweight Angular/Bootstrap portal where suppliers log their own data.

API-First: Allow other apps to query a product’s carbon footprint via a REST API.

How we’d build it fast
Using JHipster, we could have the entire identity management, JWT/OIDC security, and CRUD API for the core entities (Suppliers, Products, Emissions) running in less than a day. This lets us focus 90% of our energy on the specific "Carbon Calculation" logic and the Material dashboard.

"In 2026, the winner isn't who has the most AI—it's who has the most reliable data."

Sketch out a JHipster JDL (Domain Language) file to define the entities for this Micro-SaaS.
Generate all Entities, Repositories, Services, Controllers, Components Configurations and frontend webapp for this application.
