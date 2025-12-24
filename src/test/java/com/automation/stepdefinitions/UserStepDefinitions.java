package com.automation.stepdefinitions;

import com.automation.database.DatabaseManager;
import com.automation.pojo.User;
import com.automation.pom.UserPOM;
import com.automation.utils.FileManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for User API scenarios
 */
public class UserStepDefinitions {
    
    private static final Logger logger = LoggerFactory.getLogger(UserStepDefinitions.class);
    
    private UserPOM userPOM = new UserPOM();
    private DatabaseManager dbManager = DatabaseManager.getInstance();
    private FileManager fileManager = new FileManager();
    
    private Response response;
    private User user;
    private User createdUser;
    
    @Given("the API base URL is configured")
    public void the_api_base_url_is_configured() {
        userPOM.setupRestAssured();
        logger.info("API base URL configured");
    }
    
    @Given("the request headers are set")
    public void the_request_headers_are_set() {
        // Headers are set in BasePOM
        logger.info("Request headers are set");
    }
    
    @Given("a user exists with ID {int}")
    public void a_user_exists_with_id(int userId) {
        // Verify user exists by making GET request
        response = userPOM.getUserById(userId);
        assertThat(response.getStatusCode()).isEqualTo(200);
        logger.info("Verified user exists with ID: {}", userId);
    }
    
    @Given("I have user data:")
    public void i_have_user_data(DataTable dataTable) {
        List<Map<String, String>> userData = dataTable.asMaps(String.class, String.class);
        Map<String, String> userMap = userData.get(0);
        
        user = new User();
        user.setName(userMap.get("name"));
        user.setEmail(userMap.get("email"));
        user.setUsername(userMap.get("username"));
        
        logger.info("User data prepared: {}", user);
    }
    
    @Given("I have user data with name {string}, email {string}, and username {string}")
    public void i_have_user_data_with_details(String name, String email, String username) {
        user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setUsername(username);
        
        logger.info("User data prepared: {} - {} - {}", name, email, username);
    }
    
    @Given("I have updated user data:")
    public void i_have_updated_user_data(DataTable dataTable) {
        List<Map<String, String>> userData = dataTable.asMaps(String.class, String.class);
        Map<String, String> userMap = userData.get(0);
        
        user = new User();
        user.setName(userMap.get("name"));
        user.setEmail(userMap.get("email"));
        user.setUsername(userMap.get("username"));
        
        logger.info("Updated user data prepared: {}", user);
    }
    
    @Given("a user is created through API")
    public void a_user_is_created_through_api() {
        user = new User("Test User", "test@email.com", "testuser");
        response = userPOM.createUser(user);
        createdUser = userPOM.extractResponse(response, User.class);
        logger.info("User created through API: {}", createdUser);
    }
    
    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        if (endpoint.contains("{") || endpoint.matches(".*users/\\d+.*")) {
            // Extract user ID if present
            String[] parts = endpoint.split("/");
            if (parts.length > 2 && parts[2].matches("\\d+")) {
                long userId = Long.parseLong(parts[2]);
                response = userPOM.getUserById(userId);
            } else {
                response = userPOM.performGet(endpoint);
            }
        } else {
            response = userPOM.performGet(endpoint);
        }
        
        // Save response for debugging
        fileManager.saveApiResponse("GET_" + endpoint.replace("/", "_"), response.asString());
        logger.info("GET request sent to: {}", endpoint);
    }
    
    @When("I send a POST request to {string} with the user data")
    public void i_send_a_post_request_with_user_data(String endpoint) {
        response = userPOM.createUser(user);
        createdUser = userPOM.extractResponse(response, User.class);
        
        // Save response for debugging
        fileManager.saveApiResponse("POST_" + endpoint.replace("/", "_"), response.asString());
        logger.info("POST request sent to: {} with user data", endpoint);
    }
    
    @When("I send a PUT request to {string} with the updated user data")
    public void i_send_a_put_request_with_updated_user_data(String endpoint) {
        // Extract user ID from endpoint
        String[] parts = endpoint.split("/");
        long userId = Long.parseLong(parts[parts.length - 1]);
        
        response = userPOM.updateUser(userId, user);
        
        // Save response for debugging
        fileManager.saveApiResponse("PUT_" + endpoint.replace("/", "_"), response.asString());
        logger.info("PUT request sent to: {} with updated user data", endpoint);
    }
    
    @When("I send a PATCH request to {string} with partial data:")
    public void i_send_a_patch_request_with_partial_data(String endpoint, DataTable dataTable) {
        List<Map<String, String>> partialData = dataTable.asMaps(String.class, String.class);
        Map<String, String> dataMap = partialData.get(0);
        
        // Extract user ID from endpoint
        String[] parts = endpoint.split("/");
        long userId = Long.parseLong(parts[parts.length - 1]);
        
        User partialUser = new User();
        if (dataMap.containsKey("name")) {
            partialUser.setName(dataMap.get("name"));
        }
        if (dataMap.containsKey("email")) {
            partialUser.setEmail(dataMap.get("email"));
        }
        if (dataMap.containsKey("username")) {
            partialUser.setUsername(dataMap.get("username"));
        }
        
        response = userPOM.patchUser(userId, partialUser);
        
        // Save response for debugging
        fileManager.saveApiResponse("PATCH_" + endpoint.replace("/", "_"), response.asString());
        logger.info("PATCH request sent to: {} with partial data", endpoint);
    }
    
    @When("I send a DELETE request to {string}")
    public void i_send_a_delete_request_to(String endpoint) {
        // Extract user ID from endpoint
        String[] parts = endpoint.split("/");
        long userId = Long.parseLong(parts[parts.length - 1]);
        
        response = userPOM.deleteUser(userId);
        
        // Save response for debugging
        fileManager.saveApiResponse("DELETE_" + endpoint.replace("/", "_"), response.asString());
        logger.info("DELETE request sent to: {}", endpoint);
    }
    
    @When("I query the database for the user")
    public void i_query_the_database_for_the_user() {
        if (createdUser != null && createdUser.getId() != null) {
            dbManager.connect();
            boolean exists = dbManager.recordExists("users", "id = ?", createdUser.getId());
            assertThat(exists).isTrue();
            logger.info("Queried database for user ID: {}", createdUser.getId());
        }
    }
    
    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int expectedStatusCode) {
        assertThat(response.getStatusCode()).isEqualTo(expectedStatusCode);
        logger.info("Response status code validation passed: {}", expectedStatusCode);
    }
    
    @Then("the response should contain a list of users")
    public void the_response_should_contain_a_list_of_users() {
        List<User> users = response.jsonPath().getList("", User.class);
        assertThat(users).isNotEmpty();
        logger.info("Response contains {} users", users.size());
    }
    
    @Then("each user should have required fields")
    public void each_user_should_have_required_fields() {
        List<User> users = response.jsonPath().getList("", User.class);
        for (User user : users) {
            assertThat(user.getId()).isNotNull();
            assertThat(user.getName()).isNotBlank();
            assertThat(user.getEmail()).isNotBlank();
            assertThat(user.getUsername()).isNotBlank();
        }
        logger.info("All users have required fields");
    }
    
    @Then("the response should contain user details")
    public void the_response_should_contain_user_details() {
        User responseUser = response.as(User.class);
        assertThat(responseUser.getId()).isNotNull();
        assertThat(responseUser.getName()).isNotBlank();
        logger.info("Response contains user details for user: {}", responseUser.getName());
    }
    
    @Then("the user should have all required fields populated")
    public void the_user_should_have_all_required_fields_populated() {
        User responseUser = response.as(User.class);
        assertThat(responseUser.getId()).isNotNull();
        assertThat(responseUser.getName()).isNotBlank();
        assertThat(responseUser.getEmail()).isNotBlank();
        assertThat(responseUser.getUsername()).isNotBlank();
        logger.info("User has all required fields populated");
    }
    
    @Then("the response should contain the created user details")
    public void the_response_should_contain_the_created_user_details() {
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();
        logger.info("Response contains created user details: {}", createdUser);
    }
    
    @Then("the created user should match the input data")
    public void the_created_user_should_match_the_input_data() {
        assertThat(createdUser.getName()).isEqualTo(user.getName());
        assertThat(createdUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(createdUser.getUsername()).isEqualTo(user.getUsername());
        logger.info("Created user matches input data");
    }
    
    @Then("the created user name should be {string}")
    public void the_created_user_name_should_be(String expectedName) {
        assertThat(createdUser.getName()).isEqualTo(expectedName);
        logger.info("Created user name is: {}", expectedName);
    }
    
    @Then("the response should contain the updated user details")
    public void the_response_should_contain_the_updated_user_details() {
        User updatedUser = response.as(User.class);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo(user.getName());
        logger.info("Response contains updated user details: {}", updatedUser);
    }
    
    @Then("the user name should be updated to {string}")
    public void the_user_name_should_be_updated_to(String expectedName) {
        User updatedUser = response.as(User.class);
        assertThat(updatedUser.getName()).isEqualTo(expectedName);
        logger.info("User name updated to: {}", expectedName);
    }
    
    @Then("the user should exist in the database")
    public void the_user_should_exist_in_the_database() {
        // This is already validated in the "When" step
        logger.info("User exists in database validation passed");
    }
    
    @Then("the database user data should match API response data")
    public void the_database_user_data_should_match_api_response_data() {
        if (createdUser != null && createdUser.getId() != null) {
            Map<String, Object> dbUser = dbManager.executeQuery(
                "SELECT * FROM users WHERE id = ?", 
                createdUser.getId()
            ).get(0);
            
            assertThat(dbUser.get("name")).isEqualTo(createdUser.getName());
            assertThat(dbUser.get("email")).isEqualTo(createdUser.getEmail());
            assertThat(dbUser.get("username")).isEqualTo(createdUser.getUsername());
            
            logger.info("Database user data matches API response data");
        }
    }
}
