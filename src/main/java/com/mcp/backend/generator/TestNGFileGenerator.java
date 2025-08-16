package com.mcp.backend.generator;

public class TestNGFileGenerator {
    public static String generateTestNGXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<!DOCTYPE suite SYSTEM \"https://testng.org/testng-1.0.dtd\">\n");
        sb.append("<suite name=\"API Test Suite\">\n");
        sb.append("    <test name=\"API Tests\">\n");
        sb.append("        <classes>\n");
        sb.append("            <class name=\"com.mcp.generated.test.Tests\"/>\n");
        sb.append("        </classes>\n");
        sb.append("    </test>\n");
        sb.append("</suite>\n");
        return sb.toString();
    }
}
