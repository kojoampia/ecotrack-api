In 2026, standard tables aren't enough. We’ll use Angular Material combined with ngx-charts (or Chart.js) to create a high-density analytics view.

carbon-dashboard.component.html
We use mat-card for layout and ngx-charts-bar-vertical-stacked to visualize the three emission Scopes.

Compliance Overview page shows the following:
#1 On a stats-grid layer, a mat-card with class stat-card, titled total number, measured in kg CO2e and sub titled "Total Footprint"
#2 On a mat-card class chart-card is titled Emissions by Source showing emissionsBySourceData in the results value field.
#3 Any further graph you think is appropriate for displaying CO2 emissions data.
#4 Menu Items listing graph titles in the side navigation
