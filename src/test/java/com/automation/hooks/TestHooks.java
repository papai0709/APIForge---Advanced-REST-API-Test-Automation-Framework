package com.automation.hooks;

import com.automation.config.ConfigManager;
import com.automation.database.DatabaseManager;
import com.automation.utils.FileManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber hooks for setup and teardown operations
 */
public class TestHooks {
    
    private static final Logger logger = LoggerFactory.getLogger(TestHooks.class);
    
    private final ConfigManager config = ConfigManager.getInstance();
    private final DatabaseManager dbManager = DatabaseManager.getInstance();
    private final FileManager fileManager = new FileManager();

    @Before
    public void setUp(Scenario scenario) {
        logger.info("========== Starting Scenario: {} ==========", scenario.getName());
        
        try {
            // Setup test directories
            fileManager.createDirectory(config.getLocalStoragePath());
            fileManager.createDirectory(config.getReportPath());

            // Connect to database if needed
            if (scenario.getSourceTagNames().contains("@database")) {
                if (!dbManager.isConnected()) {
                    dbManager.connect();
                    logger.info("Database connection established for scenario: {}", scenario.getName());
                }
            }

            // Log scenario tags
            logger.info("Scenario tags: {}", scenario.getSourceTagNames());
            logger.debug("Starting test execution for scenario: {}", scenario.getName());
        } catch (Exception e) {
            logger.error("Error during test setup for scenario: {}", scenario.getName(), e);
            throw new RuntimeException("Test setup failed: " + e.getMessage(), e);
        }
    }
    
    @After
    public void tearDown(Scenario scenario) {
        try {
            logger.info("========== Completing Scenario: {} ==========", scenario.getName());

            // Save scenario results
            String scenarioResult = String.format(
                "Scenario: %s%nStatus: %s%nTags: %s%nTimestamp: %s%n",
                scenario.getName(),
                scenario.getStatus().name(),
                scenario.getSourceTagNames(),
                java.time.LocalDateTime.now()
            );

            String fileName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_");
            fileManager.saveTestResults(fileName, scenarioResult);
            logger.debug("Test results saved for scenario: {}", scenario.getName());

            // Disconnect from database
            if (dbManager.isConnected()) {
                try {
                    dbManager.disconnect();
                    logger.info("Database connection closed");
                } catch (Exception e) {
                    logger.warn("Error while disconnecting database: {}", e.getMessage());
                }
            }

            // Clean up old files (older than 7 days)
            fileManager.cleanupOldFiles(config.getLocalStoragePath(), 7);

            logger.info("Scenario status: {} - Duration: {}ms",
                scenario.getStatus(),
                System.currentTimeMillis());
        } catch (Exception e) {
            logger.error("Error during test teardown for scenario: {}", scenario.getName(), e);
        }
    }
    
    @Before("@smoke")
    public void setUpSmokeTest() {
        logger.info("Setting up smoke test - Running quick validation tests");
        // Additional setup for smoke tests if needed
    }
    
    @After("@smoke")
    public void tearDownSmokeTest(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.warn("Smoke test failed: {}", scenario.getName());
        } else {
            logger.info("Smoke test passed: {}", scenario.getName());
        }
    }

    @Before("@regression")
    public void setUpRegressionTest() {
        logger.info("Setting up regression test - Running comprehensive validation");
        // Additional setup for regression tests if needed
    }
    
    @After("@regression")
    public void tearDownRegressionTest(Scenario scenario) {
        logger.info("Regression test completed: {} - Status: {}",
            scenario.getName(), scenario.getStatus());
    }

    @Before("@performance")
    public void setUpPerformanceTest() {
        logger.info("Setting up performance test - Initializing performance monitoring");
        // Additional setup for performance tests if needed
    }

    @After("@performance")
    public void tearDownPerformanceTest(Scenario scenario) {
        logger.info("Performance test completed: {} - Status: {}",
            scenario.getName(), scenario.getStatus());
    }
}
