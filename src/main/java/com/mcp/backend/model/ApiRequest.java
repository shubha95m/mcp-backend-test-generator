package com.mcp.backend.model;

import java.util.Map;

public class ApiRequest {
    private String url;
    private String method;
    private Map<String, String> headers;
    private String requestBody;
    private String responseBody;
    private String testName;

    // Constructor
    public ApiRequest(String url, String method, Map<String, String> headers, String requestBody, String responseBody, String testName) {
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.testName = testName;
    }

    // Getters
    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String getTestName() {
        return testName;
    }

    public String getRequestDtoClassName() {
        return "RequestDto";
    }

    public String getResponseDtoClassName() {
        return "ResponseDto";
    }

    public String getStepsClassName() {
        return "Steps";
    }

    public String getTestClassName() {
        return "Tests";
    }
}
