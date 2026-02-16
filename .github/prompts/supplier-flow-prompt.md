Prompt Specification: CBAM Supplier Component
Goal: Create a secure, feature-rich application using Angular to facilitate the collection, collation, and verification of carbon emissions data for the EU Carbon Border Adjustment Mechanism (CBAM) from suppliers, with a focus on a seamless user experience for both data submitters and administrators.
A Tenant (administrator) will have multiple Suppliers (data submitters) under their management. The application must support role-based access control, ensuring that Suppliers can only submit data while the Tenant can create, update, delete, view, verify, and manage suppliers.
A Tenant can have multiple Suppliers, and each Supplier can submit multiple reports. The application should provide a dashboard for the Tenant to view aggregated data and manage supplier submissions effectively.

1. Technical Stack Requirements
   Framework: Angular (Standalone Components, Signals architecture). Use @if and @for directives for conditional rendering and list generation.
   Styling: Tailwind CSS (Enterprise-grade UI, Emerald/Slate color palette) and Angular Material M3.
   Backend: Postgres (RLS) strongly tied to user roles and tenant ownership.
   Icons: Inline SVGs (No external library dependencies).
2. Role-Based Access Control
   The app must support two distinct user views based on a simulated login selection:
   A. Supplier Role (Data Collection)
   Access: Can only submit new data.
   Form Requirements: A comprehensive data entry form mirroring the EU CBAM Communication Template.
   Installation Data: Name, Country, UNLOCODE.
   Goods: CN Code selection (e.g., 7318 Screws, 2523 Cement), Net Mass (tonnes).
   Emissions: Specific Direct Emissions (), Specific Indirect Emissions ().
   Precursors: Text area for listing input materials.
   Action: Submits data to a public Postgres table cbam_reports.
   B. Importer Admin Role (Collation & Verification)
   Access: Can view, verify, delete, and export data.
   Dashboard Stats:
   Real-time aggregation using Angular computed() signals.
   Metrics: Total Reports, Total Mass Imported, Weighted Average Direct Emissions, Verification Progress.
   Data Management:
   List View: Real-time feed of supplier submissions.
   Detail View: Side panel showing full report data.
   Verification: Toggle button to mark reports as "Verified" (Green status) or "Pending" (Amber status).
   Export: Ability to download all "Verified" reports as a CSV file for EU Registry upload.

3. Data Structure (Postgres Interface)
   The data model for a Report must include:
   interface Report {
   id: string;
   installationName: string; // e.g., "SteelWorks Factory A"
   cnCode: string; // e.g., "73180000"
   quantity: number; // Net mass
   directEmissions: number; // Scope 1
   indirectEmissions: number;// Scope 2
   verified: boolean; // Admin control
   timestamp: Timestamp; // Submission time
   }

4. User Interface Components
   A. Supplier View

- Data Entry Form: A structured form with input fields for installation data, goods details, emissions, and precursors. Use Angular Material form controls styled with Tailwind CSS for a cohesive look.
- Submit Button: A prominent button styled with emerald-600 to encourage submission, with a loading state during data processing.
  B. Importer Admin View
- Dashboard: A clean, organized layout displaying key metrics at the top and a real-time feed of supplier submissions below. Use Tailwind CSS grid and flex utilities for layout.
- List View: A scrollable list of reports with basic details (Installation Name, CN Code, Quantity, Status). Each item should be clickable to open the detail view.
- Detail View: A side panel that slides in from the right, showing all details of the selected report, including a toggle for verification status.
- Export Button: A clearly visible button to export verified reports, styled with emerald-600, and placed in the dashboard header for easy access.

5. Visual & UX Guidelines
   Theme: Professional compliance tool aesthetic. Use bg-slate-50 for backgrounds, emerald-600 for primary actions, and slate-800 for typography.
   Responsiveness: The layout must adapt to mobile devices (stacking the list and detail views).
   Feedback: Loading states (skeleton screens or spinners) during Auth/Data fetching.
   Example User Story:
   "As a Supplier, I log in and submit the emissions for 50 tonnes of Steel Screws. As an Admin, I immediately see this submission appear on my dashboard, check the 'Precursors' field, mark it as 'Verified', and then export the verified data to Excel."

6. Implementation Notes

- Use Angular's reactive forms for the data entry form to ensure robust validation and user feedback.
- Leverage Angular's Signals for real-time data updates and computed properties to calculate dashboard metrics efficiently.
- Implement Role-Based Access Control (RBAC) at the database level using Postgres Row-Level Security (RLS) to ensure data integrity and security.
- Use Tailwind CSS utility classes to create a clean and responsive UI without the overhead of custom CSS.
- Ensure that all icons are implemented as inline SVGs to maintain performance and avoid external dependencies.
- For data export functionality, consider using a library like PapaParse to convert JSON data to CSV.
- Implement comprehensive error handling and user feedback mechanisms to enhance the user experience, especially during data submission and verification processes.
- Ensure that the application is secure, with proper authentication and authorization mechanisms in place to protect sensitive data and ensure that only authorized users can access specific functionalities.
- Consider implementing a mock authentication system for the purpose of this application, allowing users to select their role (Supplier or Importer Admin) upon login to simulate the different user experiences.
- Use Angular Material components where appropriate to enhance the UI and provide a consistent user experience, while ensuring that the overall design remains cohesive with the Tailwind CSS styling.
