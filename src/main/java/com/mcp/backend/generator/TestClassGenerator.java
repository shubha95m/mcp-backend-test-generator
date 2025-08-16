package com.mcp.backend.generator;

import com.mcp.backend.model.ApiRequest;

public class TestClassGenerator {

    public static String generateTestClass(ApiRequest apiRequest, String testPackage, String stepsPackage, String dtosPackage) {
        StringBuilder sb = new StringBuilder();
        sb.append("package " + testPackage + ";\n\n");
        sb.append("import org.testng.annotations.Test;\n");
        sb.append("import static org.assertj.core.api.Assertions.*;\n");
        sb.append("import io.restassured.response.Response;\n");
        // Import Steps and DTOs from their respective packages
        String stepsClassName = apiRequest.getStepsClassName();
        String requestDtoClassName = apiRequest.getRequestDtoClassName();
        String responseDtoClassName = apiRequest.getResponseDtoClassName();
        if (stepsClassName != null && !stepsClassName.isEmpty()) {
            sb.append("import " + stepsPackage + "." + stepsClassName + ";\n");
        }
        if (requestDtoClassName != null && !requestDtoClassName.isEmpty()) {
            sb.append("import " + dtosPackage + "." + requestDtoClassName + ";\n");
        }
        if (responseDtoClassName != null && !responseDtoClassName.isEmpty()) {
            sb.append("import " + dtosPackage + "." + responseDtoClassName + ";\n");
        }
        sb.append("\n");

        String testClassName = apiRequest.getTestClassName();
        sb.append("public class " + testClassName + " {\n\n");

        // Test method with descriptive name based on the operation
        sb.append("    @Test\n");
        sb.append("    public void should_create_new_post_and_verify_response() {\n");
        sb.append("        // Given\n");
        sb.append("        Steps steps = new Steps(\"" + apiRequest.getUrl().replaceAll("['\"]", "") + "\");\n");

        boolean hasRequestBody = apiRequest.getRequestBody() != null && !apiRequest.getRequestBody().isEmpty();
        if (hasRequestBody) {
            sb.append("\n        // Prepare request data\n");
            sb.append("        RequestDto requestBody = new RequestDto();\n");
            sb.append("        requestBody.setTitle(\"foo\");\n");
            sb.append("        requestBody.setBody(\"bar\");\n");
            sb.append("        requestBody.setUserId(1);\n");
        }

        sb.append("\n        // When\n");
        sb.append("        Response response = steps." + apiRequest.getTestName().toLowerCase() + "(");
        if (hasRequestBody) {
            sb.append("requestBody");
        }
        sb.append(");\n\n");

        sb.append("        // Then\n");
        sb.append("        assertThat(response.getStatusCode())\n");
        sb.append("            .as(\"Status code should be 200 OK\")\n");
        sb.append("            .isEqualTo(200);\n\n");

        if (apiRequest.getResponseBody() != null && !apiRequest.getResponseBody().isEmpty()) {
            sb.append("        ResponseDto responseDto = response.as(ResponseDto.class);\n");
            sb.append("        \n");
            sb.append("        // Verify response data\n");
            sb.append("        assertThat(responseDto)\n");
            sb.append("            .as(\"Response DTO should not be null\")\n");
            sb.append("            .isNotNull();\n\n");

            sb.append("        assertThat(responseDto.getTitle())\n");
            sb.append("            .as(\"Title should match the request\")\n");
            sb.append("            .isEqualTo(\"foo\");\n\n");

            sb.append("        assertThat(responseDto.getBody())\n");
            sb.append("            .as(\"Body should match the request\")\n");
            sb.append("            .isEqualTo(\"bar\");\n\n");

            sb.append("        assertThat(responseDto.getUserId())\n");
            sb.append("            .as(\"UserId should match the request\")\n");
            sb.append("            .isEqualTo(1);\n\n");

            sb.append("        assertThat(responseDto.getId())\n");
            sb.append("            .as(\"ID should be assigned by the server\")\n");
            sb.append("            .isPositive();\n");
        }

        sb.append("    }\n");
        sb.append("}\n");

        return sb.toString();
    }
}
