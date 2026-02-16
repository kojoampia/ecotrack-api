#!/bin/bash

# EcoTrack Pro - CBAM Compliance Payload Generator
# Generates XML/JSON payloads for EU TARIC system compliance reporting
# Usage: ./generate-compliance-payload.sh <tenant_id> <year> <jwt_token> [output_format]

set -e

# Configuration
TENANT_ID="$1"
REPORTING_YEAR="$2"
JWT_TOKEN="$3"
OUTPUT_FORMAT="${4:-json}"  # json or xml
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-ecotrack}"
DB_USER="${DB_USER:-ecotrack}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
OUTPUT_DIR="${SCRIPT_DIR}/../target/compliance-reports"
TEMP_DIR="${OUTPUT_DIR}/temp"

# Validate inputs
if [ -z "$TENANT_ID" ] || [ -z "$REPORTING_YEAR" ] || [ -z "$JWT_TOKEN" ]; then
    echo "Usage: $0 <tenant_id> <year> <jwt_token> [output_format]"
    echo "Example: $0 company123 2026 eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9... json"
    exit 1
fi

# Create output directories
mkdir -p "$OUTPUT_DIR" "$TEMP_DIR"

# Output file names
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
OUTPUT_FILE="${OUTPUT_DIR}/cbam_payload_${TENANT_ID}_${REPORTING_YEAR}_${TIMESTAMP}.${OUTPUT_FORMAT}"
TEMP_JSON="${TEMP_DIR}/data_${TIMESTAMP}.json"
TEMP_XML="${TEMP_DIR}/data_${TIMESTAMP}.xml"

echo "Generating CBAM compliance payload for tenant: $TENANT_ID, year: $REPORTING_YEAR"

# PostgreSQL connection and data extraction
# Note: In production, use proper connection pooling and prepared statements
PGPASSWORD="${DB_PASSWORD:-password}" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "
    -- Set session variable for RLS
    SET app.current_tenant = '$TENANT_ID';

    -- Export data to temporary JSON file
    COPY (
        SELECT json_build_object(
            'reportingPeriod', '$REPORTING_YEAR',
            'tenantId', '$TENANT_ID',
            'generatedAt', NOW(),
            'emissions', json_agg(
                json_build_object(
                    'id', er.id,
                    'scope', er.scope,
                    'carbonGrams', er.carbon_grams,
                    'dateRecorded', er.date_recorded,
                    'source', er.source,
                    'notes', er.notes,
                    'verified', er.verified,
                    'product', CASE
                        WHEN p.id IS NOT NULL THEN json_build_object(
                            'id', p.id,
                            'name', p.name,
                            'sku', p.sku,
                            'category', p.category
                        )
                        ELSE NULL
                    END
                )
            ),
            'summary', json_build_object(
                'totalEmissions', COALESCE(SUM(er.carbon_grams), 0),
                'scope1Total', COALESCE(SUM(CASE WHEN er.scope = 'SCOPE_1' THEN er.carbon_grams ELSE 0 END), 0),
                'scope2Total', COALESCE(SUM(CASE WHEN er.scope = 'SCOPE_2' THEN er.carbon_grams ELSE 0 END), 0),
                'scope3Total', COALESCE(SUM(CASE WHEN er.scope = 'SCOPE_3' THEN er.carbon_grams ELSE 0 END), 0),
                'recordCount', COUNT(*),
                'verifiedCount', COUNT(CASE WHEN er.verified THEN 1 END)
            )
        )
        FROM emission_record er
        LEFT JOIN product p ON er.product_id = p.id
        WHERE EXTRACT(YEAR FROM er.date_recorded) = $REPORTING_YEAR
    ) TO '$TEMP_JSON' WITH CSV QUOTE '\"' ESCAPE '\\';
" > /dev/null 2>&1

# Check if data was extracted
if [ ! -f "$TEMP_JSON" ] || [ ! -s "$TEMP_JSON" ]; then
    echo "Error: Failed to extract data from database"
    exit 1
fi

# Clean up the JSON (remove CSV headers and quotes)
sed -i 's/^"//' "$TEMP_JSON"  # Remove leading quote
sed -i 's/"$//' "$TEMP_JSON"  # Remove trailing quote
sed -i 's/\\"/"/g' "$TEMP_JSON"  # Unescape quotes

if [ "$OUTPUT_FORMAT" = "xml" ]; then
    # Convert JSON to XML format for TARIC system
    cat > "$TEMP_XML" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<CBAMDeclaration xmlns="urn:eu:taxud:cbam:declaration:1.0">
    <Header>
        <ReportingPeriod>$REPORTING_YEAR</ReportingPeriod>
        <TenantId>$TENANT_ID</TenantId>
        <GeneratedAt>$(date -Iseconds)</GeneratedAt>
        <Format>XML</Format>
    </Header>
EOF

    # Use jq to parse JSON and generate XML (assuming jq is available)
    if command -v jq >/dev/null 2>&1; then
        jq -r '
            .emissions[]? | select(. != null) |
            "<EmissionRecord id=\"\(.id)\">
                <Scope>\(.scope)</Scope>
                <CarbonGrams>\(.carbonGrams)</CarbonGrams>
                <DateRecorded>\(.dateRecorded)</DateRecorded>
                <Source>\(.source)</Source>
                <Notes>\(.notes)</Notes>
                <Verified>\(.verified)</Verified>" +
            (if .product then
                "<Product>
                    <Id>\(.product.id)</Id>
                    <Name>\(.product.name)</Name>
                    <SKU>\(.product.sku)</SKU>
                    <Category>\(.product.category)</Category>
                </Product>"
            else "" end) +
            "</EmissionRecord>"
        ' "$TEMP_JSON" >> "$TEMP_XML"
    else
        echo "Warning: jq not available, generating basic XML structure"
        # Fallback XML generation
        echo "<!-- jq not available, basic XML structure -->" >> "$TEMP_XML"
    fi

    cat >> "$TEMP_XML" << EOF
    <Summary>
        <TotalEmissions>$(jq -r '.summary.totalEmissions // 0' "$TEMP_JSON" 2>/dev/null || echo "0")</TotalEmissions>
        <Scope1Total>$(jq -r '.summary.scope1Total // 0' "$TEMP_JSON" 2>/dev/null || echo "0")</Scope1Total>
        <Scope2Total>$(jq -r '.summary.scope2Total // 0' "$TEMP_JSON" 2>/dev/null || echo "0")</Scope2Total>
        <Scope3Total>$(jq -r '.summary.scope3Total // 0' "$TEMP_JSON" 2>/dev/null || echo "0")</Scope3Total>
        <RecordCount>$(jq -r '.summary.recordCount // 0' "$TEMP_JSON" 2>/dev/null || echo "0")</RecordCount>
        <VerifiedCount>$(jq -r '.summary.verifiedCount // 0' "$TEMP_JSON" 2>/dev/null || echo "0")</VerifiedCount>
    </Summary>
</CBAMDeclaration>
EOF

    cp "$TEMP_XML" "$OUTPUT_FILE"
else
    # JSON format - add metadata and clean up
    jq --arg tenant "$TENANT_ID" --arg year "$REPORTING_YEAR" --arg format "JSON" --arg generated "$(date -Iseconds)" '
        . + {
            header: {
                reportingPeriod: $year,
                tenantId: $tenant,
                generatedAt: $generated,
                format: $format
            }
        }
    ' "$TEMP_JSON" > "$OUTPUT_FILE"
fi

# Validate output file
if [ ! -f "$OUTPUT_FILE" ] || [ ! -s "$OUTPUT_FILE" ]; then
    echo "Error: Failed to generate output file"
    exit 1
fi

# Clean up temporary files
rm -f "$TEMP_JSON" "$TEMP_XML"

echo "CBAM compliance payload generated successfully:"
echo "File: $OUTPUT_FILE"
echo "Size: $(stat -f%z "$OUTPUT_FILE" 2>/dev/null || stat -c%s "$OUTPUT_FILE" 2>/dev/null || echo "unknown") bytes"
echo "Format: $OUTPUT_FORMAT"

# Optional: Submit to TARIC system (placeholder for future implementation)
# curl -X POST "https://taric.europa.eu/api/cbam/submit" \
#      -H "Authorization: Bearer $JWT_TOKEN" \
#      -H "Content-Type: application/${OUTPUT_FORMAT}" \
#      --data-binary @"$OUTPUT_FILE"

exit 0