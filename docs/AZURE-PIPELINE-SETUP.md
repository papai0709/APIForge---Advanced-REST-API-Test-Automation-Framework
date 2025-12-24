# Azure Pipeline Setup Guide

## Overview
This guide provides instructions for setting up Azure Pipelines for the RestAssured Test Automation Framework.

## Prerequisites
- Azure DevOps account and project
- GitHub repository connected to Azure DevOps
- Service connections configured for artifact repository
- Java 21 SDK available

## Quick Setup

### 1. Connect Repository
1. Go to Azure DevOps project
2. Create new pipeline
3. Select GitHub as source
4. Authorize GitHub connection
5. Select the RestAssured framework repository

### 2. Configure Pipeline
1. Select "Existing Azure Pipelines YAML file"
2. Choose `/azure-pipelines.yml`
3. Review and save

### 3. Set Pipeline Variables
Navigate to Pipeline settings and add:

```yaml
# Required Variables
JAVA_VERSION: 21
MVN_VERSION: 3.9.0
BUILD_CONFIGURATION: Release

# Optional Variables
SKIP_TESTS: false
PARALLEL_EXECUTION: true
REPORT_PATH: test-reports
```

## Service Connections

### Maven Repository Connection
1. Project Settings → Service connections
2. New service connection → Maven
3. Configure repository URL and credentials
4. Use connection name: `RestAssured-Artifacts`

### SonarQube Connection
1. Project Settings → Service connections
2. New service connection → SonarQube
3. Enter SonarQube server URL
4. Add authentication token
5. Use connection name: `SonarQube`

## Stage Configuration

### Build Stage
- Compiles Java code
- Runs Maven clean install
- Publishes JUnit results
- Runs on: Ubuntu latest

### Test Stage (Parallel Jobs)
Three parallel test jobs:
1. **Unit Tests** - Runs mvn test
2. **Smoke Tests** - Runs @smoke tagged scenarios
3. **Regression Tests** - Runs @regression tagged scenarios

### Code Quality Stage
1. **SonarQube Analysis** - Static code analysis
2. **Dependency Check** - Security vulnerability scanning

### Reports Stage
1. **Allure Reports** - Generates Allure test reports
2. **Test Reports** - Publishes JUnit results to Azure DevOps
3. **Artifacts** - Uploads logs and test outputs

### Package Stage
1. Creates JAR artifact
2. Publishes to artifact repository (main branch only)

## Environment Configuration

### Local Storage Path
Store test results locally:
```bash
$(Agent.BuildDirectory)/test-results
```

### Artifact Location
Build artifacts stored in:
```bash
target/
```

### Logs Location
Test logs stored in:
```bash
logs/
```

## Trigger Configuration

### Automatic Triggers
- **Push to main** - Runs full pipeline
- **Push to develop** - Runs tests only
- **Push to feature/* - Runs build and unit tests only

### Manual Trigger
1. Go to Pipelines
2. Click "Run pipeline"
3. Select branch
4. Configure stage to run (optional)

## Environment-Specific Configuration

### Development
- Branch: `develop`
- Stages: Build, Test (smoke + regression)
- Reports: Allure + JUnit
- Deploy: Artifact repository (snapshot)

### Production
- Branch: `main`
- Stages: All stages
- Reports: Allure + JUnit + SonarQube
- Deploy: Maven Central (release)

## Pipeline Variables

### Java & Maven
```yaml
JAVA_VERSION: '21'
MVN_VERSION: '3.9.0'
MAVEN_OPTS: '-Xmx3072m'
```

### Test Configuration
```yaml
CUCUMBER_FILTER_TAGS: '@smoke'  # For tag-based execution
PARALLEL_EXECUTION: 'true'
BROWSER: 'chrome'
```

## Troubleshooting

### Build Failures
1. Check Maven build logs
2. Verify Java version (21)
3. Clear Maven cache: `mvn clean`
4. Check Maven Central connectivity

### Test Failures
1. Review test output in artifacts
2. Check logs/ directory
3. Verify API endpoints are accessible
4. Check database connections if @database tagged

### Report Generation Failures
1. Verify Allure plugin configuration
2. Check write permissions on target directory
3. Ensure test results exist before report generation

## Best Practices

### Pipeline Optimization
1. Use job caching for dependencies
2. Run tests in parallel
3. Skip tests in deploy stage
4. Archive only necessary artifacts

### Security
1. Store credentials in service connections
2. Don't commit sensitive data
3. Use pipeline secrets for API keys
4. Rotate tokens regularly

### Monitoring
1. Enable pipeline notifications
2. Set up build badges in README
3. Configure email alerts for failures
4. Monitor SonarQube quality gates

## Integration with Other Tools

### Slack Notifications
```yaml
- task: SlackNotification@1
  inputs:
    webhookUrl: $(SlackWebhook)
    message: 'Pipeline completed: $(Build.BuildNumber)'
```

### Email Notifications
1. Project Settings → Notifications
2. Create new subscription
3. Set conditions and recipients

### Azure App Insights
1. Enable Application Insights
2. Configure instrumentation key
3. Monitor pipeline performance

## Advanced Configuration

### Multiple Environments
Create pipeline environments for:
- Dev
- Staging
- Production

### Approval Gates
Configure approvals before:
- Deployment to production
- SonarQube quality gates
- Manual stage execution

### Scheduled Builds
Configure nightly builds:
```yaml
schedules:
  - cron: '0 2 * * *'  # 2 AM UTC daily
    branches:
      include:
        - main
        - develop
```

## Performance Tuning

### Parallel Execution
- Unit tests run independently
- Smoke and regression tests run in parallel
- Reduces total pipeline time

### Caching Strategy
```yaml
- task: Maven@3
  inputs:
    mavenOptions: '-Xmx3072m'
    restoreContinuingOnError: true
```

### Resource Allocation
- Default: Ubuntu latest (2-core)
- For large test suites: Windows or macOS agents

## Support

For issues or questions:
1. Check Azure Pipelines documentation
2. Review build logs
3. Contact DevOps team
4. Check Maven/Java compatibility

## References
- [Azure Pipelines Documentation](https://docs.microsoft.com/azure/devops/pipelines/)
- [Maven Plugin Documentation](https://maven.apache.org/plugins/)
- [TestNG Documentation](https://testng.org/)
- [Cucumber Documentation](https://cucumber.io/)

