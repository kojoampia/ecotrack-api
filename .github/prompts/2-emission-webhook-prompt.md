The Backend: Secure Webhook Ingestion
Factories in 2026 use IoT sensors that push data via Webhooks. To handle this at scale, we create a secure, asynchronous endpoint in our Spring Boot microservice.

EmissionWebhookResource.java
We use an X-Hub-Signature (HMAC-SHA256) check to ensure the data is actually coming from the authorized factory sensor.

@RestController
@RequestMapping("/api/webhooks")
public class EmissionWebhookResource {

    private final EmissionRecordService emissionService;

    @PostMapping("/sensor-data")
    public ResponseEntity<Void> handleSensorData(
        @RequestBody EmissionDTO dto,
        @RequestHeader("X-Signature") String signature
    ) {
        // 1. Validate HMAC Signature (Security First!)
        if (!SecurityUtils.isValidSignature(dto, signature)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2. Process Asynchronously (Don't block the IoT device)
        CompletableFuture.runAsync(() -> {
            emissionService.saveFromSensor(dto);
        });

        return ResponseEntity.accepted().build();
    }

}
