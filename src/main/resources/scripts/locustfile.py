import random
import time
from locust import HttpUser, task, between

class EcoTrackProLoadTester(HttpUser):
    """
    Simulates high-load scenarios for EcoTrack Pro to verify 
    OpenTelemetry tracing and Prometheus alerting.
    """
    # Wait between 0.5 and 2 seconds between tasks per user
    wait_time = between(0.5, 2)

    def on_start(self):
        """
        Setup: Authenticate or set tenant context headers.
        """
        self.tenants = ["tenant_steel_001", "tenant_green_002"]

    @task(3)
    def simulate_ai_analysis(self):
        """
        Hits the Gemini Analysis endpoint.
        Verifies: AIServiceHighLatency alert and ai.model tracing.
        """
        tenant = random.choice(self.tenants)
        payload = {
            "tenantId": tenant,
            "activityData": "Industrial smelting energy consumption: 50000kWh",
            "simulateLatency": random.choice([False, False, True]) # Randomly trigger slow paths if backend supports it
        }
        
        # This will show up in Tempo as 'gemini_analysis_execution'
        with self.client.post("/api/ai/analyze", json=payload, catch_response=True) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure(f"AI Service error: {response.status_code}")

    @task(1)
    def simulate_low_confidence_data(self):
        """
        Sends low-quality data to trigger confidence alerts.
        Verifies: CarbonConfidenceBelowThreshold alert.
        """
        tenant = self.tenants[0] # Focus on one tenant for the alert
        payload = {
            "tenantId": tenant,
            "source": "Manual Spreadsheet Upload",
            "confidenceFactor": 0.45 # Intentionally low
        }
        self.client.post("/api/emissions/record", json=payload)

    @task(5)
    def dashboard_view(self):
        """
        Simulates standard dashboard traffic.
        """
        tenant = random.choice(self.tenants)
        self.client.get(f"/api/emissions/summary?tenantId={tenant}")