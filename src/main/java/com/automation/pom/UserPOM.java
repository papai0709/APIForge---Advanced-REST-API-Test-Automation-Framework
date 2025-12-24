package com.automation.pom;

import com.automation.pojo.User;
import io.restassured.response.Response;

import java.util.List;

/**
 * User API POM class
 * Contains methods specific to User API operations
 */
public class UserPOM extends BasePOM {
    
    private static final String USERS_ENDPOINT = "/users";
    
    public UserPOM() {
        super(USERS_ENDPOINT);
    }
    
    /**
     * Get all users
     */
    public Response getAllUsers() {
        return performGet(USERS_ENDPOINT);
    }
    
    /**
     * Get user by ID
     */
    public Response getUserById(long userId) {
        String endpoint = buildEndpoint(USERS_ENDPOINT, String.valueOf(userId));
        return performGet(endpoint);
    }
    
    /**
     * Create new user
     */
    public Response createUser(User user) {
        return performPost(USERS_ENDPOINT, user);
    }
    
    /**
     * Update existing user
     */
    public Response updateUser(long userId, User user) {
        String endpoint = buildEndpoint(USERS_ENDPOINT, String.valueOf(userId));
        return performPut(endpoint, user);
    }
    
    /**
     * Partially update user
     */
    public Response patchUser(long userId, User user) {
        String endpoint = buildEndpoint(USERS_ENDPOINT, String.valueOf(userId));
        return performPatch(endpoint, user);
    }
    
    /**
     * Delete user
     */
    public Response deleteUser(long userId) {
        String endpoint = buildEndpoint(USERS_ENDPOINT, String.valueOf(userId));
        return performDelete(endpoint);
    }
    
    /**
     * Get all users as User objects
     */
    public List<User> getAllUsersAsObjects() {
        Response response = getAllUsers();
        validateStatusCode(response, 200);
        return response.jsonPath().getList("", User.class);
    }
    
    /**
     * Get user by ID as User object
     */
    public User getUserByIdAsObject(long userId) {
        Response response = getUserById(userId);
        validateStatusCode(response, 200);
        return extractResponse(response, User.class);
    }
    
    /**
     * Validate user creation response
     */
    public User validateUserCreation(User user) {
        Response response = createUser(user);
        validateStatusCode(response, 201);
        User createdUser = extractResponse(response, User.class);
        
        // Validate that the created user has the expected properties
        if (createdUser.getName() == null || !createdUser.getName().equals(user.getName())) {
            throw new AssertionError("Created user name does not match expected");
        }
        
        if (createdUser.getEmail() == null || !createdUser.getEmail().equals(user.getEmail())) {
            throw new AssertionError("Created user email does not match expected");
        }
        
        logger.info("User creation validation passed for user: {}", user.getName());
        return createdUser;
    }
    
    /**
     * Validate user update response
     */
    public User validateUserUpdate(long userId, User user) {
        Response response = updateUser(userId, user);
        validateStatusCode(response, 200);
        User updatedUser = extractResponse(response, User.class);
        
        logger.info("User update validation passed for user ID: {}", userId);
        return updatedUser;
    }
    
    /**
     * Validate user deletion
     */
    public void validateUserDeletion(long userId) {
        Response response = deleteUser(userId);
        validateStatusCode(response, 200);
        logger.info("User deletion validation passed for user ID: {}", userId);
    }
    
    /**
     * Check if user exists
     */
    public boolean userExists(long userId) {
        try {
            Response response = getUserById(userId);
            return response.getStatusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Search users by name (if API supports query parameters)
     */
    public Response searchUsersByName(String name) {
        return getRequestSpec()
                .queryParam("name", name)
                .when()
                .get(USERS_ENDPOINT)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
}
