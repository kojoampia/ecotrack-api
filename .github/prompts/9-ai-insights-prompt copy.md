Context:
You are an expert Senior Full-Stack Engineer working on EcoTrack Pro v2026.1. backend.
Your tech stack includes:
Backend: Spring Boot 3.4, Java 21, Spring AI
AI Provider: Google Gemini 2.5 Flash Preview
Resilience: Resilience4j
Frontend: Angular 19 (Standalone Components, Signals)
Caching: Spring Cache (CaffeineCache)
Observability: OpenTelemetry (OTel), Micrometer
Task:
Your objective is to implement the AI Neural Insights Engine end-to-end based on the Technical Specification below. This feature acts as a compliance intelligence bridge, using Gemini 2.5 Flash to generate localized carbon reduction strategies based on a tenant's "Green Ledger" data.
Please execute the implementation step-by-step. Do not skip any layers (Backend, Resilience, Observability, Frontend, Alerting). Ensure you follow Java 21 and Angular 19 best practices.
Technical Specification

1. Backend Implementation (GeminiAnalysisService.java)
   Step 1.1: Inference Engine Setup
   Create GeminiAnalysisService.java and inject the native Spring AI ChatClient.
   Implement the main method: public String generateInsights(String tenantId, String ledgerContext).
   Prompt Construction:
   System/Instruction: "You are an expert carbon compliance consultant in 2026."
   User Prompt: "Based on these emission records: [ledgerContext]. Generate 3 strategic reduction priorities in concise HTML bullet points (<li>). Tone: Clinical, Expert."
   Step 1.2: Resilience & Fallbacks
   Annotate generateInsights with @Retry(name = "geminiRetry").
   Annotate generateInsights with @CircuitBreaker(name = "geminiService", fallbackMethod = "fallbackAnalysis").
   Implement fallbackAnalysis(String tenantId, String ledgerContext, Throwable t):
   If t contains "CARBON_CREDIT_EXHAUSTED", return a specific HTML alert about upgrading tiers.
   If t contains "RATE_LIMIT_EXCEEDED", return a specific HTML alert about retrying in 60 seconds.
   Otherwise, return generic cached/offline compliance advice.
   Step 1.3: Financial Guardrails (Cost Control via Cache)
   Implement these pre-flight checks inside generateInsights before calling the AI:
   checkRateLimit(tenantId): Ensure requests < 5 per minute. If exceeded, throw an exception and increment the micrometer counter ai.rate_limit.exceeded.
   checkBudget(tenantId): Check accrued daily AI cost against a $50.00 maximum. If exceeded, throw an exception and increment ai.budget.exceeded.
   updateGlobalSpend(tenantId, estimatedCost): Calculate the cost based on token usage (input + output) and add it to the tenant's daily budget cache.
2. Observability & Telemetry
   Step 2.1: OpenTelemetry Tracing
   Annotate generateInsights with @WithSpan("gemini_analysis_execution").
   Inject Span.current() and add custom attributes: tenant.id, ai.model, ai.tokens_used, and ai.cost_usd.
   Step 2.2: Micrometer Metrics
   After a successful AI call, record the following using MeterRegistry:
   ai.tokens.consumed (counter)
   ai.cost.estimated (counter)
3. Alerting Configuration
   Step 3.1: Prometheus Alerts
   Generate the YAML configuration for prometheus-alerts.yml including these specific rules:
   AIServiceHighLatency: Alert if the p95 response time for ai_request_duration_seconds_bucket exceeds 5 seconds.
   GlobalAIServiceDown: Alert if probe_success{job="blackbox_ai"} equals 0.
   CarbonBudgetBreach: Alert if ai_budget_exceeded_total increases by > 0 in a 15m window.
   Output Instructions
   Please generate the code for these steps. Present the output in distinct code blocks for:
   GeminiAnalysisService.java
   The Angular Component (TypeScript + inline HTML/CSS)
   prometheus-alerts.yml
