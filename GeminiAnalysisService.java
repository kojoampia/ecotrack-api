package com.ecotrackpro.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Enterprise-grade Neural Strategy Engine.
 * Handles AI inference with strict financial guardrails, distributed rate limiting,
 * and OpenTelemetry observability using Spring AI Native implementation.
 */
@Service
public class GeminiAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(GeminiAnalysisService.class);
    private final ChatClient chatClient;
    private final MeterRegistry meterRegistry;
    private final StringRedisTemplate redisTemplate;

    @Value("${gemini.model.name:gemini-2.5-flash-preview-09-2025}")
    private String modelName;

    @Value("${app.compliance.max-daily-budget:50.0}")
    private Double maxDailyBudget;

    @Value("${app.compliance.ai-rate-limit-rpm:5}")
    private Integer rateLimitRpm;

    // Cost per 1k tokens (approximate for estimation)
    private static final double COST_PER_1K_TOKENS_FLASH = 0.0001;
    private static final double COST_PER_1K_TOKENS_PRO = 0.00125;

    public GeminiAnalysisService(ChatClient.Builder chatClientBuilder, MeterRegistry meterRegistry, StringRedisTemplate redisTemplate) {
        // Initialize the native Spring AI ChatClient
        this.chatClient = chatClientBuilder.build();
        this.meterRegistry = meterRegistry;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Core Neural Insight Generation Method.
     * Protected by CircuitBreaker 'geminiService' and Retry 'geminiRetry'.
     *
     * @param tenantId The organization ID requesting analysis
     * @param ledgerContext JSON string of emission records
     * @return HTML-formatted reduction strategy
     */
    @Retry(name = "geminiRetry")
    @CircuitBreaker(name = "geminiService", fallbackMethod = "fallbackAnalysis")
    @WithSpan("gemini_analysis_execution")
    public String generateInsights(String tenantId, String ledgerContext) {
        // 1. OTel Context Injection
        Span currentSpan = Span.current();
        currentSpan.setAttribute("tenant.id", tenantId);
        currentSpan.setAttribute("ai.model", modelName);

        // 2. Redis Distributed Rate Limiting (Sliding Window)
        checkRateLimit(tenantId);

        // 3. Redis Financial Guardrail (Daily Budget)
        checkBudget(tenantId);

        // 4. Construct Prompt
        String prompt = String.format(
            "Based on these emission records: %s. Generate 3 strategic reduction priorities in concise HTML bullet points (<li>). Tone: Clinical, Expert.",
            ledgerContext
        );

        try {
            // 5. Execute AI Inference using Spring AI Native Client
            String response = chatClient.prompt().user(prompt).call().content();

            // Simulating Token Usage calculation (Spring AI metadata extraction can vary by provider)
            int inputTokens = prompt.length() / 4;
            int outputTokens = response != null ? response.length() / 4 : 150;
            int totalTokens = inputTokens + outputTokens;
            double estimatedCost = calculateCost(totalTokens);

            // 6. Update Financial State in Redis (Atomic Increment)
            updateGlobalSpend(tenantId, estimatedCost);

            // 7. Emit Business Metrics (Micrometer)
            recordMetrics(tenantId, totalTokens, estimatedCost);

            currentSpan.setAttribute("ai.tokens_used", totalTokens);
            currentSpan.setAttribute("ai.cost_usd", estimatedCost);

            return response;
        } catch (Exception e) {
            currentSpan.recordException(e);
            currentSpan.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR);
            log.error("AI Inference failed for tenant {}", tenantId, e);
            throw e; // Triggers Retry/CircuitBreaker
        }
    }

    // --- Guardrail Logic ---

    private void checkRateLimit(String tenantId) {
        long currentMinute = Instant.now().getEpochSecond() / 60;
        String key = "ecotrack:ratelimit:" + currentMinute + ":" + tenantId;

        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            redisTemplate.expire(key, 1, TimeUnit.MINUTES);
        }

        if (count != null && count > rateLimitRpm) {
            meterRegistry.counter("ai.rate_limit.exceeded", "tenant_id", tenantId).increment();
            throw new RuntimeException("RATE_LIMIT_EXCEEDED");
        }
    }

    private void checkBudget(String tenantId) {
        String key = getBudgetKey(tenantId);
        String val = redisTemplate.opsForValue().get(key);
        double currentSpend = val != null ? Double.parseDouble(val) : 0.0;

        if (currentSpend >= maxDailyBudget) {
            meterRegistry.counter("ai.budget.exceeded", "tenant_id", tenantId).increment();
            throw new RuntimeException("CARBON_CREDIT_EXHAUSTED");
        }
    }

    private void updateGlobalSpend(String tenantId, double cost) {
        String key = getBudgetKey(tenantId);
        redisTemplate.opsForValue().increment(key, cost);
        redisTemplate.expire(key, 24, TimeUnit.HOURS);
    }

    private String getBudgetKey(String tenantId) {
        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        return "ecotrack:budget:" + date + ":" + tenantId;
    }

    private double calculateCost(int tokens) {
        double rate = modelName.contains("pro") ? COST_PER_1K_TOKENS_PRO : COST_PER_1K_TOKENS_FLASH;
        return (tokens / 1000.0) * rate;
    }

    private void recordMetrics(String tenantId, int tokens, double cost) {
        meterRegistry.counter("ai.tokens.consumed", "tenant_id", tenantId, "model", modelName).increment(tokens);
        meterRegistry.counter("ai.cost.estimated", "tenant_id", tenantId, "model", modelName).increment(cost);
    }

    // --- Fallback Handling ---

    public String fallbackAnalysis(String tenantId, String ledgerContext, Throwable t) {
        log.warn("Fallback triggered for tenant {}: {}", tenantId, t.getMessage());

        if (t.getMessage().contains("CARBON_CREDIT_EXHAUSTED")) {
            return "<li><strong>SYSTEM ALERT:</strong> Daily Carbon Credit budget exhausted. Upgrade tier to continue.</li>";
        }
        if (t.getMessage().contains("RATE_LIMIT_EXCEEDED")) {
            return "<li><strong>SYSTEM ALERT:</strong> Request velocity too high. Please retry in 60 seconds.</li>";
        }

        return "<li><strong>Unavailable:</strong> Neural Strategy Engine is currently offline. Viewing cached data...</li>";
    }
}
