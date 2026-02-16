#!/bin/bash
set -e

echo "🟢 Starting EcoTrack Pro Genesis Setup..."

# 1. Check Dependencies
command -v java >/dev/null 2>&1 || { echo "❌ Java not found"; exit 1; }
command -v docker >/dev/null 2>&1 || { echo "❌ Docker not found"; exit 1; }

# 2. Spin up Infrastructure
echo "🐳 Starting PostgreSQL and Keycloak..."
docker-compose -f src/main/docker/postgresql.yml up -d
docker-compose -f src/main/docker/keycloak.yml up -d

# 3. Build & Migrate
echo "🏗️ Building Spring Boot Backend & Liquibase Migrations..."
./mvnw clean compile liquibase:update

# 4. Frontend Install
echo "📦 Installing Angular Dependencies..."
npm install

echo "🚀 Setup Complete! Run './mvnw' to start the backend and 'npm start' for the UI."