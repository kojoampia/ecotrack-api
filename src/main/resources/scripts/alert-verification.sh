#!/bin/bash

# EcoTrack Pro Alert Verification Script (v2026.2)
# Updated to include Rate Limiting and Scaled Budget verification.

echo "🎯 Starting Enhanced Alert Verification Suite..."

# 1. Trigger GlobalAIServiceDown (Infrastructure)
echo "🛑 Step 1: Simulating Global AI Outage (Blackbox Probe Failure)..."
docker-compose stop blackbox-exporter
echo "⏳ Waiting 65s for evaluation..."
sleep 65
curl -s http://localhost:9090/api/v1/alerts | grep -q "GlobalAIServiceDown" && echo "✅ ALERT ACTIVE: GlobalAIServiceDown" || echo "❌ ALERT FAILED: GlobalAIServiceDown"
docker-compose start blackbox-exporter

# 2. Trigger Global Carbon Budget Breach (Redis-Shared)
echo "💰 Step 2: Simulating Budget Exhaustion for tenant_steel_001..."
# We manually inject a large value into Redis. All scaled backend instances will now block this tenant.
DATE_KEY=$(date +%Y-%m-%d)
docker-compose exec redis redis-cli SET "ecotrack:budget:$DATE_KEY:tenant_steel_001" "55.0"
echo "⏳ Waiting for Prometheus/Alertmanager sync..."
sleep 35
curl -s http://localhost:9090/api/v1/alerts | grep -q "CarbonBudgetBreach" && echo "✅ ALERT ACTIVE: CarbonBudgetBreach" || echo "❌ ALERT FAILED: CarbonBudgetBreach"

# 3. Trigger Rate Limiting (Redis-Shared)
echo "⚡ Step 3: Triggering Rate Limiter (Rapid fire requests)..."
# We hit the endpoint 10 times in 2 seconds (Threshold is 5/min)
for i in {1..10}; do
  curl -s -X POST http://localhost:8080/api/ai/analyze -d '{"tenantId":"tenant_green_002"}' -H "Content-Type: application/json" > /dev/null &
done
echo "🔍 Check Loki Logs: '{service_name=~\"ecotrack-backend.*\"} |= \"RATE_LIMIT_EXCEEDED\"'"

# 4. Trigger High Load (Locust)
echo "🔥 Step 4: Spawning load to verify p95 tracing..."
locust -f locustfile.py --headless -u 20 -r 5 --run-time 1m --host http://localhost:8080

echo "🏁 Suite Complete. Review Grafana 'AI & Unit Economics' Dashboard for real-time telemetry."