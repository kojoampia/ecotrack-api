#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)

ENV_FILE="$SCRIPT_DIR/.env"
if [[ ! -f "$ENV_FILE" ]]; then
  ENV_FILE="$SCRIPT_DIR/$(basename "${BASH_SOURCE[0]}").env"
fi
if [[ ! -f "$ENV_FILE" ]]; then
  echo "No .env file found next to the script." >&2
  exit 1
fi

set -a
# shellcheck disable=SC1091
. "$ENV_FILE"
set +a

DB_HOST="${PGHOST:-localhost}"
DB_PORT="${PGPORT:-5432}"
DB_ADMIN_USER="${DB_ADMIN_USER:-${PGUSER:-postgres}}"
DB_ADMIN_PASSWORD="${DB_ADMIN_PASSWORD:-${PGPASSWORD:-}}"
DB_ADMIN_DATABASE="${DB_ADMIN_DATABASE:-${PGDATABASE:-postgres}}"
APP_DB_USER="${APP_DB_USER:-ecotrackApi}"
APP_DB_PASSWORD="${APP_DB_PASSWORD:-${DB_PASSWORD:-postgres}}"

if [[ -n "$DB_ADMIN_PASSWORD" ]]; then
  export PGPASSWORD="$DB_ADMIN_PASSWORD"
fi

psql "host=$DB_HOST port=$DB_PORT user=$DB_ADMIN_USER dbname=$DB_ADMIN_DATABASE" -v app_db_user="$APP_DB_USER" -v role_password="$APP_DB_PASSWORD" <<'SQL'
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_roles WHERE rolname = :'app_db_user') THEN
    EXECUTE format('ALTER ROLE %I WITH LOGIN PASSWORD %L', :'app_db_user', :'role_password');
  ELSE
    EXECUTE format('CREATE ROLE %I WITH LOGIN PASSWORD %L', :'app_db_user', :'role_password');
  END IF;
END
$$;
SQL
