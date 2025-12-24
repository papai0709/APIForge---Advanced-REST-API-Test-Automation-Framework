Feature: User API Management
  As an API consumer
  I want to perform CRUD operations on User resources
  So that I can manage user data effectively

  Background:
    Given the API base URL is configured
    And the request headers are set

  @smoke @get
  Scenario: Get all users
    When I send a GET request to "/users"
    Then the response status code should be 200
    And the response should contain a list of users
    And each user should have required fields

  @smoke @get
  Scenario: Get user by valid ID
    Given a user exists with ID 1
    When I send a GET request to "/users/1"
    Then the response status code should be 200
    And the response should contain user details
    And the user should have all required fields populated

  @negative @get
  Scenario: Get user by invalid ID
    When I send a GET request to "/users/999999"
    Then the response status code should be 404

  @post @create
  Scenario: Create a new user
    Given I have user data:
      | name     | email              | username |
      | John Doe | john.doe@email.com | johndoe  |
    When I send a POST request to "/users" with the user data
    Then the response status code should be 201
    And the response should contain the created user details
    And the created user should match the input data

  @post @create
  Scenario Outline: Create users with different data sets
    Given I have user data with name "<name>", email "<email>", and username "<username>"
    When I send a POST request to "/users" with the user data
    Then the response status code should be 201
    And the created user name should be "<name>"

    Examples:
      | name        | email                   | username    |
      | Alice Smith | alice.smith@email.com   | alicesmith  |
      | Bob Johnson | bob.johnson@email.com   | bobjohnson  |
      | Carol White | carol.white@email.com   | carolwhite  |

  @put @update
  Scenario: Update an existing user
    Given a user exists with ID 1
    And I have updated user data:
      | name         | email                   | username     |
      | John Updated | john.updated@email.com  | johnupdated  |
    When I send a PUT request to "/users/1" with the updated user data
    Then the response status code should be 200
    And the response should contain the updated user details

  @patch @update
  Scenario: Partially update a user
    Given a user exists with ID 1
    When I send a PATCH request to "/users/1" with partial data:
      | name         |
      | John Patched |
    Then the response status code should be 200
    And the user name should be updated to "John Patched"

  @delete
  Scenario: Delete an existing user
    Given a user exists with ID 1
    When I send a DELETE request to "/users/1"
    Then the response status code should be 200
    When I send a GET request to "/users/1"
    Then the response status code should be 404

  @negative @delete
  Scenario: Delete a non-existing user
    When I send a DELETE request to "/users/999999"
    Then the response status code should be 404

  @database @validation
  Scenario: Validate user data in database
    Given a user is created through API
    When I query the database for the user
    Then the user should exist in the database
    And the database user data should match API response data
