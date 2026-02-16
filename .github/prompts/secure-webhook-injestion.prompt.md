The Backend: Secure Webhook Ingestion
Factories in 2026 use IoT sensors that push data via Webhooks. To handle this at scale, we create a secure, asynchronous endpoint in our Spring Boot microservice.

EmissionWebhookResource.java
We use an X-Hub-Signature (HMAC-SHA256) check to ensure the data is actually coming from the authorized factory sensor.
