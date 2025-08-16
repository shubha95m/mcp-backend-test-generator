package com.mcp.backend.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    public static void writeFile(String directoryPath, String fileName, String content) throws IOException {
        try {
            Path dirPath = Paths.get(directoryPath);
            if (!Files.exists(dirPath)) {
                System.out.println("DEBUG: Attempting to create directory: " + dirPath);
                Files.createDirectories(dirPath);
                System.out.println("DEBUG: Directory created successfully: " + dirPath);
            }
            Path filePath = Paths.get(directoryPath, fileName);
            System.out.println("DEBUG: Attempting to write file: " + filePath);
            Files.write(filePath, content.getBytes());
            System.out.println("DEBUG: File written successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("ERROR: Failed to write file: " + e.getMessage());
            throw e;
        }
    }
}
