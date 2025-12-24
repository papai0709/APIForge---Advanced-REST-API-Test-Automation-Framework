<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

# RestAssured Test Automation Framework - Copilot Instructions

## Project Overview
This is a comprehensive Java-based REST API test automation framework using RestAssured, Cucumber, TestNG, and Maven. The framework follows industry best practices with POM design pattern, BDD support, database connectivity, and robust configuration management.

## Framework Architecture
- **Base Package**: `com.automation`
- **POM Pattern**: API endpoints are modeled as Page Object Model classes
- **POJO Classes**: Data objects with Jackson annotations for JSON serialization
- **BDD Support**: Cucumber with Gherkin feature files
- **Database**: MySQL/PostgreSQL support with connection pooling
- **Configuration**: Properties-based configuration management
- **Parallel Execution**: TestNG-based parallel test execution

## Code Standards and Practices

### Java Code Style
- Use Java 21 features and modern coding practices
- Follow camelCase for methods and variables
- Use PascalCase for class names
- Add comprehensive JavaDoc comments for public methods
- Use SLF4J for logging with appropriate log levels

### Test Automation Best Practices
- Always extend `BaseTest` for API test classes
- Use `BasePOM` for creating API endpoint classes  
- Implement proper error handling and validation
- Add meaningful assertions with descriptive error messages
- Use data-driven testing approach with external test data

### BDD Guidelines
- Write clear and readable Gherkin scenarios
- Use descriptive step definitions
- Implement reusable step definitions
- Tag scenarios appropriately (@smoke, @regression, @database, etc.)
- Follow Given-When-Then structure consistently

### Database Integration
- Use `DatabaseManager.getInstance()` for database operations
- Always use prepared statements for SQL queries
- Implement proper connection management (connect/disconnect)
- Add database validation scenarios with `@database` tag

### Configuration Management
- Use `ConfigManager.getInstance()` for accessing configuration
- Support environment-specific configurations
- Allow system property overrides for CI/CD integration
- Keep sensitive data configurable through properties

## File Organization

### Source Structure
```
src/main/java/com/automation/
├── base/          # Base classes for tests
├── config/        # Configuration management
├── database/      # Database connectivity
├── pojo/          # Data objects
├── pom/           # Page Object Model for APIs
└── utils/         # Utility classes

src/test/java/com/automation/
├── hooks/         # Cucumber hooks
├── runner/        # TestNG runners
└── stepdefinitions/ # Cucumber step definitions

src/test/resources/
├── config/        # Configuration files
├── features/      # Cucumber feature files
└── testdata/      # Test data files
```

### Naming Conventions
- POM Classes: `[Entity]POM.java` (e.g., `UserPOM.java`)
- POJO Classes: `[Entity].java` (e.g., `User.java`)
- Feature Files: `[entity]_[action].feature` (e.g., `user_management.feature`)
- Step Definitions: `[Entity]StepDefinitions.java`
- Test Data: `[entity]_test_data.json`

## Dependencies and Libraries
- **RestAssured 5.4.0**: API testing framework
- **Cucumber 7.18.1**: BDD framework
- **TestNG 7.9.0**: Test execution framework
- **Jackson 2.16.1**: JSON processing
- **MySQL/PostgreSQL**: Database connectivity
- **Allure 2.25.0**: Test reporting
- **SLF4J + Logback**: Logging framework

## Common Patterns

### Creating API POM Classes
```java
public class EntityPOM extends BasePOM {
    private static final String BASE_ENDPOINT = "/entities";
    
    public EntityPOM() {
        super(BASE_ENDPOINT);
    }
    
    public Response getAllEntities() {
        return performGet(BASE_ENDPOINT);
    }
}
```

### Creating POJO Classes
```java
@JsonIgnoreProperties(ignoreUnknown = true)
public class Entity extends BasePojo {
    @JsonProperty("field_name")
    private String fieldName;
    
    // Constructors, getters, setters
}
```

### Writing Step Definitions
```java
@When("I perform action on {string}")
public void performAction(String endpoint) {
    response = entityPOM.performAction(endpoint);
    fileManager.saveApiResponse("action_" + endpoint, response.asString());
}
```

## Testing Guidelines

### Test Data Management
- Use JSON files for static test data
- Use `TestDataUtils` for dynamic data generation
- Store test results in local storage for debugging
- Implement data cleanup in hooks

### Error Handling
- Implement proper exception handling in all methods
- Use meaningful error messages in assertions
- Log all important operations and failures
- Validate both positive and negative scenarios

### Performance Considerations
- Use parallel execution for faster test runs
- Implement connection pooling for database operations
- Configure appropriate timeouts
- Clean up resources properly

## Reporting and Monitoring
- Use Allure for comprehensive test reporting
- Generate Cucumber HTML reports
- Implement custom logging for debugging
- Store test artifacts in local storage

When generating code for this framework, please:
1. Follow the established patterns and conventions
2. Add appropriate error handling and logging
3. Include comprehensive documentation
4. Implement proper validation and assertions
5. Consider thread safety for parallel execution
6. Use the existing utility classes and managers
7. Add appropriate tags to Cucumber scenarios
8. Ensure database operations are properly managed
