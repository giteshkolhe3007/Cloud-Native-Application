#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$ROOT_DIR/banking-cloud-native"

start_service() {
  local module="$1"
  echo "Starting ${module}..."
  (cd "$PROJECT_DIR/$module" && mvn spring-boot:run) &
}

start_service "eureka-server"
echo "Waiting 10 seconds for Eureka Server to initialize..."
sleep 10

start_service "api-gateway"
start_service "user-service"
start_service "account-service"
start_service "transaction-service"
start_service "notification-service"

echo "All services launched in background from this shell."
echo "Press Ctrl+C to stop this script (services may continue running)."
wait
