package com.mcp.backend.cli;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.mcp.backend.generator.DtoGenerator;
import com.mcp.backend.generator.RestAssuredStepsGenerator;
import com.mcp.backend.generator.TestClassGenerator;
import com.mcp.backend.generator.PomGenerator; // Import PomGenerator
import com.mcp.backend.generator.TestNGFileGenerator; // Import TestNGFileGenerator
import com.mcp.backend.parser.CurlParser;
import com.mcp.backend.model.ApiRequest;
import com.mcp.backend.util.FileUtils;

public class CliApplication {

    public static void main(String[] args) {
        String curlCommand = null;
        String outputDir = generateTimestampedDirName(); // Default to timestamped directory
        String responseBody = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--curl":
                    if (i + 1 < args.length) {
                        curlCommand = args[++i];
                    } else {
                        System.err.println("Error: --curl requires a value.");
                        printUsage();
                        System.exit(1);
                    }
                    break;
                case "--output":
                    if (i + 1 < args.length) {
                        outputDir = args[++i];
                    } else {
                        System.err.println("Error: --output requires a value.");
                        printUsage();
                        System.exit(1);
                    }
                    break;
                case "--response-body":
                    if (i + 1 < args.length) {
                        responseBody = args[++i];
                    } else {
                        System.err.println("Error: --response-body requires a value.");
                        printUsage();
                        System.exit(1);
                    }
                    break;
                case "--help":
                    printUsage();
                    System.exit(0);
                default:
                    System.err.println("Error: Unknown argument " + args[i]);
                    printUsage();
                    System.exit(1);
            }
        }

        if (curlCommand == null) {
            System.err.println("Error: --curl command is required.");
            printUsage();
            System.exit(1);
        }

        try {
            // 1. Parse curl command
            System.out.println("DEBUG: Starting test generation...");
            System.out.println("DEBUG: Output directory: " + outputDir);

            ApiRequest apiRequest = CurlParser.parse(curlCommand, responseBody);
            System.out.println("✅ Curl command parsed successfully.");
            System.out.println("DEBUG: URL from request: " + apiRequest.getUrl());

            // Set up base package and sub-packages
            String basePackage = "com.mcp.generated";
            String stepsPackage = basePackage + ".steps";
            String dtosPackage = stepsPackage + ".dtos";
            String testPackage = basePackage + ".test";

            System.out.println("DEBUG: Setting up output paths...");
            String stepsOutputPath = outputDir + "/src/main/java/" + stepsPackage.replace('.', '/');
            String dtosOutputPath = outputDir + "/src/main/java/" + dtosPackage.replace('.', '/');
            String testOutputPath = outputDir + "/src/test/java/" + testPackage.replace('.', '/');

            System.out.println("DEBUG: Steps output path: " + stepsOutputPath);
            System.out.println("DEBUG: DTOs output path: " + dtosOutputPath);
            System.out.println("DEBUG: Test output path: " + testOutputPath);

            // 2. Generate POM file
            System.out.println("DEBUG: Generating pom.xml...");
            String pomContent = PomGenerator.generatePom(apiRequest.getTestName());
            FileUtils.writeFile(outputDir, "pom.xml", pomContent);
            System.out.println("✅ POM file generated: " + outputDir + "/pom.xml");

            // 3. Generate DTOs
            if (apiRequest.getRequestBody() != null && !apiRequest.getRequestBody().isEmpty()) {
                String requestDtoContent = DtoGenerator.generateDto(apiRequest.getRequestDtoClassName(), apiRequest.getRequestBody(), dtosPackage);
                FileUtils.writeFile(dtosOutputPath, apiRequest.getRequestDtoClassName() + ".java", requestDtoContent);
                System.out.println("✅ Request DTO generated: " + apiRequest.getRequestDtoClassName());
            }

            if (apiRequest.getResponseBody() != null && !apiRequest.getResponseBody().isEmpty()) {
                String responseDtoContent = DtoGenerator.generateDto(apiRequest.getResponseDtoClassName(), apiRequest.getResponseBody(), dtosPackage);
                FileUtils.writeFile(dtosOutputPath, apiRequest.getResponseDtoClassName() + ".java", responseDtoContent);
                System.out.println("✅ Response DTO generated: " + apiRequest.getResponseDtoClassName());
            }

            // 4. Generate RestAssured steps
            String stepsContent = RestAssuredStepsGenerator.generateSteps(apiRequest, stepsPackage, dtosPackage);
            FileUtils.writeFile(stepsOutputPath, apiRequest.getStepsClassName() + ".java", stepsContent);
            System.out.println("✅ RestAssured steps generated: " + apiRequest.getStepsClassName());

            // 5. Generate Test class
            String testClassContent = TestClassGenerator.generateTestClass(apiRequest, testPackage, stepsPackage, dtosPackage);
            FileUtils.writeFile(testOutputPath, apiRequest.getTestClassName() + ".java", testClassContent);
            System.out.println("✅ Test class generated: " + apiRequest.getTestClassName());

            // 6. Generate TestNG XML configuration
            String testNGContent = TestNGFileGenerator.generateTestNGXml();
            FileUtils.writeFile(outputDir, "testng.xml", testNGContent);
            System.out.println("✅ TestNG configuration generated: " + outputDir + "/testng.xml");

            System.out.println("\nAll files generated successfully in: " + outputDir);
            System.out.println("\nTo run the generated tests:\ncd " + outputDir + "\nmvn clean test");

        } catch (Exception e) {
            System.err.println("💥 Error during test generation: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String generateTimestampedDirName() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-HH-mm").withLocale(java.util.Locale.ENGLISH);
        return "my-generated-api-tests-" + now.format(formatter);
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar backend-test-generator.jar --curl \"<curl_command>\" [--response-body \"<json_string>\"] [--output <output_directory>] [--help]");
        System.out.println("\nOptions:");
        System.out.println("  --curl <curl_command>     : The full curl command to parse.");
        System.out.println("  --response-body <json_string> : Optional. A JSON string representing the expected API response body, used to generate response DTO.");
        System.out.println("  --output <output_directory> : Directory to save generated files (default: timestamped directory e.g., my-generated-api-tests-16-Aug-16-41).");
        System.out.println("  --help                    : Display this help message.");
    }
}
