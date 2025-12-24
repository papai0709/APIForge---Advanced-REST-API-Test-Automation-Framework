# 🔧 APIForge - Advanced REST API Test Automation Framework

A comprehensive, production-ready REST API test automation framework built with Java, RestAssured, Cucumber BDD, and TestNG.

## 🏗️ Framework Architecture

This framework follows industry best practices and includes:

- **POM (Page Object Model)** pattern for API endpoints
- **POJO classes** for data management with Jackson annotations
- **BDD support** using Cucumber with Gherkin syntax
- **Database connectivity** with MySQL and PostgreSQL support
- **Configuration management** through properties files
- **Local storage** for test data and reports
- **Parallel execution** capability
- **Comprehensive reporting** with Allure integration

## 📁 Project Structure

```
src/
├── main/java/com/automation/
│   ├── base/
│   │   └── BaseTest.java              # Base test class with common setup
│   ├── config/
│   │   └── ConfigManager.java         # Configuration management
│   ├── database/
│   │   └── DatabaseManager.java       # Database connectivity and operations
│   ├── pojo/
│   │   ├── BasePojo.java             # Base POJO with common fields
│   │   └── User.java                 # User entity POJO
│   ├── pom/
│   │   ├── BasePOM.java              # Base POM for API operations
│   │   └── UserPOM.java              # User API specific operations
│   └── utils/
│       ├── FileManager.java          # File operations and local storage
│       └── TestDataUtils.java        # Test data generation utilities
│
├── test/java/com/automation/
│   ├── hooks/
│   │   └── TestHooks.java            # Cucumber hooks for setup/teardown
│   ├── runner/
│   │   └── TestRunner.java           # TestNG runner for Cucumber
│   └── stepdefinitions/
│       └── UserStepDefinitions.java  # Cucumber step definitions
│
└── test/resources/
    ├── config/
    │   └── application.properties     # Configuration properties
    ├── features/
    │   └── user_management.feature    # BDD feature files
    └── testdata/
        └── user_test_data.json       # Test data files
```

## 🚀 Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+
- MySQL/PostgreSQL database (for database tests)
- Git
- IntelliJ IDEA (Community or Ultimate) or VS Code

### Installation

#### Option 1: IntelliJ IDEA Setup
1. **Open IntelliJ IDEA**
2. **Import Project**: File → Open → Select `Api Framework` folder
3. **Maven Import**: IntelliJ will auto-detect Maven and import dependencies
4. **SDK Setup**: File → Project Structure → Set Project SDK to Java 21
5. **Run Configuration**: Use pre-configured run configurations (Maven Test, Smoke Tests, etc.)

📋 **See [IntelliJ-SETUP.md](docs/IntelliJ-SETUP.md) for detailed IntelliJ instructions**

#### Option 2: Command Line Setup
1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd api-framework
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure the application**
   - Update `src/test/resources/config/application.properties`
   - Set your API base URL, database connection details, etc.

4. **Run tests**
   ```bash
   # Run all tests
   mvn clean test
   
   # Run specific tags
   mvn clean test -Dcucumber.filter.tags="@smoke"
   mvn clean test -Dcucumber.filter.tags="@regression"
   ```

## ⚙️ Configuration

### Application Properties

All configurable values are stored in `application.properties`:

```properties
# Environment Configuration
environment=qa
base.url=https://api.example.com
timeout=30000

# Database Configuration  
db.type=mysql
db.host=localhost
db.port=3306
db.name=testdb
db.username=testuser
db.password=testpass

# Test Configuration
test.data.path=src/test/resources/testdata
local.storage.path=test-output
parallel.execution=true
thread.count=5
```

### Environment-specific Configuration

You can override properties using system properties:

```bash
mvn test -Dbase.url=https://staging-api.example.com -Denvironment=staging
```

## ✨ Key Features

**APIForge** brings together industry best practices with modern testing tools:

### 1. Robust API Testing
- RESTful API testing with RestAssured
- JSON schema validation
- Response time assertions
- Custom request/response logging

### 2. BDD Support
- Cucumber integration with Gherkin syntax
- Reusable step definitions
- Scenario context management
- Tag-based test execution

### 3. Database Integration
- Support for MySQL and PostgreSQL
- Connection pooling
- Prepared statements for security
- Database validation in tests

### 4. Data Management
- POJO classes with Jackson annotations
- Test data generation utilities
- JSON data handling
- Local storage for test artifacts

### 5. Parallel Execution
- TestNG parallel execution
- Thread-safe design
- Configurable thread count
- Isolated test contexts

### 6. Comprehensive Reporting
- Allure reporting integration
- Cucumber HTML reports
- JSON and XML report formats
- Screenshot and log attachments

## 📝 Writing Tests

### Creating Feature Files

```gherkin
Feature: User Management API
  
  @smoke
  Scenario: Create a new user
    Given I have user data:
      | name     | email              | username |
      | John Doe | john.doe@email.com | johndoe  |
    When I send a POST request to "/users" with the user data
    Then the response status code should be 201
    And the response should contain the created user details
```

### Adding Step Definitions

```java
@When("I send a POST request to {string} with the user data")
public void i_send_post_request(String endpoint) {
    response = userPOM.createUser(user);
    fileManager.saveApiResponse("POST_" + endpoint, response.asString());
}
```

### Creating POJO Classes

```java
public class User extends BasePojo {
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("email")  
    private String email;
    
    // Constructors, getters, setters
}
```

### Adding API Endpoints

```java
public class UserPOM extends BasePOM {
    private static final String USERS_ENDPOINT = "/users";
    
    public Response createUser(User user) {
        return performPost(USERS_ENDPOINT, user);
    }
}
```

## 🧪 Test Execution

### Running Tests by Tags

```bash
# Smoke tests only
mvn test -Dcucumber.filter.tags="@smoke"

# Regression tests
mvn test -Dcucumber.filter.tags="@regression"  

# Database tests
mvn test -Dcucumber.filter.tags="@database"

# Exclude negative tests
mvn test -Dcucumber.filter.tags="not @negative"
```

### Parallel Execution

```bash
# Run with specific thread count
mvn test -Dparallel.execution=true -Dthread.count=10
```

### Environment-specific Execution

```bash
# Run against different environments
mvn test -Denvironment=staging -Dbase.url=https://staging-api.com

mvn test -Denvironment=prod -Dbase.url=https://api.com
```

## 📚 Documentation

Complete documentation files are available in the `docs/` directory:

- 📘 **[Setup Guide](docs/SETUP.md)** - Complete project setup and initial configuration
- 🔧 **[IntelliJ IDEA Setup](docs/IntelliJ-SETUP.md)** - Detailed IntelliJ IDEA configuration and setup
- 📋 **[CI/CD Setup Guide](docs/CI-CD-SETUP.md)** - Complete overview and comparison of CI/CD pipelines
- 📋 **[CI/CD Setup Checklist](docs/CI-CD-SETUP-CHECKLIST.md)** - Step-by-step checklist for CI/CD implementation
- 🔵 **[Azure Pipeline Setup](docs/AZURE-PIPELINE-SETUP.md)** - Detailed Azure DevOps configuration and deployment
- ⚪ **[GitHub Actions Setup](docs/GITHUB-ACTIONS-SETUP.md)** - Detailed GitHub Actions workflow configuration
- ✅ **[CI/CD Complete](docs/CI-CD-COMPLETE.md)** - Verification and validation of CI/CD pipeline setup
- 📋 **[CI/CD Implementation Summary](docs/CI-CD-IMPLEMENTATION-SUMMARY.md)** - Summary of CI/CD implementation with examples
- 🎉 **[Deliverables Summary](docs/DELIVERABLES-SUMMARY.md)** - Complete project deliverables and features overview

## 🚀 CI/CD Pipelines

This framework includes support for **Azure Pipelines** and **GitHub Actions** for automated testing and deployment.

### Quick Links
- 📘 **[CI/CD Setup Guide](docs/CI-CD-SETUP.md)** - Complete overview and comparison
- 🔵 **[Azure Pipeline Setup](docs/AZURE-PIPELINE-SETUP.md)** - Detailed Azure DevOps configuration
- ⚪ **[GitHub Actions Setup](docs/GITHUB-ACTIONS-SETUP.md)** - Detailed GitHub Actions configuration

### Supported Platforms

#### Azure Pipelines
- Multi-stage pipeline with parallel job execution
- Integration with Azure DevOps
- SonarQube code quality analysis
- Artifact repository deployment
- Enterprise-grade features

```bash
# Pipeline File: azure-pipelines.yml
```

#### GitHub Actions
- Automated workflows triggered by GitHub events
- Built-in CI/CD without external services
- Community-driven actions
- Easy GitHub integration
- Free for public repositories

```bash
# Pipeline File: .github/workflows/ci-cd.yml
```

### Pipeline Stages

Both pipelines follow this execution flow:

```
1. BUILD              - Compile code, run Maven install
2. TEST (Parallel)    - Unit tests, Smoke tests, Regression tests  
3. CODE QUALITY       - SonarQube analysis, Dependency security check
4. REPORTS            - Allure reports, JUnit results, Test logs
5. PACKAGE            - Create JAR artifact, Deploy to repository
```

### Automatic Triggers

**Azure Pipelines:**
- Push to `main`, `develop`, or `feature/*` branches
- Pull requests
- Scheduled daily runs

**GitHub Actions:**
- Push to `main`, `develop`, or `feature/*` branches
- Pull requests to `main` or `develop`
- Scheduled daily at 2 AM UTC

### Test Tags

Filter tests by tags in CI/CD:

```bash
# Smoke tests (quick validation)
-Dcucumber.filter.tags="@smoke"

# Regression tests (comprehensive)
-Dcucumber.filter.tags="@regression"

# Database tests
-Dcucumber.filter.tags="@database"

# Custom combinations
-Dcucumber.filter.tags="@smoke and @api"
-Dcucumber.filter.tags="not @performance"
```

### Configuration & Secrets

**Azure Pipelines:**
- Configure via `Project Settings → Pipeline settings`
- Store secrets in pipeline variables
- Service connections for external integrations

**GitHub Actions:**
- Configure via `Settings → Secrets and variables → Actions`
- Store secrets as repository secrets
- Auto-provided `GITHUB_TOKEN`

### Artifacts

All pipelines publish:
- Build artifacts (JAR files)
- Test results (JUnit XML)
- Test logs (application logs)
- Allure reports (HTML reports)
- Framework package (Maven compatible)

### Performance

**Parallel Execution:**
- Unit tests, Smoke tests, and Regression tests run simultaneously
- Typical total time: 20-30 minutes

**Caching:**
- Maven dependencies cached automatically
- Faster builds and tests

## 📊 Reporting

### Generate Allure Reports

```bash
# Generate Allure report
mvn allure:report

# Serve Allure report
mvn allure:serve
```

### View Cucumber Reports

After test execution, reports are available in:
- `target/cucumber-reports/html/index.html` - HTML report
- `target/cucumber-reports/Cucumber.json` - JSON report
- `target/allure-results/` - Allure results

## 🔧 Customization

### Adding New API Endpoints

1. Create POJO class in `com.automation.pojo`
2. Create POM class in `com.automation.pom` extending `BasePOM`
3. Add feature file in `src/test/resources/features`
4. Create step definitions in `com.automation.stepdefinitions`

### Adding Database Support

1. Add database driver dependency in `pom.xml`
2. Update `DatabaseManager` for new database type
3. Add connection configuration in `application.properties`

### Custom Validations

1. Add validation methods in respective POM classes
2. Create utility methods in `TestDataUtils`
3. Add assertion methods in step definitions

## 🛠️ IDE Support

### IntelliJ IDEA (Recommended)
- **Pre-configured project files** - `.idea/` folder with all settings
- **Run configurations** - Maven tasks, TestNG runners, Cucumber tests
- **Code style** - Consistent formatting and import organization  
- **Plugin recommendations** - Cucumber, TestNG, Maven Helper
- **Debugging support** - Full debugging capabilities for tests
- **Database tools** - Built-in database management (Ultimate edition)

📋 **Complete setup guide: [IntelliJ-SETUP.md](docs/IntelliJ-SETUP.md)**

### VS Code Support
- **Tasks configuration** - `.vscode/tasks.json` for build/test tasks
- **Java extension pack** compatibility
- **Integrated terminal** support
- **Git integration** with source control

## 🛠️ Troubleshooting

### Common Issues

1. **Database Connection Issues**
   - Verify database is running
   - Check connection parameters in properties file
   - Ensure database driver is included in dependencies

2. **Test Failures**
   - Check API endpoint availability
   - Verify test data validity
   - Review logs in `logs/` directory

3. **Parallel Execution Issues**
   - Ensure thread-safe design
   - Check for shared state between tests
   - Adjust thread count if needed

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add/update tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**APIForge Framework Version:** 1.0.0  
**Last Updated:** December 2025  
**Supported Java Versions:** 21+  
**Repository:** APIForge - Advanced REST API Test Automation Framework
