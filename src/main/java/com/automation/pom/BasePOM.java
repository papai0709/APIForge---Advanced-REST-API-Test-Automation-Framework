package com.automation.pom;

import com.automation.base.BaseTest;
import com.automation.config.ConfigManager;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Base Page Object Model class for API endpoints
 * Contains common methods for API operations
 */
public abstract class BasePOM extends BaseTest {
    
    protected ConfigManager config = ConfigManager.getInstance();
    protected String baseEndpoint;
    
    public BasePOM(String baseEndpoint) {
        this.baseEndpoint = baseEndpoint;
        setupRestAssured();
    }
    
    /**
     * Get request specification with common headers and configuration
     */
    protected RequestSpecification getRequestSpec() {
        return given()
                .spec(requestSpec)
                .header("Content-Type", config.getContentType())
                .header("Accept", config.getAcceptHeader());
    }
    
    /**
     * Build full endpoint URL
     */
    protected String buildEndpoint(String... pathSegments) {
        StringBuilder endpoint = new StringBuilder(baseEndpoint);
        for (String segment : pathSegments) {
            if (!endpoint.toString().endsWith("/") && !segment.startsWith("/")) {
                endpoint.append("/");
            }
            endpoint.append(segment);
        }
        return endpoint.toString();
    }
    
    /**
     * Perform GET request
     */
    public Response performGet(String endpoint) {
        logStep("Performing GET request to: " + endpoint);
        return getRequestSpec()
                .when()
                .get(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * Perform GET request with path parameters
     */
    public Response performGet(String endpoint, Object... pathParams) {
        logStep("Performing GET request to: " + endpoint + " with path params");
        return getRequestSpec()
                .pathParams(createPathParamsMap(pathParams))
                .when()
                .get(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * Perform POST request with body
     */
    protected Response performPost(String endpoint, Object body) {
        logStep("Performing POST request to: " + endpoint);
        return getRequestSpec()
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * Perform PUT request with body
     */
    protected Response performPut(String endpoint, Object body) {
        logStep("Performing PUT request to: " + endpoint);
        return getRequestSpec()
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * Perform DELETE request
     */
    protected Response performDelete(String endpoint) {
        logStep("Performing DELETE request to: " + endpoint);
        return getRequestSpec()
                .when()
                .delete(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * Perform PATCH request with body
     */
    protected Response performPatch(String endpoint, Object body) {
        logStep("Performing PATCH request to: " + endpoint);
        return getRequestSpec()
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * Create path parameters map from varargs
     */
    private java.util.Map<String, Object> createPathParamsMap(Object... params) {
        java.util.Map<String, Object> pathParams = new java.util.HashMap<>();
        for (int i = 0; i < params.length; i += 2) {
            if (i + 1 < params.length) {
                pathParams.put(params[i].toString(), params[i + 1]);
            }
        }
        return pathParams;
    }
    
    /**
     * Validate response status code
     */
    protected void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        if (actualStatusCode != expectedStatusCode) {
            throw new AssertionError(
                String.format("Expected status code: %d, but got: %d. Response: %s", 
                    expectedStatusCode, actualStatusCode, response.asString())
            );
        }
        logger.info("Status code validation passed: {}", expectedStatusCode);
    }
    
    /**
     * Extract response as specific type
     */
    public <T> T extractResponse(Response response, Class<T> responseType) {
        try {
            return response.as(responseType);
        } catch (Exception e) {
            logger.error("Failed to extract response as {}: {}", responseType.getSimpleName(), e.getMessage());
            throw new RuntimeException("Response extraction failed", e);
        }
    }
}
