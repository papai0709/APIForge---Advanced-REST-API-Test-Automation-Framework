package com.automation.utils;

import com.automation.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * File Manager utility for handling local storage operations
 * Manages test data files, reports, and other file operations
 */
public class FileManager {
    private static final Logger logger = LoggerFactory.getLogger(FileManager.class);
    private ConfigManager config = ConfigManager.getInstance();
    
    /**
     * Create directory if it doesn't exist
     */
    public void createDirectory(String directoryPath) {
        try {
            Path path = Paths.get(directoryPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                logger.info("Directory created: {}", directoryPath);
            }
        } catch (IOException e) {
            logger.error("Failed to create directory: {}", directoryPath, e);
            throw new RuntimeException("Directory creation failed", e);
        }
    }
    
    /**
     * Read file content as string
     */
    public String readFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            String content = Files.readString(path);
            logger.info("File read successfully: {}", filePath);
            return content;
        } catch (IOException e) {
            logger.error("Failed to read file: {}", filePath, e);
            throw new RuntimeException("File read failed", e);
        }
    }
    
    /**
     * Write content to file
     */
    public void writeFile(String filePath, String content) {
        try {
            Path path = Paths.get(filePath);
            createDirectory(path.getParent().toString());
            Files.writeString(path, content);
            logger.info("File written successfully: {}", filePath);
        } catch (IOException e) {
            logger.error("Failed to write file: {}", filePath, e);
            throw new RuntimeException("File write failed", e);
        }
    }
    
    /**
     * Append content to file
     */
    public void appendToFile(String filePath, String content) {
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(content);
            bw.newLine();
            logger.info("Content appended to file: {}", filePath);
        } catch (IOException e) {
            logger.error("Failed to append to file: {}", filePath, e);
            throw new RuntimeException("File append failed", e);
        }
    }
    
    /**
     * Copy file from source to destination
     */
    public void copyFile(String sourcePath, String destinationPath) {
        try {
            Path source = Paths.get(sourcePath);
            Path destination = Paths.get(destinationPath);
            createDirectory(destination.getParent().toString());
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File copied from {} to {}", sourcePath, destinationPath);
        } catch (IOException e) {
            logger.error("Failed to copy file from {} to {}", sourcePath, destinationPath, e);
            throw new RuntimeException("File copy failed", e);
        }
    }
    
    /**
     * Delete file
     */
    public void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
                logger.info("File deleted: {}", filePath);
            }
        } catch (IOException e) {
            logger.error("Failed to delete file: {}", filePath, e);
            throw new RuntimeException("File deletion failed", e);
        }
    }
    
    /**
     * Check if file exists
     */
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
    
    /**
     * Get file size in bytes
     */
    public long getFileSize(String filePath) {
        try {
            return Files.size(Paths.get(filePath));
        } catch (IOException e) {
            logger.error("Failed to get file size: {}", filePath, e);
            return -1;
        }
    }
    
    /**
     * Create timestamped file name
     */
    public String createTimestampedFileName(String baseName, String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return String.format("%s_%s.%s", baseName, timestamp, extension);
    }
    
    /**
     * Get local storage path from configuration
     */
    public String getLocalStoragePath() {
        String path = config.getLocalStoragePath();
        createDirectory(path);
        return path;
    }
    
    /**
     * Get test data path from configuration
     */
    public String getTestDataPath() {
        return config.getTestDataPath();
    }
    
    /**
     * Save test results to local storage
     */
    public void saveTestResults(String testName, String results) {
        String fileName = createTimestampedFileName(testName + "_results", "json");
        String filePath = getLocalStoragePath() + File.separator + fileName;
        writeFile(filePath, results);
    }
    
    /**
     * Save API response to local storage
     */
    public void saveApiResponse(String endpointName, String response) {
        String fileName = createTimestampedFileName(endpointName + "_response", "json");
        String filePath = getLocalStoragePath() + File.separator + "responses" + File.separator + fileName;
        writeFile(filePath, response);
    }
    
    /**
     * Clean up old files in directory (older than specified days)
     */
    public void cleanupOldFiles(String directoryPath, int daysOld) {
        try {
            Path dir = Paths.get(directoryPath);
            if (!Files.exists(dir)) return;
            
            long cutoffTime = System.currentTimeMillis() - (daysOld * 24L * 60 * 60 * 1000);
            
            Files.list(dir)
                .filter(Files::isRegularFile)
                .filter(path -> {
                    try {
                        return Files.getLastModifiedTime(path).toMillis() < cutoffTime;
                    } catch (IOException e) {
                        return false;
                    }
                })
                .forEach(path -> {
                    try {
                        Files.delete(path);
                        logger.info("Deleted old file: {}", path);
                    } catch (IOException e) {
                        logger.error("Failed to delete old file: {}", path, e);
                    }
                });
                
        } catch (IOException e) {
            logger.error("Failed to cleanup old files in: {}", directoryPath, e);
        }
    }
}
