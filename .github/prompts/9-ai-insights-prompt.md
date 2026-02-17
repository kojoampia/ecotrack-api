Here is a detailed **feature specification prompt** designed to generate the `GeminiAnalysisService.java` backend service. This prompt captures the enterprise-grade requirements for resilience, observability, and financial guardrails found in the EcoTrack Api architecture.

---

### **Backend Feature AI Insights: Spring AI Analysis Service**

**Context:**
You are a Senior Java Engineer building the **EcoTrack Api v2026** backend. You need to implement a robust, multi-tenant AI service that generates carbon reduction strategies using **Spring AI**.

**Class Name:** `GeminiAnalysisService`
**Package:** `com.ecotrack.api.service`

**Technical Stack Requirements:**

1. **Framework:** Spring Boot 3.4+ with **Spring AI** (`ChatClient` API).
2. **Resilience:** **Resilience4j** for `@CircuitBreaker` and `@Retry` patterns.
3. **State/Cache:** **Ehcache** (`CacheManager`) for local/clustered rate limiting and budget tracking.
4. **Observability:** **OpenTelemetry** (`Span`, `@WithSpan`) and **Micrometer** (`MeterRegistry`) for tracing and metrics.

**Functional Requirements:**

1. **Constructor & Configuration:**

- Inject `ChatClient.Builder` to build the Spring AI client.
- Inject `MeterRegistry` and `CacheManager`.
- Load configuration properties:
- `ai.model.name` (Default: "gemini-2.5-flash...")
- `app.compliance.max-daily-budget` (e.g., $50.00)
- `app.compliance.ai-rate-limit-rpm` (e.g., 5 requests per minute)

2. **Method: `generateInsights(String tenantId, String ledgerContext)**`

- **Annotation:** Apply `@Retry(name = "aiRetry")`, `@CircuitBreaker(name = "aiService")`, and `@WithSpan`.
- **Execution Flow:**

1. **OTel Context:** Inject `tenant.id` and `ai.model` attributes into the current Trace Span.
2. **Guardrails (Ehcache):**

- **Rate Limit:** Implement a sliding window check using Ehcache atomic increments (Key: `ecotrack:ratelimit:{minute}:{tenantId}`). If limit exceeded, throw `RATE_LIMIT_EXCEEDED` and increment a specific Micrometer counter.
- **Budget Check:** Check the accumulated daily spend in Ehcache (Key: `ecotrack:budget:{date}:{tenantId}`). If > `maxDailyBudget`, throw `CARBON_CREDIT_EXHAUSTED` and increment a counter.

3. **AI Inference:**

- Construct a prompt: _"Based on these emission records: {ledgerContext}. Generate 3 strategic reduction priorities..."_
- Call the LLM using `chatClient.prompt().user(prompt).call().content()`.

4. **Post-Apicessing:**

- Calculate estimated token usage and cost (Assume Flash = $0.0001/1k tokens, Api = $0.00125/1k tokens).
- **Atomic Update:** Increment the daily budget key in Redis with the calculated cost.
- **Metrics:** Record `ai.tokens.consumed` and `ai.cost.estimated` in Micrometer, tagged by `tenant_id` and `model`.
- Add `ai.tokens_used` and `ai.cost_usd` to the OTel Span.

- **Return:** The string response from the AI.

3. **Method: `fallbackAnalysis**`

- Implement a fallback for the Circuit Breaker.
- Logic:
- If exception contains "CARBON_CREDIT_EXHAUSTED", return a user-friendly "System Alert: Daily Budget Exhausted".
- If exception contains "RATE_LIMIT_EXCEEDED", return "System Alert: Request velocity too high".
- Otherwise, return a generic "Service Unavailable" message to prevent cascading UI failures.

**Code Style:**

- Use strict typing and clear variable naming.
- Ensure Redis keys have proper TTLs (e.g., 1 minute for rate limits, 24 hours for budget).
- Handle exceptions gracefully within the trace context (record exception to Span).
