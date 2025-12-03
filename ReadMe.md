# Checkout Service API Documentation

## Overview

The Checkout Service API provides endpoints for calculating shopping cart totals with promotional offers applied.

## API Documentation 

### Access - Swagger UI (Interactive)
**URL:** `http://localhost:8080/swagger-ui/index.html`

### OpenAPI Specification (JSON)
**URL:** `http://localhost:8080/checkout-svc/openapi/v3/api-docs`

Raw OpenAPI 3.0 specification in JSON format for:
- Code generation
- API client creation
- Import into tools like Postman
- Documentation generation

---

## Quick Start

1. **Start the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

2. **Open Swagger UI:**
   Navigate to `http://localhost:8080/swagger-ui/index.html`

3. **Try the API:**
   - Use "POST /api/checkout/calculate-total"

---

## API Endpoints

### POST /api/checkout/calculate-total

Calculate the total price for a shopping cart with promotional offers applied.

**Request Body:**
```json
{
  "items": [
    {"itemName": "Apple", "quantity": 3},
    {"itemName": "Banana", "quantity": 2}
  ]
}
```

**Response (200 OK):**
```json
{
  "totalPrice": 175.50
}
```

**Error Responses:**

- **400 Bad Request** - Invalid request (empty items, null values, negative quantity)
  ```json
  {
    "type": "about:blank",
    "title": "Invalid Request",
    "status": 400,
    "detail": "Validation failed",
    "errors": ["items: must not be empty"]
  }
  ```

- **404 Not Found** - Price not found for item
  ```json
  {
    "type": "about:blank",
    "title": "Price Not Found",
    "status": 404,
    "detail": "No price found for item with name unknown"
  }
  ```

- **500 Internal Server Error** - Unexpected error
  ```json
  {
    "type": "about:blank",
    "title": "Internal Server Error",
    "status": 500,
    "detail": "An unexpected error occurred"
  }
  ```

---

## Available Items & Pricing

| Item      | Unit Price | Special Offer  |
|-----------|------------|----------------|
| Apple     | 30         | 2 for 45       |
| Banana    | 50         | 3 for 130      |
| Peach     | 60         | -              |
| Kiwi      | 20         | -              |
| Mango     | 10         | 2 for 15       |
| Pineapple | 25         | -              |

---

## Features

### Case-Insensitive Item Names
Items are case-insensitive. The following are equivalent:
- "Apple", "APPLE", "apple"

### Quantity Aggregation
Multiple line items with the same item name are automatically grouped together for offer calculations:

```json
{
  "items": [
    {"itemName": "Apple", "quantity": 2},
    {"itemName": "apple", "quantity": 1}
  ]
}
```
Total quantity for Apple: 3 (will apply "2 for 45" offer once)

### Promotional Offers
Offers are automatically applied when quantity thresholds are met:
- "2 for 45" means: Buy 2 items for a special price of 45 (instead of 60)
- "3 for 130" means: Buy 3 items for a special price of 130 (instead of 150)


---

## Validation Rules

### CheckoutRequest
- `items` - **Required**, must not be empty

### LineItem
- `itemName` - **Required**, must not be null
- `quantity` - **Required**, must be >= 1

---

## Testing with cURL

### Successful Request
```bash
curl -X POST "http://localhost:8080/api/checkout/calculate-total" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {"itemName": "Apple", "quantity": 5},
      {"itemName": "Banana", "quantity": 3},
      {"itemName": "Peach", "quantity": 1}
    ]
  }'
```

### Invalid Request (Empty Items)
```bash
curl -X POST "http://localhost:8080/api/checkout/calculate-total" \
  -H "Content-Type: application/json" \
  -d '{"items": []}'
```

### Unknown Item
```bash
curl -X POST "http://localhost:8080/api/checkout/calculate-total" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {"itemName": "Unknown", "quantity": 1}
    ]
  }'
```

