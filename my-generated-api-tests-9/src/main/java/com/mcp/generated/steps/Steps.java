package com.mcp.generated.steps;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import com.mcp.generated.steps.dtos.RequestDto;
import com.mcp.generated.steps.dtos.ResponseDto;

public class Steps {

    private final String BASE_URL;
    private final String PATH;

    public Steps(String url) {
        this.BASE_URL = extractBaseUrl(url);
        this.PATH = extractPath(url);
    }

    public Response postposts(RequestDto requestBody) {
        return given()
            .baseUri(BASE_URL)
            .header("Content-Type", "application/json")
            .body(requestBody)
        .post(PATH)
            .then()
            .extract()
            .response();
    }

    private String extractBaseUrl(String url) {
        try {
            java.net.URL aURL = new java.net.URL(url);
            StringBuilder baseUrlBuilder = new StringBuilder();
            if (aURL.getPort() > -1) {
                baseUrlBuilder.append(":").append(aURL.getPort());
            }
            baseUrlBuilder.append(aURL.getProtocol()).append("://").append(aURL.getHost());
            return baseUrlBuilder.toString();
        } catch (java.net.MalformedURLException e) {
            return ""; // Should not happen with valid URLs
        }
    }

    private String extractPath(String url) {
        try {
            java.net.URL aURL = new java.net.URL(url);
            String path = aURL.getPath();
            return path.isEmpty() ? "/" : path;
        } catch (java.net.MalformedURLException e) {
            return "/"; // Should not happen with valid URLs
        }
    }
}
