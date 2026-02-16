We need a "Carbon Calculator" engine. Since we’re using MapStruct, we’ll transform raw IoT/User inputs into compliance-ready records.

Logic Implementation: Create a CarbonCalculationService.java.
Formula Integration: Use standard GHG Protocol constants.
E = A _ EF _ (1 - ER)
(Where E is emissions, A is activity data, EF is emission factor, and ER is the efficiency/reduction ratio).
