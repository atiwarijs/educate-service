#!/bin/bash

# Exit on error
set -e

# Step 1: Build all services
echo "🔨 Building all services with Maven..."
mvn clean install -DskipTests

# Step 2: Start Eureka Server
echo "🚀 Starting Eureka Server..."
cd cloud-configs/eureka-server
nohup mvn spring-boot:run > ../../logs/eureka.log 2>&1 &
cd ../..
sleep 30

# Step 3: Start API Gateway
echo "🌐 Starting API Gateway..."
cd cloud-configs/api-gateway
nohup mvn spring-boot:run > ../../logs/gateway.log 2>&1 &
cd ../..
sleep 30

# Step 4: Start other services
SERVICES=("security-service" "teacher-service" "address-service" "configuration-service" "campus-mgmt-service" "enrollment-service" "profiles-service")

for service in "${SERVICES[@]}"; do
  echo "📦 Starting $service..."
  cd $service
  nohup mvn spring-boot:run > ../logs/${service}.log 2>&1 &
  cd ..
  sleep 5
done

echo "✅ All services started successfully!"
