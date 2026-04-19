@echo off
setlocal enabledelayedexpansion

echo 🔨 Building all services with Maven...
mvn clean install -DskipTests
if %ERRORLEVEL% neq 0 (
    echo Build failed!
    exit /b 1
)

echo 🚀 Starting Eureka Server...
cd cloud-configs\eureka-server
start "Eureka Server" cmd /c "mvn spring-boot:run > ..\..\logs\eureka.log 2>&1"
cd ..\..
timeout /t 30 /nobreak

echo 🌐 Starting API Gateway...
cd cloud-configs\api-gateway
start "API Gateway" cmd /c "mvn spring-boot:run > ..\..\logs\gateway.log 2>&1"
cd ..\..
timeout /t 30 /nobreak

echo 📦 Starting other services...
set SERVICES=security-service teacher-service address-service configuration-service campus-mgmt-service enrollment-service profiles-service

for %%s in (%SERVICES%) do (
    echo 📦 Starting %%s...
    cd %%s
    start "%%s" cmd /c "mvn spring-boot:run > ..\logs\%%s.log 2>&1"
    cd ..
    timeout /t 5 /nobreak
)

echo ✅ All services started successfully!
pause
