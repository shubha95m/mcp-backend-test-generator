package com.mcp.backend.generator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;
import java.util.Map;

public class DtoGenerator {

    public static String generateDto(String className, String jsonString, String packageName) {
        StringBuilder sb = new StringBuilder();
        sb.append("package " + packageName + ";\n\n");
        sb.append("import com.fasterxml.jackson.annotation.JsonProperty;\n");
        sb.append("\n");
        sb.append("public class " + className + " {\n\n");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonString);

            if (rootNode.isObject()) {
                ObjectNode objectNode = (ObjectNode) rootNode;
                Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> field = fields.next();
                    String fieldName = field.getKey();
                    JsonNode valueNode = field.getValue();

                    String javaType = getJavaType(valueNode);
                    sb.append("    @JsonProperty(\"" + fieldName + "\")\n");
                    sb.append("    private " + javaType + " " + toCamelCase(fieldName) + ";\n");
                }

                sb.append("\n");

                // Generate getters and setters
                fields = objectNode.fields(); // Reset iterator
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> field = fields.next();
                    String fieldName = field.getKey();
                    String javaType = getJavaType(field.getValue());
                    String camelCaseFieldName = toCamelCase(fieldName);
                    String capitalizedFieldName = capitalize(camelCaseFieldName);

                    // Getter
                    sb.append("    public " + javaType + " get" + capitalizedFieldName + "() {\n");
                    sb.append("        return " + camelCaseFieldName + ";\n");
                    sb.append("    }\n\n");

                    // Setter
                    sb.append("    public void set" + capitalizedFieldName + "(" + javaType + " " + camelCaseFieldName + ") {\n");
                    sb.append("        this." + camelCaseFieldName + " = " + camelCaseFieldName + ";\n");
                    sb.append("    }\n\n");
                }

            } else if (rootNode.isArray()) {
                sb.append("    // This DTO represents a JSON array. Consider creating a DTO for the array elements.\n");
                sb.append("    // For example, if the array contains objects, create a separate DTO for that object\n");
                sb.append("    // and use List<YourObjectDTO> here.\n");
                sb.append("    private String rawJsonArray; // Representing as raw string for simplicity\n");
                sb.append("\n");
                sb.append("    public String getRawJsonArray() {\n");
                sb.append("        return rawJsonArray;\n");
                sb.append("    }\n\n");
                sb.append("    public void setRawJsonArray(String rawJsonArray) {\n");
                sb.append("        this.rawJsonArray = rawJsonArray;\n");
                sb.append("    }\n\n");
            } else {
                sb.append("    // This DTO represents a primitive JSON value (e.g., string, number, boolean).\n");
                sb.append("    private String rawJsonValue; // Representing as raw string for simplicity\n");
                sb.append("\n");
                sb.append("    public String getRawJsonValue() {\n");
                sb.append("        return rawJsonValue;\n");
                sb.append("    }\n\n");
                sb.append("    public void setRawJsonValue(String rawJsonValue) {\n");
                sb.append("        this.rawJsonValue = rawJsonValue;\n");
                sb.append("    }\n\n");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate DTO for " + className + ": " + e.getMessage(), e);
        }

        sb.append("}\n");
        return sb.toString();
    }

    private static String getJavaType(JsonNode node) {
        if (node.isTextual()) {
            return "String";
        } else if (node.isBoolean()) {
            return "boolean";
        } else if (node.isIntegralNumber()) {
            return "int";
        } else if (node.isFloatingPointNumber()) {
            return "double";
        } else if (node.isArray()) {
            // For simplicity, we'll return List<Object> or infer more specifically later
            if (node instanceof ArrayNode && node.size() > 0 && node.get(0).isObject()) {
                // If array contains objects, we might want a specific DTO for it
                return "java.util.List<Object>"; // Placeholder
            }
            return "java.util.List<String>"; // Default to List<String> or primitive list
        } else if (node.isObject()) {
            return "Object"; // Or a specific nested DTO class name
        } else if (node.isNull()) {
            return "Object"; // Can be anything, so Object or String
        } else {
            return "String"; // Fallback
        }
    }

    private static String toCamelCase(String snakeCase) {
        StringBuilder camelCaseString = new StringBuilder();
        boolean capitalizeNext = false;
        for (int i = 0; i < snakeCase.length(); i++) {
            char c = snakeCase.charAt(i);
            if (c == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    camelCaseString.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    camelCaseString.append(c);
                }
            }
        }
        return camelCaseString.toString();
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
