# MCP Backend Test Generator

Generate RestAssured API tests (Java) from curl commands.

## 🚀 Quick Start

### 1. Build the project
Navigate to the project root and build the executable JAR:

```bash
cd mcp-backend-test-generator
mvn clean install
```

### 2. Run the generator
Use the `--curl` flag to provide your curl command. The generated files will be placed in `./generated-backend-tests/` by default.

```bash
java -jar target/backend-test-generator-1.0.0-SNAPSHOT.jar --curl "curl -X POST https://api.example.com/data -H \"Content-Type: application/json\" -d \"{\\\"name\\\":\\\"test\\\",\\\"value\\\":123}\""
```

Alternatively, specify an output directory:

```bash
java -jar target/backend-test-generator-1.0.0-SNAPSHOT.jar --curl "curl -X GET https://api.example.com/users/123" --output my-api-tests
```

### 3. Run the generated tests
Navigate to the generated directory and run the Maven tests:

```bash
cd generated-backend-tests # or your specified output directory
mvn clean install
mvn test
```

## 📁 Generated Project Structure

When you run the generator, it creates a new Maven project in the specified output directory (e.g., `generated-backend-tests/`) with the following structure:

```
generated-backend-tests/
├── pom.xml
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── mcp/
│   │               └── backend/
│   │                   ├── model/
│   │                   │   ├── <YourRequestDto>.java   # Generated Request DTO
│   │                   │   └── <YourResponseDto>.java  # Generated Response DTO
│   │                   └── steps/
│   │                       └── <YourApiSteps>.java   # Generated RestAssured API step methods
│   └── test/
│       └── java/
│           └── com/
│               └── mcp/
│                   └── backend/
│                       └── test/
│                           └── <YourApiTest>.java      # Generated JUnit 5 test class
└── README.md
```

## ✨ Features
- **Curl Command Parsing**: Extracts URL, HTTP method, headers, and request body.
- **DTO Generation**: Infers and generates Java POJOs (DTOs) for request and response bodies using Jackson annotations.
- **RestAssured Steps**: Creates dedicated methods for API calls, handling content type, headers, and request bodies.
- **JUnit 5 Test Class**: Generates a runnable test class with basic assertions (status code, DTO deserialization).
- **Extendable**: The generated code is clean and easily extendable for more complex assertions and test scenarios.

## ⚠️ Limitations & Notes
- **JSON Complexity**: DTO generation for deeply nested or polymorphic JSON structures is basic. Manual refinement might be needed.
- **Error Handling**: Generated tests include basic assertions. Add more robust error handling and specific data validations as required.
- **Authentication**: Generated tests do not include authentication mechanisms (e.g., API keys, OAuth). You need to add these manually to the generated steps.

## 📄 License
MIT

## Contact
For questions or support, contact Shubham (shubham.sharma75319@gmail.com, +91 701-474-0879).
