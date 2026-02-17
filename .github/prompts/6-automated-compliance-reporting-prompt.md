## Phase 6: Automated Compliance Reporting

One of the biggest pain points for companies in 2026 is the **CBAM Declaration**. We’ll automate this by generating an XML/JSON payload that fits the EU’s **TARIC** system requirements.

### Bash Script: `generate-compliance-payload.sh`

This script aggregates data from the PostgreSQL database and prepares it for the authorized declarant portal.

```bash
#!/bin/bash
# EcoTrack Pro - Compliance Exporter v2026.1
TENANT_ID=$1
YEAR=$2

echo "📊 Aggregating Scope 1, 2, and 3 for Tenant: $TENANT_ID..."

# Use a Spring Boot custom endpoint to get the JSON
curl -X GET "http://localhost:8080/api/compliance/export?tenantId=$TENANT_ID&year=$YEAR" \
     -H "Authorization: Bearer $JWT_TOKEN" \
     -o "CBAM_Declaration_${TENANT_ID}_${YEAR}.json"

echo "✅ Declaration generated. Ready for submission to TARIC portal."

```

---

### Java Service: `CompliancePayloadService.java`

This service utilizes the generate-compliance-payload.sh script preparing for and passing in the relevant parameters such as bearer jwt_token, tenant_id and year

Java Controller: CompliancePayloadResource.java
This controller wraps the CompliancePayloadService with and api endpoint /api/compliance-payload that supports CRUD, Search and results pagination.
