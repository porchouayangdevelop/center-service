#!/bin/bash

# Diagnostic Script for Scalar/OpenAPI Issues
# Run this after starting your application

echo "======================================"
echo "CBS Center Service Diagnostic Script"
echo "======================================"
echo ""

BASE_URL="http://localhost:9070/api/v1/uat/center-service"

echo "1. Testing Root Endpoint..."
RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" ${BASE_URL}/)
if [ "$RESPONSE" = "200" ]; then
    echo "   ✓ Root endpoint OK (200)"
    curl -s ${BASE_URL}/
    echo ""
else
    echo "   ✗ Root endpoint FAILED (HTTP $RESPONSE)"
fi
echo ""

echo "2. Testing OpenAPI JSON (v3/api-docs)..."
RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" ${BASE_URL}/v3/api-docs)
if [ "$RESPONSE" = "200" ]; then
    echo "   ✓ OpenAPI JSON OK (200)"
    echo "   First 500 characters:"
    curl -s ${BASE_URL}/v3/api-docs | head -c 500
    echo "..."
else
    echo "   ✗ OpenAPI JSON FAILED (HTTP $RESPONSE)"
    echo "   This is the ROOT CAUSE - Scalar won't work without this!"
fi
echo ""

echo "3. Testing Swagger UI..."
RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" ${BASE_URL}/swagger-ui/index.html)
if [ "$RESPONSE" = "200" ] || [ "$RESPONSE" = "302" ]; then
    echo "   ✓ Swagger UI OK ($RESPONSE)"
else
    echo "   ✗ Swagger UI FAILED (HTTP $RESPONSE)"
fi
echo ""

echo "4. Testing Scalar UI..."
RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" ${BASE_URL}/scalar)
if [ "$RESPONSE" = "200" ]; then
    echo "   ✓ Scalar UI OK (200)"
else
    echo "   ✗ Scalar UI FAILED (HTTP $RESPONSE)"
fi
echo ""

echo "5. Testing Teller API..."
RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" ${BASE_URL}/tellers/alls)
if [ "$RESPONSE" = "200" ] || [ "$RESPONSE" = "204" ]; then
    echo "   ✓ Teller API OK ($RESPONSE)"
else
    echo "   ✗ Teller API FAILED (HTTP $RESPONSE)"
fi
echo ""

echo "======================================"
echo "URLs to try in browser:"
echo "======================================"
echo "Root:       ${BASE_URL}/"
echo "OpenAPI:    ${BASE_URL}/v3/api-docs"
echo "Swagger:    ${BASE_URL}/swagger-ui/index.html"
echo "Scalar:     ${BASE_URL}/scalar"
echo "Tellers:    ${BASE_URL}/tellers/alls"
echo ""

echo "======================================"
echo "Checking dependencies..."
echo "======================================"
if command -v mvn &> /dev/null; then
    echo "SpringDoc dependency:"
    mvn dependency:tree 2>/dev/null | grep springdoc || echo "   ✗ SpringDoc NOT FOUND - THIS IS THE PROBLEM!"
    echo ""
    echo "Scalar dependency:"
    mvn dependency:tree 2>/dev/null | grep scalar || echo "   ✗ Scalar NOT FOUND"
else
    echo "Maven not found in PATH, skipping dependency check"
fi
echo ""

echo "======================================"
echo "Summary:"
echo "======================================"
if [ "$RESPONSE" = "200" ]; then
    echo "✓ Everything looks good!"
else
    echo "✗ Issues detected. Check the failures above."
    echo ""
    echo "Most common fix:"
    echo "1. Add SpringDoc dependency to pom.xml"
    echo "2. Run: mvn clean install"
    echo "3. Restart application"
fi