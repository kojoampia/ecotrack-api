## Phase 5: The Carbon Calculation Engine

In 2026, the intensity of the EU grid has dropped significantly (averaging around **110-140 gCO2e/kWh** depending on the region). Your SaaS needs to reflect these live factors to provide accurate "Embedded Emissions" reports.
Now we’re getting into the engine room. To make EcoTrack App more than just a CRUD app, it needs a "Brain" —a calculation engine that handles the 2026 regulatory landscape, specifically for the European Carbon Border Adjustment Mechanism (CBAM).

The Service Implementation: CarbonCalculatorService.java
We use a Strategy Pattern here.
One strategy for "Direct Emissions" (Scope 1) and another for "Indirect Emissions" (Scope 2) and another for "Supply Chain Goods" (Scope 3).

Now we’re getting into the engine room. To make **EcoTrack Pro** more than just a CRUD app, it needs a "Brain"—a calculation engine that handles the 2026 regulatory landscape, specifically for the **European Carbon Border Adjustment Mechanism (CBAM)**.

---

### The Service Implementation: `CarbonCalculatorService.java`

We use a **Strategy Pattern** here. One strategy for "Direct Emissions" (Scope 1), another for "Indirect Emissions" (Scope 2), and another for "Supply Chain Goods" (Scope 3).

---

We need a "Carbon Calculator" engine. Since we’re using MapStruct, we’ll transform raw IoT/User inputs into compliance-ready records.

Logic Implementation: Create a CarbonCalculationService.java.
Formula Integration: Use standard GHG Protocol constants.
E = A _ EF _ (1 - ER)
(Where E is emissions, A is activity data, EF is emission factor, and ER is the efficiency/reduction ratio).
