package com.automation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration Manager to handle all configuration properties
 * This class follows Singleton pattern to ensure single instance
 */
public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private Properties properties;
    
    private static final String DEFAULT_CONFIG_PATH = "src/test/resources/config/application.properties";
    
    private ConfigManager() {
        loadProperties();
    }
    
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }
    
    private void loadProperties() {
        properties = new Properties();
        String configPath = System.getProperty("config.path", DEFAULT_CONFIG_PATH);
        
        try (FileInputStream fis = new FileInputStream(configPath)) {
            properties.load(fis);
            logger.info("Configuration loaded from: {}", configPath);
        } catch (IOException e) {
            logger.error("Failed to load configuration from: {}", configPath, e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
    
    public String getProperty(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }
    
    public String getProperty(String key, String defaultValue) {
        return System.getProperty(key, properties.getProperty(key, defaultValue));
    }
    
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for key: {}, using default: {}", key, defaultValue);
            return defaultValue;
        }
    }
    
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    // Environment Configuration
    public String getEnvironment() {
        return getProperty("environment", "qa");
    }
    
    public String getBaseUrl() {
        return getProperty("base.url");
    }
    
    public int getTimeout() {
        return getIntProperty("timeout", 30000);
    }
    
    // Database Configuration
    public String getDbType() {
        return getProperty("db.type", "mysql");
    }
    
    public String getDbHost() {
        return getProperty("db.host", "localhost");
    }
    
    public int getDbPort() {
        return getIntProperty("db.port", 3306);
    }
    
    public String getDbName() {
        return getProperty("db.name");
    }
    
    public String getDbUsername() {
        return getProperty("db.username");
    }
    
    public String getDbPassword() {
        return getProperty("db.password");
    }
    
    public int getDbConnectionPoolSize() {
        return getIntProperty("db.connection.pool.size", 10);
    }
    
    // Test Data Configuration
    public String getTestDataPath() {
        return getProperty("test.data.path", "src/test/resources/testdata");
    }
    
    public String getLocalStoragePath() {
        return getProperty("local.storage.path", "test-output");
    }
    
    // API Configuration
    public String getApiVersion() {
        return getProperty("api.version", "v1");
    }
    
    public String getContentType() {
        return getProperty("content.type", "application/json");
    }
    
    public String getAcceptHeader() {
        return getProperty("accept.header", "application/json");
    }
    
    // Retry Configuration
    public int getRetryCount() {
        return getIntProperty("retry.count", 3);
    }
    
    public int getRetryDelay() {
        return getIntProperty("retry.delay", 1000);
    }
    
    // Report Configuration
    public String getReportPath() {
        return getProperty("report.path", "test-reports");
    }
    
    public String getScreenshotPath() {
        return getProperty("screenshot.path", "screenshots");
    }
    
    // Logging Configuration
    public String getLogLevel() {
        return getProperty("log.level", "INFO");
    }
    
    public String getLogFilePath() {
        return getProperty("log.file.path", "logs/test.log");
    }
    
    // Thread Configuration
    public boolean isParallelExecution() {
        return getBooleanProperty("parallel.execution", true);
    }
    
    public int getThreadCount() {
        return getIntProperty("thread.count", 5);
    }
}
