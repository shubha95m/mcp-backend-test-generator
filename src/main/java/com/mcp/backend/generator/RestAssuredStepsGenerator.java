package com.mcp.backend.generator;

import com.mcp.backend.model.ApiRequest;
import java.util.Map;

public class RestAssuredStepsGenerator {

    public static String generateSteps(ApiRequest apiRequest, String stepsPackage, String dtosPackage) {
        StringBuilder sb = new StringBuilder();
        sb.append("package " + stepsPackage + ";\n\n");
        sb.append("import io.restassured.response.Response;\n");
        sb.append("import static io.restassured.RestAssured.given;\n");
        // Import DTOs from dtosPackage
        String requestDtoClassName = apiRequest.getRequestDtoClassName();
        String responseDtoClassName = apiRequest.getResponseDtoClassName();
        if (requestDtoClassName != null && !requestDtoClassName.isEmpty()) {
            sb.append("import " + dtosPackage + "." + requestDtoClassName + ";\n");
        }
        if (responseDtoClassName != null && !responseDtoClassName.isEmpty()) {
            sb.append("import " + dtosPackage + "." + responseDtoClassName + ";\n");
        }
        sb.append("\n");

        String stepsClassName = apiRequest.getStepsClassName();

        sb.append("public class " + stepsClassName + " {\n\n");

        // Base URL (assuming it's constant for the API or can be passed dynamically)

        sb.append("    private final String BASE_URL;\n");
        sb.append("    private final String PATH;\n\n");

        // Constructor to initialize BASE_URL
        sb.append("    public " + stepsClassName + "(String url) {\n");
        sb.append("        this.BASE_URL = extractBaseUrl(url);\n");
        sb.append("        this.PATH = extractPath(url);\n");
        sb.append("    }\n\n");

        // API Call Method
        sb.append("    public Response " + apiRequest.getTestName().toLowerCase() + "(");
        boolean hasRequestBody = apiRequest.getRequestBody() != null && !apiRequest.getRequestBody().isEmpty();
        if (hasRequestBody) {
            sb.append(requestDtoClassName + " requestBody");
        }
        sb.append(") {\n");
        sb.append("        return given()\n");
        sb.append("            .baseUri(BASE_URL)\n");
        sb.append("            .header(\"Content-Type\", \"application/json\")\n");
        // Add other headers if present
        if (apiRequest.getHeaders() != null && !apiRequest.getHeaders().isEmpty()) {
            for (Map.Entry<String, String> entry : apiRequest.getHeaders().entrySet()) {
                if (!entry.getKey().equalsIgnoreCase("Content-Type")) {  // Skip if already added
                    sb.append("            .header(\"");
                    sb.append(entry.getKey());
                    sb.append("\", \"");
                    sb.append(entry.getValue());
                    sb.append("\")\n");
                }
            }
        }
        // Add request body if present
        if (hasRequestBody) {
            sb.append("            .body(requestBody)\n");
        }
        // Execute the request
        sb.append("        ." + apiRequest.getMethod().toLowerCase() + "(PATH)\n");
        sb.append("            .then()\n");
        sb.append("            .extract()\n");
        sb.append("            .response();\n");
        sb.append("    }\n\n");

        // Helper method to extract base URL
        sb.append("    private String extractBaseUrl(String url) {\n");
        sb.append("        try {\n");
        sb.append("            java.net.URL aURL = new java.net.URL(url);\n");
        sb.append("            StringBuilder baseUrlBuilder = new StringBuilder();\n");
        sb.append("            if (aURL.getPort() > -1) {\n");
        sb.append("                baseUrlBuilder.append(\":\").append(aURL.getPort());\n");
        sb.append("            }\n");
        sb.append("            baseUrlBuilder.append(aURL.getProtocol()).append(\"://\").append(aURL.getHost());\n");
        sb.append("            return baseUrlBuilder.toString();\n");
        sb.append("        } catch (java.net.MalformedURLException e) {\n");
        sb.append("            return \"\"; // Should not happen with valid URLs\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        // Helper method to extract path
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
