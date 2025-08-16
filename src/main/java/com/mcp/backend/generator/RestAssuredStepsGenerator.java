package com.mcp.backend.generator;

import com.mcp.backend.model.ApiRequest;
import java.util.Map;

public class RestAssuredStepsGenerator {

    public static String generateSteps(ApiRequest apiRequest, String stepsPackage, String dtosPackage) {
        StringBuilder sb = new StringBuilder();
        String requestDtoClassName = apiRequest.getRequestDtoClassName();
        String responseDtoClassName = apiRequest.getResponseDtoClassName();

        // Generate DTOs first
        DtoGenerator.generateRequestAndResponseDtos(
            requestDtoClassName,
            responseDtoClassName,
            apiRequest.getRequestBody(),
            apiRequest.getResponseBody(),
            dtosPackage
        );

        // Generate Steps class content
        sb.append(String.format("package %s;%n%n", stepsPackage));

        // Imports
        sb.append("import io.restassured.response.Response;\n");
        sb.append("import static io.restassured.RestAssured.given;\n");
        if (requestDtoClassName != null && !requestDtoClassName.isEmpty()) {
            sb.append(String.format("import %s.%s;%n", dtosPackage, requestDtoClassName));
        }
        if (responseDtoClassName != null && !responseDtoClassName.isEmpty()) {
            sb.append(String.format("import %s.%s;%n", dtosPackage, responseDtoClassName));
        }
        sb.append("\n");

        // Class definition
        sb.append(String.format("public class Steps {%n%n"));
        sb.append("    private final String BASE_URL;\n");
        sb.append("    private final String PATH;\n\n");

        // Constructor
        sb.append("    public Steps(String url) {\n");
        sb.append("        this.BASE_URL = extractBaseUrl(url);\n");
        sb.append("        this.PATH = extractPath(url);\n");
        sb.append("    }\n\n");

        // API Call Method
        sb.append(String.format("    public Response %s(%s requestBody) {%n",
            apiRequest.getTestName().toLowerCase(), requestDtoClassName));
        sb.append("        return given()\n");
        sb.append("            .baseUri(BASE_URL)\n");
        sb.append("            .header(\"Content-Type\", \"application/json\")\n");
        sb.append("            .body(requestBody)\n");
        sb.append(String.format("        .%s(PATH)%n", apiRequest.getMethod().toLowerCase()));
        sb.append("            .then()\n");
        sb.append("            .extract()\n");
        sb.append("            .response();\n");
        sb.append("    }\n\n");

        // Helper methods
        sb.append("    private String extractBaseUrl(String url) {\n");
        sb.append("        try {\n");
        sb.append("            java.net.URL aURL = new java.net.URL(url);\n");
        sb.append("            StringBuilder baseUrlBuilder = new StringBuilder();\n");
        sb.append("            baseUrlBuilder.append(aURL.getProtocol()).append(\"://\").append(aURL.getHost());\n");
        sb.append("            if (aURL.getPort() > -1) {\n");
        sb.append("                baseUrlBuilder.append(\":\").append(aURL.getPort());\n");
        sb.append("            }\n");
        sb.append("            return baseUrlBuilder.toString();\n");
        sb.append("        } catch (java.net.MalformedURLException e) {\n");
        sb.append("            return \"\"; // Should not happen with valid URLs\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    private String extractPath(String url) {\n");
        sb.append("        try {\n");
        sb.append("            java.net.URL aURL = new java.net.URL(url);\n");
        sb.append("            String path = aURL.getPath();\n");
        sb.append("            return path.isEmpty() ? \"/\" : path;\n");
        sb.append("        } catch (java.net.MalformedURLException e) {\n");
        sb.append("            return \"/\"; // Should not happen with valid URLs\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("}\n");

        return sb.toString();
    }

}
