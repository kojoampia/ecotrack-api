import requests
import datetime
import json
import os

# Configuration
PROMETHEUS_URL = os.getenv("PROMETHEUS_URL", "http://localhost:9090")
# Set this environment variable to your Slack/Teams webhook URL
SLACK_WEBHOOK_URL = os.getenv("SLACK_WEBHOOK_URL", "") 
OUTPUT_DIR = "./invoices"
MONTHLY_THRESHOLD = 5000.00  # Threshold defined in prometheus-alerts.yml

PRICING = {
    "ledger_entry": 0.05,
    "ai_strategy": 2.00,
    "ai_simulation": 0.50,
    "cbam_report": 150.00
}

def query_prometheus(query, range_str="30d"):
    """Queries Prometheus for the increase of a counter over a range."""
    full_query = f"sum by (tenant_id) (increase({query}[{range_str}]))"
    params = {'query': full_query}
    try:
        response = requests.get(f"{PROMETHEUS_URL}/api/v1/query", params=params)
        response.raise_for_status()
        return response.json()['data']['result']
    except Exception as e:
        print(f"Error querying Prometheus for {query}: {e}")
        return []

def notify_slack(tenant_id, total_amount, overage):
    """Sends a high-priority alert to the finance Slack channel."""
    if not SLACK_WEBHOOK_URL:
        return

    payload = {
        "text": f"🚨 *Financial Alert: Budget Breach Detected*",
        "attachments": [
            {
                "color": "#ef4444",
                "fields": [
                    {"title": "Tenant ID", "value": tenant_id, "short": True},
                    {"title": "Billing Cycle", "value": datetime.date.today().strftime('%B %Y'), "short": True},
                    {"title": "Total Accrued", "value": f"${total_amount:,.2f}", "short": True},
                    {"title": "Overage Amount", "value": f"${overage:,.2f}", "short": True}
                ],
                "footer": "EcoTrack Pro Billing Service",
                "ts": datetime.datetime.now().timestamp()
            }
        ]
    }

    try:
        requests.post(SLACK_WEBHOOK_URL, json=payload)
    except Exception as e:
        print(f"Failed to send Slack notification: {e}")

def generate_statements():
    print(f"🚀 EcoTrack Pro: Generating Monthly Statements for {datetime.date.today().strftime('%B %Y')}...")
    
    # 1. Fetch Metrics from Prometheus
    metrics = {
        "ledger_entries": query_prometheus("carbon_calculation_total"),
        "ai_strategies": query_prometheus("ai_strategies_generated_total"),
        "ai_simulations": query_prometheus("ai_simulations_run_total"),
        "cbam_reports": query_prometheus("cbam_reports_generated_total")
    }

    # 2. Pivot data by Tenant
    tenant_data = {}

    for category, results in metrics.items():
        for item in results:
            tenant_id = item['metric'].get('tenant_id', 'unknown_tenant')
            value = float(item['value'][1])
            
            if tenant_id not in tenant_data:
                tenant_data[tenant_id] = {
                    "ledger_entries": 0, "ai_strategies": 0, 
                    "ai_simulations": 0, "cbam_reports": 0
                }
            tenant_data[tenant_id][category] = round(value)

    # 3. Calculate and Write Statements
    if not os.path.exists(OUTPUT_DIR):
        os.makedirs(OUTPUT_DIR)

    for tenant_id, stats in tenant_data.items():
        total_ledger = stats['ledger_entries'] * PRICING['ledger_entry']
        total_ai_strat = stats['ai_strategies'] * PRICING['ai_strategy']
        total_ai_sim = stats['ai_simulations'] * PRICING['ai_simulation']
        total_cbam = stats['cbam_reports'] * PRICING['cbam_report']
        
        grand_total = total_ledger + total_ai_strat + total_ai_sim + total_cbam
        
        # Determine if budget alert is needed
        is_breach = grand_total > MONTHLY_THRESHOLD
        breach_footer = ""
        if is_breach:
            overage = grand_total - MONTHLY_THRESHOLD
            breach_footer = f"""
!!! BUDGET WARNING !!!
This account has exceeded the Tier 1 Monthly Budget of ${MONTHLY_THRESHOLD:,.2f}.
Current Overage: ${overage:,.2f}
Action Required: Initiate Tier 2 Upgrade or apply overage surcharge.
!!!!!!!!!!!!!!!!!!!!!!
"""
            # Send real-time Slack notification
            notify_slack(tenant_id, grand_total, overage)

        invoice_content = f"""
==================================================
        ECOTRACK PRO - MONTHLY STATEMENT
==================================================
Date: {datetime.date.today()}
Tenant ID: {tenant_id}
Status: {"⚠️ OVER LIMIT" if is_breach else "✅ WITHIN LIMIT"}
--------------------------------------------------
ITEM                    QTY      RATE      TOTAL
--------------------------------------------------
Green Ledger Entries    {stats['ledger_entries']:<8} ${PRICING['ledger_entry']:<8.2f} ${total_ledger:>8.2f}
AI Reduction Strategies {stats['ai_strategies']:<8} ${PRICING['ai_strategy']:<8.2f} ${total_ai_strat:>8.2f}
AI Simulations Run      {stats['ai_simulations']:<8} ${PRICING['ai_simulation']:<8.2f} ${total_ai_sim:>8.2f}
CBAM Declarations       {stats['cbam_reports']:<8} ${PRICING['cbam_report']:<8.2f} ${total_cbam:>8.2f}
--------------------------------------------------
GRAND TOTAL:                           ${grand_total:>8.2f}
==================================================
{breach_footer}
Thank you for securing your compliance with EcoTrack.
        """
        
        file_path = f"{OUTPUT_DIR}/invoice_{tenant_id}_{datetime.date.today()}.txt"
        with open(file_path, "w") as f:
            f.write(invoice_content)
        
        status_icon = "❌" if is_breach else "✅"
        print(f"{status_icon} Generated statement for {tenant_id}: ${grand_total:.2f}")

if __name__ == "__main__":
    generate_statements()