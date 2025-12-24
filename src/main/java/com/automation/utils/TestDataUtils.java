package com.automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Test data utility class for generating and manipulating test data
 */
public class TestDataUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(TestDataUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Random random = new Random();
    
    /**
     * Generate random email address
     */
    public static String generateRandomEmail() {
        String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "email.com", "test.com"};
        String username = "user" + System.currentTimeMillis() + random.nextInt(1000);
        String domain = domains[random.nextInt(domains.length)];
        return username + "@" + domain;
    }
    
    /**
     * Generate random username
     */
    public static String generateRandomUsername() {
        String[] prefixes = {"user", "test", "demo", "sample", "auto"};
        String prefix = prefixes[random.nextInt(prefixes.length)];
        return prefix + System.currentTimeMillis() + random.nextInt(1000);
    }
    
    /**
     * Generate random name
     */
    public static String generateRandomName() {
        String[] firstNames = {"John", "Jane", "Bob", "Alice", "Charlie", "Diana", "Eve", "Frank"};
        String[] lastNames = {"Doe", "Smith", "Johnson", "Brown", "Davis", "Miller", "Wilson", "Moore"};
        
        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];
        
        return firstName + " " + lastName;
    }
    
    /**
     * Generate random phone number
     */
    public static String generateRandomPhoneNumber() {
        return String.format("%d-%d-%d", 
            random.nextInt(900) + 100,
            random.nextInt(900) + 100,
            random.nextInt(9000) + 1000);
    }
    
    /**
     * Generate timestamp string
     */
    public static String generateTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    /**
     * Generate unique ID
     */
    public static long generateUniqueId() {
        return System.currentTimeMillis() + random.nextInt(1000);
    }
    
    /**
     * Convert object to JSON string
     */
    public static String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.error("Failed to convert object to JSON: {}", e.getMessage());
            throw new RuntimeException("JSON conversion failed", e);
        }
    }
    
    /**
     * Convert JSON string to object
     */
    public static <T> T fromJsonString(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.error("Failed to convert JSON to object: {}", e.getMessage());
            throw new RuntimeException("JSON parsing failed", e);
        }
    }
    
    /**
     * Pretty print JSON string
     */
    public static String prettyPrintJson(String json) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (IOException e) {
            logger.error("Failed to pretty print JSON: {}", e.getMessage());
            return json;
        }
    }
    
    /**
     * Generate random string of specified length
     */
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        
        return sb.toString();
    }
    
    /**
     * Generate random integer within range
     */
    public static int generateRandomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    
    /**
     * Generate random boolean
     */
    public static boolean generateRandomBoolean() {
        return random.nextBoolean();
    }
    
    /**
     * Mask sensitive data in strings (for logging)
     */
    public static String maskSensitiveData(String data) {
        if (data == null || data.length() <= 4) {
            return "****";
        }
        
        int visibleChars = 2;
        String start = data.substring(0, visibleChars);
        String end = data.substring(data.length() - visibleChars);
        String masked = "*".repeat(data.length() - (visibleChars * 2));
        
        return start + masked + end;
    }
    
    /**
     * Generate test data file name with timestamp
     */
    public static String generateTestDataFileName(String baseName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return String.format("%s_testdata_%s.json", baseName, timestamp);
    }
}
