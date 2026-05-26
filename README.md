# BFHL Spring Boot API Challenge

An enterprise-grade, highly scalable, and production-ready implementation of the Bajaj Finserv Health Limited (BFHL) developer challenge REST API. This application separates mixed input arrays into numbers (odd and even), alphabets (upper-cased), and special characters, calculates the summation of all numeric inputs, and generates a formatted reversed string with alternating capitalization rules.

---

## Technical Stack & Architecture

- **Language:** Java 17
- **Framework:** Spring Boot 3.2.5
- **Build Tool:** Maven 3+
- **Metadata Configuration:** Annotation-driven via Lombok
- **Validation:** Jakarta Validation API (`spring-boot-starter-validation`)
- **Testing:** JUnit 5 & Mockito
- **Logging:** SLF4J (backed by Logback)

### Architecture Highlights
The codebase adheres strictly to the **SOLID principles** and a **Layered Architecture Pattern**:
1. **Controller Layer (`com.bfhl.controller`):** Exposes HTTP endpoints, parses incoming payloads, performs schema-level validations, logs traffic metadata, and returns standard ResponseEntities.
2. **DTO Layer (`com.bfhl.dto`):** Separates presentation entities from core business entities. Uses standard Jackson annotations (`@JsonProperty`) for precise, predictable payload naming formats.
3. **Service Layer (`com.bfhl.service`):** Contains interface-driven business operations. The implementation layer isolates classifications, calculations, and mutations from the controller.
4. **Exception Handling (`com.bfhl.exception`):** Uses a `@RestControllerAdvice` global interceptor to format all runtime/business/request-binding exceptions into readable JSON structures ensuring the `"is_success": false` contract is always honored.
5. **Utility Layer (`com.bfhl.util`):** Decoupled static functions for fast string classification and string manipulation algorithms.

---

## API Specifications

### POST `/bfhl`

Processes a mixed data array and categorizes elements.

#### **Request Payload Format**
- **Method:** `POST`
- **Content-Type:** `application/json`

```json
{
  "data": ["a", "1", "334", "4", "R", "$"]
}
```

#### **Success Response Format (HTTP 200 OK)**
- **Content-Type:** `application/json`

```json
{
  "is_success": true,
  "user_id": "ajinkya_sukhtankar_05022005",
  "email": "ajinkyasukhtankar230619@acropolis.in",
  "roll_number": "0827CS231023",
  "odd_numbers": ["1"],
  "even_numbers": ["334", "4"],
  "alphabets": ["A", "R"],
  "special_characters": ["$"],
  "sum": "339",
  "concat_string": "Ra"
}
```

#### **Error Response Format (HTTP 400 Bad Request)**
If the payload is malformed or invalid:
```json
{
  "is_success": false,
  "error": "Malformed JSON request body"
}
```

---

## Logic and Validation Rules

1. **User ID Scheme:** Generated dynamically in the format `<name_lowercase>_<dob>`. E.g., `ajinkya_sukhtankar_05022005`.
2. **Numeric Values:** Classified using the regular expression `^-?\d+$` (supporting signed integers). Summation is computed using `java.math.BigInteger` to prevent numerical overflows. Even/odd routing uses mathematical remainders. All numbers in lists and sums are output as strings.
3. **Alphabet Values:** Classified using `^[a-zA-Z]+$`. Characters are converted to uppercase for the `alphabets` array.
4. **Special Characters:** Evaluated as any string that is neither pure numeric nor pure alphabetic.
5. **Concat String Formulation:**
   - Letters from alphabetic strings are extracted sequentially.
   - The sequence is reversed.
   - Alternating caps are applied where characters at index `i` are made uppercase if `i` is even, and lowercase if `i` is odd.
   - Example: `["A", "ABCD", "DOE"]` -> `AABCDDOE` -> Reversed: `EODDCBAA` -> Result: `EoDdCbAa`.

---

## Local Setup & Run Instructions

### Prerequisites
- **Java JDK 17** installed and configured in your environment path.
- **Maven** installed.

### Step 1: Clone the repository & Navigate
```bash
cd Java_Baja_Project
```

### Step 2: Build the project
To build the project and package it into a runnable JAR, run:
```bash
mvn clean package
```

### Step 3: Run the Application
You can run the application directly using the Spring Boot plugin:
```bash
mvn spring-boot:run
```
Alternatively, execute the compiled JAR file:
```bash
java -jar target/bfhl-0.0.1-SNAPSHOT.jar
```
The application will start locally on port `8080` (unless configured otherwise).

---

## Running Unit and Integration Tests

The test suite covers:
- Valid mixed classifications
- Empty data input arrays
- Numeric-only inputs (including negative numbers and zero)
- Alphabet-only inputs (verifying multi-character string reversals)
- Special character filtering
- Edge cases like large integers (overflow protection validation)
- Malformed payloads and null validations

To execute the test suite:
```bash
mvn test
```

---

## Deployment Instructions

The application is pre-configured with a `Procfile` and dynamic port binding, making it immediately ready for deployment on platforms like Render or Railway.

### **Option A: Railway Deployment**
1. Log in to [Railway](https://railway.app/).
2. Click **New Project** -> **Deploy from GitHub repo**.
3. Choose this repository.
4. Railway will automatically read the `Procfile` and start the server using the configuration:
   `web: java -jar target/bfhl-0.0.1-SNAPSHOT.jar`
5. Set environment variables on the dashboard if you want to override configurations (e.g. `PORT`).

### **Option B: Render Deployment**
1. Log in to [Render](https://render.com/).
2. Create a new **Web Service** and connect your GitHub repository.
3. Set the following build and run settings:
   - **Environment:** `Java`
   - **Build Command:** `mvn clean package -DskipTests`
   - **Start Command:** `java -jar target/bfhl-0.0.1-SNAPSHOT.jar`
4. Set the Java version to `17` if prompted.
5. Render will assign an external URL and expose it over HTTPS.

---

## Sample Client Requests

### **cURL Command**
```bash
curl -X POST http://localhost:8080/bfhl \
  -H "Content-Type: application/json" \
  -d '{"data": ["a", "1", "334", "4", "R", "$"]}'
```

### **Postman Payload JSON**
```json
{
  "data": [
    "a",
    "1",
    "334",
    "4",
    "R",
    "$"
  ]
}
```
