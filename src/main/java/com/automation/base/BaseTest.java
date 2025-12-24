package com.automation.base;

import com.automation.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all API tests
 * Contains common setup and configuration for REST Assured
 */
public class BaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected ConfigManager config = ConfigManager.getInstance();
    
    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;
    
    public void setupRestAssured() {
        // Set base URI
        RestAssured.baseURI = config.getBaseUrl();
        
        // Build request specification
        RequestSpecBuilder requestBuilder = new RequestSpecBuilder();
        requestBuilder.setContentType(ContentType.JSON);
        requestBuilder.addHeader("Accept", config.getAcceptHeader());
        requestBuilder.log(LogDetail.ALL);
        
        requestSpec = requestBuilder.build();
        
        // Build response specification
        ResponseSpecBuilder responseBuilder = new ResponseSpecBuilder();
        responseBuilder.log(LogDetail.ALL);
        
        responseSpec = responseBuilder.build();
        
        // Set default specifications
        RestAssured.requestSpecification = requestSpec;
        RestAssured.responseSpecification = responseSpec;
        
        logger.info("RestAssured configured with base URI: {}", config.getBaseUrl());
    }
    
    public void resetRestAssured() {
        RestAssured.reset();
        logger.info("RestAssured configuration reset");
    }
    
    protected void logTestStart(String testName) {
        logger.info("========== Starting Test: {} ==========", testName);
    }
    
    protected void logTestEnd(String testName) {
        logger.info("========== Test Completed: {} ==========", testName);
    }
    
    protected void logStep(String step) {
        logger.info("STEP: {}", step);
    }
}
