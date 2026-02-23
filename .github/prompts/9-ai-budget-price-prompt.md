Context
You are an expert Senior Full-Stack Engineer working on EcoTrack Pro v2026.1.
Your tech stack includes:
Backend: Spring Boot 3.4, Java 21
Database: MongoDB
Database Migrations: Mongock
Frontend: Angular 19 (Standalone Components, Signals)
Caching: Spring Cache (Ehcache)
Task
Your objective is to implement the AiBudgetPrice module end-to-end based on the Technical Specification below. This feature transitions our system from hardcoded AI unit economics into a dynamic, MongoDB-backed configuration managed by the system administrators.
Please execute the implementation step-by-step. Do not skip any layers (Database, Backend, Frontend, Scheduled Jobs). Ensure you follow Java 21 and Angular 19 best practices.
Technical Specification

1. Data & Persistence Layer (MongoDB / Mongock)
   Step 1.1: Document Definition
   Create the AiBudgetPrice document entity in Spring Data MongoDB.
   Fields:
   id: String (mapped to MongoDB ObjectId primary key)
   serviceName: String, @Indexed(unique = true) (e.g., "Strategy Generation (Pro)")
   costPerUnit: BigDecimal, annotated with @Field(targetType = FieldType.DECIMAL128) for financial precision
   unitMeasure: String (e.g., "Per Strategy", "Per 1k Tokens")
   status: String Enum (ACTIVE, DEPRECATED)
   createdAt / updatedAt: Instant (using @CreatedDate and @LastModifiedDate for auditing)
   Step 1.2: Database Migrations (Mongock)
   Create a Mongock ChangeLog class: config/dbmigrations/AiBudgetPriceChangeLog.java.
   Write an @ChangeSet method to populate these initial baseline prices:
   Strategy Generation (Pro): $2.00, Per Strategy, ACTIVE
   Predictive Simulation: $0.50, Per Run, ACTIVE
   EcoAssistant Access: $500.00, Per Month / Tenant, ACTIVE
2. Backend Implementation (Spring Boot)
   Step 2.1: Caching Layer
   Create AiBudgetPriceRepository and AiBudgetPriceService.
   Annotate AiBudgetPriceService.findAllActive() with @Cacheable("ai_prices").
   Ensure create(), update(), and delete() methods trigger @CacheEvict(value = "ai_prices", allEntries = true).
   Step 2.2: Refactoring GeminiAnalysisService.java
   Inject AiBudgetPriceService into the existing GeminiAnalysisService.
   Replace hardcoded constant tokens (COST_PER_1K_TOKENS_FLASH) in calculateCost(). Fetch the dynamic active price from the cache instead.
   Update the micrometer counter usage to reflect the dynamic billing unit: meterRegistry.counter("ai.revenue.accrued", "tenant_id", tenantId).increment(dynamicPrice.doubleValue());
   Step 2.3: Admin REST API
   Create AiBudgetPriceResource.java to expose standard CRUD endpoints under /api/admin/ai-pricing.
   Secure all endpoints with @PreAuthorize("hasRole('ADMIN')").
3. Frontend Implementation (Angular 19)
   Step 3.1: State Management
   Create AiBudgetService.ts to handle HttpClient calls to /api/admin/ai-pricing.
   In admin_app.ts (AdminService), replace the mocked aiBudgetPrices signal array. Use toSignal(aiBudgetService.getAll()) or a manual subscription that populates a WritableSignal.
   Step 3.2: UI Integration
   Implement the saveAiPrice() method in admin_app.ts to execute HTTP POST/PUT via the new AiBudgetService.
   Implement Optimistic UI updates for the save action. If the HTTP request fails, revert the Signal state and show a Material SnackBar error.
   Ensure the Currency Pipe used in the template (currency:'USD':'symbol':'1.2-4') supports up to 4 decimal places for micro-transaction accuracy.
4. Scheduled Jobs
   Step 4.1: Java Billing Utility Refactor
   Create a new Spring Boot @Scheduled component named AutoInvoiceGeneratorJob.java (e.g., cron job running on the 1st of every month).
   Inject AiBudgetPriceService to fetch real-time billing values directly from MongoDB/Cache.
   Replicate the logic to calculate month-end statements based on Prometheus metrics (mock the metric retrieval for now, but focus on the dynamic price calculation).
   Output Instructions
   Please generate the code for these steps. You can output the code in distinct file blocks. Start with the MongoDB Entity, Mongock migration, and Spring Boot Service layer.
