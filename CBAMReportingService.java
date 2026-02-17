import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class CBAMReportingService {

    /**
     * Entry point: Converts raw supplier records into a consolidated Quarterly Report.
     */
    public CBAMReport generateReport(List<EmissionRecord> records) {
        CBAMReport report = new CBAMReport();
        report.setGeneratedAt(java.time.Instant.now());

        // 1. Group records by Unique Line Item Identity (CN Code + Installation)
        Map<ReportKey, List<EmissionRecord>> groupedRecords = records
            .stream()
            .collect(Collectors.groupingBy(r -> new ReportKey(r.getCnCode(), r.getInstallationName(), r.getCountry())));

        // 2. Process each group to calculate weighted averages
        List<ReportLineItem> lineItems = new ArrayList<>();

        for (Map.Entry<ReportKey, List<EmissionRecord>> entry : groupedRecords.entrySet()) {
            ReportLineItem item = calculateWeightedLineItem(entry.getKey(), entry.getValue());
            lineItems.add(item);
        }

        report.setLineItems(lineItems);
        return report;
    }

    /**
     * Core Logic: Calculates Weighted Averages for a specific aggregated line item.
     * Formula: Sum(Mass * SpecificEmission) / TotalMass
     */
    private ReportLineItem calculateWeightedLineItem(ReportKey key, List<EmissionRecord> batch) {
        BigDecimal totalMass = BigDecimal.ZERO;
        BigDecimal totalDirectLoad = BigDecimal.ZERO; // Mass * DirectEmission
        BigDecimal totalIndirectLoad = BigDecimal.ZERO; // Mass * IndirectEmission
        Set<String> uniquePrecursors = new HashSet<>();

        for (EmissionRecord record : batch) {
            BigDecimal mass = record.getQuantity();

            // Validation: Skip records with zero mass to avoid divide-by-zero later (though strictly shouldn't exist)
            if (mass.compareTo(BigDecimal.ZERO) <= 0) continue;

            totalMass = totalMass.add(mass);

            // Load = Mass (tonnes) * Specific Emission (tCO2e/t)
            totalDirectLoad = totalDirectLoad.add(mass.multiply(record.getDirectEmissions()));
            totalIndirectLoad = totalIndirectLoad.add(mass.multiply(record.getIndirectEmissions()));

            if (record.getPrecursors() != null && !record.getPrecursors().isEmpty()) {
                uniquePrecursors.add(record.getPrecursors());
            }
        }

        // Avoid division by zero
        if (totalMass.compareTo(BigDecimal.ZERO) == 0) return null;

        // Calculate Weighted Averages
        // Scale 4 is standard for emissions reporting
        BigDecimal weightedDirect = totalDirectLoad.divide(totalMass, 4, RoundingMode.HALF_UP);
        BigDecimal weightedIndirect = totalIndirectLoad.divide(totalMass, 4, RoundingMode.HALF_UP);

        // Construct the Line Item
        ReportLineItem item = new ReportLineItem();
        item.setInstallationName(key.installationName());
        item.setCountry(key.country());
        item.setCnCode(key.cnCode());
        item.setTotalQuantity(totalMass);
        item.setWeightedDirectEmissions(weightedDirect);
        item.setWeightedIndirectEmissions(weightedIndirect);
        item.setAggregatedPrecursors(String.join("; ", uniquePrecursors));

        return item;
    }

    // --- Data Structures (DTOs) ---

    // Input DTO
    public static class EmissionRecord {

        private String installationName;
        private String country;
        private String cnCode;
        private BigDecimal quantity;
        private BigDecimal directEmissions;
        private BigDecimal indirectEmissions;
        private String precursors;

        // Getters and Setters omitted for brevity
        public String getInstallationName() {
            return installationName;
        }

        public String getCountry() {
            return country;
        }

        public String getCnCode() {
            return cnCode;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public BigDecimal getDirectEmissions() {
            return directEmissions;
        }

        public BigDecimal getIndirectEmissions() {
            return indirectEmissions;
        }

        public String getPrecursors() {
            return precursors;
        }
    }

    // Aggregation Key
    private record ReportKey(String cnCode, String installationName, String country) {}

    // Output DTO
    public static class CBAMReport {

        private java.time.Instant generatedAt;
        private List<ReportLineItem> lineItems;

        public void setGeneratedAt(java.time.Instant generatedAt) {
            this.generatedAt = generatedAt;
        }

        public void setLineItems(List<ReportLineItem> lineItems) {
            this.lineItems = lineItems;
        }
    }

    // Consolidated Line Item for EU Registry
    public static class ReportLineItem {

        private String installationName;
        private String country;
        private String cnCode;
        private BigDecimal totalQuantity; // Sum of all shipments
        private BigDecimal weightedDirectEmissions; // Weighted Average Scope 1
        private BigDecimal weightedIndirectEmissions; // Weighted Average Scope 2
        private String aggregatedPrecursors;

        public void setInstallationName(String v) {
            this.installationName = v;
        }

        public void setCountry(String v) {
            this.country = v;
        }

        public void setCnCode(String v) {
            this.cnCode = v;
        }

        public void setTotalQuantity(BigDecimal v) {
            this.totalQuantity = v;
        }

        public void setWeightedDirectEmissions(BigDecimal v) {
            this.weightedDirectEmissions = v;
        }

        public void setWeightedIndirectEmissions(BigDecimal v) {
            this.weightedIndirectEmissions = v;
        }

        public void setAggregatedPrecursors(String v) {
            this.aggregatedPrecursors = v;
        }
    }
}
