package com.mcp.backend.generator;

public class PomGenerator {

    public static String generatePom(String projectName) {
        StringBuilder pomContent = new StringBuilder();
        pomContent.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        pomContent.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n");
        pomContent.append("         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        pomContent.append("         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n");
        pomContent.append("    <modelVersion>4.0.0</modelVersion>\n\n");

        pomContent.append("    <groupId>com.mcp.generated</groupId>\n");
        pomContent.append("    <artifactId>" + projectName.toLowerCase() + "</artifactId>\n");
        pomContent.append("    <version>1.0-SNAPSHOT</version>\n\n");

        pomContent.append("    <properties>\n");
        pomContent.append("        <maven.compiler.source>11</maven.compiler.source>\n");
        pomContent.append("        <maven.compiler.target>11</maven.compiler.target>\n");
        pomContent.append("        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n");
        pomContent.append("        <restassured.version>5.3.0</restassured.version>\n");
        pomContent.append("        <jackson.version>2.15.2</jackson.version>\n");
        pomContent.append("    </properties>\n\n");

        pomContent.append("    <dependencies>\n");
        pomContent.append("        <!-- RestAssured for API testing -->\n");
        pomContent.append("        <dependency>\n");
        pomContent.append("            <groupId>io.rest-assured</groupId>\n");
        pomContent.append("            <artifactId>rest-assured</artifactId>\n");
        pomContent.append("            <version>${restassured.version}</version>\n");
        pomContent.append("        </dependency>\n");
        pomContent.append("        <dependency>\n");
        pomContent.append("            <groupId>io.rest-assured</groupId>\n");
        pomContent.append("            <artifactId>json-path</artifactId>\n");
        pomContent.append("            <version>${restassured.version}</version>\n");
        pomContent.append("        </dependency>\n");
        pomContent.append("        <dependency>\n");
        pomContent.append("            <groupId>io.rest-assured</groupId>\n");
        pomContent.append("            <artifactId>xml-path</artifactId>\n");
        pomContent.append("            <version>${restassured.version}</version>\n");
        pomContent.append("        </dependency>\n");
        pomContent.append("        <!-- Jackson for DTOs -->\n");
        pomContent.append("        <dependency>\n");
        pomContent.append("            <groupId>com.fasterxml.jackson.core</groupId>\n");
        pomContent.append("            <artifactId>jackson-databind</artifactId>\n");
        pomContent.append("            <version>${jackson.version}</version>\n");
        pomContent.append("        </dependency>\n");
        pomContent.append("        <dependency>\n");
        pomContent.append("            <groupId>com.fasterxml.jackson.core</groupId>\n");
        pomContent.append("            <artifactId>jackson-annotations</artifactId>\n");
        pomContent.append("            <version>${jackson.version}</version>\n");
        pomContent.append("        </dependency>\n");
        pomContent.append("        <dependency>\n");
        pomContent.append("            <groupId>com.fasterxml.jackson.core</groupId>\n");
        pomContent.append("            <artifactId>jackson-core</artifactId>\n");
        pomContent.append("            <version>${jackson.version}</version>\n");
        pomContent.append("        </dependency>\n");
        pomContent.append("        <!-- TestNG for tests -->\n");
        pomContent.append("        <dependency>\n");
        pomContent.append("            <groupId>org.testng</groupId>\n");
        pomContent.append("            <artifactId>testng</artifactId>\n");
        pomContent.append("            <version>7.10.2</version>\n");
        pomContent.append("            <scope>test</scope>\n");
        pomContent.append("        </dependency>\n");
        pomContent.append("        <!-- AssertJ for assertions -->\n");
        pomContent.append("        <dependency>\n");
        pomContent.append("            <groupId>org.assertj</groupId>\n");
        pomContent.append("            <artifactId>assertj-core</artifactId>\n");
        pomContent.append("            <version>3.25.3</version>\n");
        pomContent.append("            <scope>test</scope>\n");
        pomContent.append("        </dependency>\n");
        pomContent.append("    </dependencies>\n");
        pomContent.append("    <build>\n");
        pomContent.append("        <plugins>\n");
        pomContent.append("            <plugin>\n");
        pomContent.append("                <groupId>org.apache.maven.plugins</groupId>\n");
        pomContent.append("                <artifactId>maven-compiler-plugin</artifactId>\n");
        pomContent.append("                <version>3.11.0</version>\n");
        pomContent.append("                <configuration>\n");
        pomContent.append("                    <source>${maven.compiler.source}</source>\n");
        pomContent.append("                    <target>${maven.compiler.target}</target>\n");
        pomContent.append("                </configuration>\n");
        pomContent.append("            </plugin>\n");
        pomContent.append("            <plugin>\n");
        pomContent.append("                <groupId>org.apache.maven.plugins</groupId>\n");
        pomContent.append("                <artifactId>maven-surefire-plugin</artifactId>\n");
        pomContent.append("                <version>3.0.0-M7</version>\n");
        pomContent.append("                <configuration>\n");
        pomContent.append("                    <suiteXmlFiles>\n");
        pomContent.append("                        <suiteXmlFile>testng.xml</suiteXmlFile>\n");
        pomContent.append("                    </suiteXmlFiles>\n");
        pomContent.append("                </configuration>\n");
        pomContent.append("            </plugin>\n");
        pomContent.append("        </plugins>\n");
        pomContent.append("    </build>\n");
        pomContent.append("</project>\n");
        return pomContent.toString();
    }
}
