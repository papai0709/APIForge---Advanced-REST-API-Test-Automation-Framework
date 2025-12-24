# Framework Setup Guide

## Prerequisites Installation

### 1. Install Java 21
- Download and install Java 21 from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
- Verify installation: `java -version`
- Set JAVA_HOME environment variable

### 2. Install Apache Maven
- Download Maven from [Apache Maven](https://maven.apache.org/download.cgi)
- Extract to desired location (e.g., C:\apache-maven-3.9.6)
- Add Maven bin directory to PATH environment variable
- Verify installation: `mvn -version`

### 3. Install Database (Optional - for database tests)
- **MySQL**: Download from [MySQL](https://dev.mysql.com/downloads/mysql/)
- **PostgreSQL**: Download from [PostgreSQL](https://www.postgresql.org/download/)

### 4. IDE Setup
- Install VS Code with Java Extension Pack
- Or use IntelliJ IDEA / Eclipse

## Quick Start

### 1. Build the Project
```bash
cd "Api Framework"
mvn clean install -DskipTests
```

### 2. Configure Application
Edit `src/test/resources/config/application.properties`:
```properties
base.url=https://jsonplaceholder.typicode.com
environment=qa
```

### 3. Run Sample Tests
```bash
# Run smoke tests
mvn test -Dcucumber.filter.tags="@smoke"

# Run all tests
mvn test
```

## Project Structure Overview

```
Api Framework/
├── src/main/java/com/automation/
│   ├── base/           # Base test classes
│   ├── config/         # Configuration management
│   ├── database/       # Database operations
│   ├── pojo/           # Data objects (User, Post, etc.)
│   ├── pom/            # API Page Objects
│   └── utils/          # Utilities (FileManager, TestDataUtils)
├── src/test/java/com/automation/
│   ├── hooks/          # Test setup/teardown
│   ├── runner/         # TestNG test runner
│   └── stepdefinitions/ # Cucumber step definitions
├── src/test/resources/
│   ├── config/         # Configuration files
│   ├── features/       # BDD feature files
│   └── testdata/       # Test data JSON files
└── target/             # Generated reports and artifacts
```

## Available Test Tags

- `@smoke` - Quick validation tests
- `@regression` - Full regression suite
- `@database` - Tests requiring database
- `@get` - GET operation tests
- `@post` - POST operation tests
- `@put` - PUT operation tests
- `@delete` - DELETE operation tests
- `@negative` - Negative test scenarios

## Running Tests

### By Tags
```bash
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@regression"
mvn test -Dcucumber.filter.tags="@database"
mvn test -Dcucumber.filter.tags="not @negative"
```

### Parallel Execution
```bash
mvn test -Dparallel.execution=true -Dthread.count=5
```

### Environment Override
```bash
mvn test -Dbase.url=https://staging-api.com -Denvironment=staging
```

## Reports Location

After test execution, reports are generated in:
- `target/cucumber-reports/html/index.html` - Cucumber HTML Report
- `target/cucumber-reports/Cucumber.json` - JSON Report
- `target/surefire-reports/` - TestNG Reports

## Framework Features

✅ **REST API Testing** with RestAssured  
✅ **BDD Support** with Cucumber  
✅ **Parallel Execution** with TestNG  
✅ **Database Integration** (MySQL/PostgreSQL)  
✅ **Configuration Management**  
✅ **Local Storage** for test artifacts  
✅ **Comprehensive Logging**  
✅ **Data Generation Utilities**  
✅ **POM Design Pattern**  
✅ **POJO Classes** with Jackson  

## Troubleshooting

### Maven Not Found
- Ensure Maven is installed and added to PATH
- Restart terminal after PATH changes

### Compilation Errors
- Run `mvn clean install` to download dependencies
- Check Java version compatibility

### Test Failures
- Verify API endpoints are accessible
- Check network connectivity
- Review test data validity

### Database Issues
- Ensure database server is running
- Verify connection parameters
- Check database permissions

## Next Steps

1. **Add Your API Endpoints**
   - Create POJO classes in `pojo/` package
   - Create POM classes in `pom/` package
   - Add feature files in `features/`

2. **Configure Your Environment**
   - Update `application.properties`
   - Add environment-specific configs

3. **Create Test Data**
   - Add JSON files in `testdata/`
   - Use `TestDataUtils` for dynamic data

4. **Run Your Tests**
   - Execute via Maven commands
   - View reports in target directory

## Support

- Check the README.md for detailed documentation
- Review existing examples (User, Post APIs)
- Use VS Code with Copilot for code assistance
- Follow the framework patterns for consistency
