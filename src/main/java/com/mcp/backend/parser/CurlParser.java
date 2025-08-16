package com.mcp.backend.parser;

import com.mcp.backend.model.ApiRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurlParser {

    private static final Pattern URL_PATTERN = Pattern.compile("(https?://[^'\"\\s]+)");
    private static final Pattern METHOD_PATTERN = Pattern.compile("-X\\s+(\\S+)");
    private static final Pattern HEADER_PATTERN = Pattern.compile("-H\\s+'([^']+)'");
    private static final Pattern DATA_PATTERN = Pattern.compile("--data\\s+'(\\{[^}]+\\})'");

    public static ApiRequest parse(String curlCommand, String responseBody) {
        System.out.println("DEBUG: Parsing curl command: " + curlCommand);
        System.out.println("DEBUG: Response body: " + responseBody);

        String url = extract(curlCommand, URL_PATTERN);
        System.out.println("DEBUG: Extracted URL: " + url);

        String method = extract(curlCommand, METHOD_PATTERN);
        System.out.println("DEBUG: Extracted method: " + method);

        Map<String, String> headers = extractHeaders(curlCommand);
        System.out.println("DEBUG: Extracted headers: " + headers);

        String requestBody = extractData(curlCommand);
        System.out.println("DEBUG: Extracted request body: " + requestBody);

        if (url == null) {
            throw new IllegalArgumentException("URL not found in curl command.");
        }

        if (method == null) {
            // Default to GET if -X is not specified and no data is present
            // Default to POST if data is present and -X is not specified
            if (requestBody != null && !requestBody.isEmpty()) {
                method = "POST";
            } else {
                method = "GET";
            }
        }

        String testName = generateTestName(url, method);
        System.out.println("DEBUG: Generated test name: " + testName);

        return new ApiRequest(url, method, headers, requestBody, responseBody, testName);
    }

    private static String extractData(String command) {
        Matcher matcher = DATA_PATTERN.matcher(command);
        if (matcher.find()) {
            String data = matcher.group(1);
            // Clean up any escaped quotes
            return data.replace("\\'", "'").replace("\\\"", "\"");
        }
        return null;
    }

    private static Map<String, String> extractHeaders(String command) {
        Map<String, String> headers = new HashMap<>();
        Pattern headerPattern = Pattern.compile("-H\\s+['\"]([^'\"]+)['\"]");
        Matcher matcher = headerPattern.matcher(command);
        while (matcher.find()) {
            String header = matcher.group(1);
            String[] parts = header.split(":\\s*", 2);
            if (parts.length == 2) {
                headers.put(parts[0], parts[1]);
            }
        }
        return headers;
    }

    private static String extract(String command, Pattern pattern) {
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String generateTestName(String url, String method) {
        // Basic sanitization and naming
        String path = url.replaceAll("^https?://[^/]+/", "").replaceAll("[^a-zA-Z0-9_/]", "");
        if (path.isEmpty()) {
            path = "root";
        }
        // Convert path segments to CamelCase for better class names
        String[] segments = path.split("/");
        StringBuilder nameBuilder = new StringBuilder();
        for (String segment : segments) {
            if (!segment.isEmpty()) {
                nameBuilder.append(Character.toUpperCase(segment.charAt(0)))
                           .append(segment.substring(1));
            }
        }
        return method.toUpperCase() + nameBuilder.toString();
    }
}
