# CI/CD Pipeline Setup Guide

This guide covers setting up both Azure Pipelines and GitHub Actions for the RestAssured Test Automation Framework.

## Quick Navigation

- **[Azure Pipeline Setup](AZURE-PIPELINE-SETUP.md)** - Detailed Azure DevOps configuration
- **[GitHub Actions Setup](GITHUB-ACTIONS-SETUP.md)** - Detailed GitHub Actions configuration

## Which Should I Use?

### Choose Azure Pipelines if:
- Using Azure DevOps organization
- Need enterprise-grade CI/CD
- Require multiple teams/projects management
- Want advanced release management
- Need SonarQube integration
- Working with Microsoft stack

### Choose GitHub Actions if:
- Using GitHub for version control
- Want built-in CI/CD in GitHub
- Need simple, quick setup
- Prefer community-driven solutions
- Want free unlimited public repo builds
- Already familiar with GitHub ecosystem

### Use Both if:
- Running in hybrid environment
- Need redundancy
- Different teams prefer different platforms
- Want to migrate gradually

## Feature Comparison

| Feature | Azure Pipelines | GitHub Actions |
|---------|---|---|
| **Setup Time** | 15-30 minutes | 5-10 minutes |
| **Configuration** | YAML + UI | YAML only |
| **Cost** | 1800 min free/month | Free (public repos) |
| **Scalability** | Enterprise | Community |
| **Integration** | Microsoft ecosystem | GitHub ecosystem |
| **SonarQube** | Native task | Via action |
| **Notifications** | Multiple channels | GitHub + integrations |
| **Parallel Jobs** | Yes | Yes |
| **Scheduled Runs** | Yes | Yes |
| **Matrix Testing** | Yes | Yes |
| **Approval Gates** | Yes | No native |
| **Artifact Storage** | Long-term | 90 days default |

## Pipeline Overview

### Stages/Jobs

```
1. BUILD
   └─ Compile Java code
   └─ Run Maven clean install
   └─ Publish artifacts

2. TEST (Parallel)
   ├─ Unit Tests
   ├─ Smoke Tests (@smoke)
   └─ Regression Tests (@regression)

3. CODE QUALITY
   ├─ SonarQube Analysis
   └─ Dependency Check

4. REPORTS
   ├─ Allure Reports
   ├─ JUnit Results
   └─ Test Logs

5. PACKAGE
   ├─ Create JAR
   └─ Deploy to Repository
```

## Parallel Execution

All test jobs run in parallel after build completes:
- **Unit Tests** - Standard JUnit tests
- **Smoke Tests** - Quick validation (@smoke tag)
- **Regression Tests** - Full test suite (@regression tag)

Reduces total execution time by ~60%.

## Test Execution

### Filter by Tags
```bash
# Smoke tests only
mvn test -Dcucumber.filter.tags="@smoke"

# Regression tests only
mvn test -Dcucumber.filter.tags="@regression"

# Database tests
mvn test -Dcucumber.filter.tags="@database"

# Multiple tags (AND)
mvn test -Dcucumber.filter.tags="@smoke and @api"

# Multiple tags (OR)
mvn test -Dcucumber.filter.tags="@smoke or @regression"

# Exclude tests
mvn test -Dcucumber.filter.tags="not @performance"
```

## Configuration Files

### Azure Pipelines
- **Main Config**: `azure-pipelines.yml`
- **Location**: Repository root
- **Setup Guide**: `AZURE-PIPELINE-SETUP.md`

### GitHub Actions
- **Main Config**: `.github/workflows/ci-cd.yml`
- **Location**: `.github/workflows/` directory
- **Setup Guide**: `GITHUB-ACTIONS-SETUP.md`

## Environment Variables

### Common Variables
```yaml
JAVA_VERSION: '21'
MVN_VERSION: '3.9.0'
BUILD_CONFIGURATION: 'Release'
```

### Test Variables
```yaml
CUCUMBER_FILTER_TAGS: '@smoke'
PARALLEL_EXECUTION: 'true'
TEST_TIMEOUT: '60'
```

### Pipeline Variables
```yaml
SKIP_TESTS: 'false'
REPORT_PATH: 'test-reports'
ARTIFACT_PATH: 'target/'
```

## Secrets Management

### Azure Pipelines
1. Project Settings → Pipeline settings
2. Add secret variables
3. Reference in pipeline: `$(SecretVariable)`

### GitHub Actions
1. Settings → Secrets and variables → Actions
2. Add repository secrets
3. Reference in workflow: `${{ secrets.SECRET_NAME }}`

## Branching Strategy

### Pipeline Triggers
```
main branch (production)
  └─ Full pipeline: Build → Test → Quality → Reports → Package
  └─ Deploy to production repository

develop branch (staging)
  └─ Test pipeline: Build → Test → Reports
  └─ Deploy to staging repository

feature/* branches
  └─ Quick test: Build → Unit Tests
  └─ No deployment

PR requests
  └─ Validation: Build → Unit Tests → Code Quality
```

## Artifact Management

### Published Artifacts
1. **Build Artifacts** - Compiled classes and JARs
2. **Test Results** - JUnit XML reports
3. **Test Logs** - Application logs and test output
4. **Allure Reports** - HTML test reports
5. **Framework JAR** - Final packaged framework

### Download Artifacts
**Azure Pipelines**: Pipeline → Run → Artifacts  
**GitHub Actions**: Actions → Workflow → Artifacts

## Reporting

### Allure Reports
- Visual test execution reports
- Historical trend analysis
- Test step details
- Failure analysis

### JUnit Reports
- Integration with Azure DevOps Test Plans
- GitHub PR checks
- Coverage metrics

### SonarQube Dashboard
- Code quality metrics
- Security vulnerabilities
- Code coverage
- Technical debt

## Performance Metrics

### Typical Execution Times
- Build: 3-5 minutes
- Unit Tests: 2-3 minutes
- Smoke Tests: 3-5 minutes
- Regression Tests: 5-10 minutes
- Code Quality: 2-3 minutes
- Reports: 1-2 minutes
- **Total: ~20-30 minutes** (parallel execution)

### Optimization Tips
1. Cache Maven dependencies
2. Run tests in parallel
3. Skip tests in package stage
4. Use smaller agent machines when possible
5. Archive only necessary artifacts

## Integration Points

### Database Testing
- Tests tagged with `@database` auto-connect
- Hooks manage connection lifecycle
- Results saved to test-results directory

### API Testing
- RestAssured framework for API calls
- Response validation and assertions
- Request/response logging

### File Management
- Local storage for test artifacts
- Report generation and archiving
- Log file cleanup after 7 days

## Monitoring

### Health Checks
- Build status badge in README
- Failed build notifications
- Quality gate status

### Dashboards
- Azure DevOps: Pipelines → Dashboard
- GitHub: Actions → Workflow runs

### Alerts
- Email notifications for failures
- Slack integration (optional)
- PR status checks

## Troubleshooting

### Common Issues

**Build Failures**
- Check Java version compatibility
- Verify Maven Central connectivity
- Review Maven logs
- Clear Maven cache: `mvn clean`

**Test Failures**
- Check API endpoint availability
- Verify test data setup
- Review test logs in artifacts
- Check database connections

**Report Generation**
- Verify test execution completed
- Check file write permissions
- Ensure Allure plugin installed
- Review target directory contents

**Artifact Issues**
- Check storage quota
- Verify artifact paths
- Monitor file sizes
- Review retention policies

## Best Practices

### Security
✓ Store credentials in secret managers  
✓ Use service principals/tokens  
✓ Enable branch protection  
✓ Require status checks  
✓ Rotate credentials regularly  

### Performance
✓ Cache dependencies  
✓ Run tests in parallel  
✓ Limit artifact retention  
✓ Use appropriate agent sizes  

### Maintenance
✓ Keep action versions updated  
✓ Monitor deprecated features  
✓ Test changes in feature branches  
✓ Document customizations  

### Reliability
✓ Implement retry logic  
✓ Set appropriate timeouts  
✓ Monitor pipeline health  
✓ Handle failures gracefully  

## Next Steps

1. **Choose Platform**
   - Azure Pipelines or GitHub Actions (or both)

2. **Configure Service Connections**
   - Maven repository
   - SonarQube (optional)
   - Artifact storage

3. **Add Secrets**
   - Repository credentials
   - API tokens
   - Authentication details

4. **Trigger First Build**
   - Push to main/develop
   - Monitor execution
   - Review reports

5. **Fine-tune Configuration**
   - Adjust timeouts
   - Configure notifications
   - Optimize performance

## Support & Resources

- [Azure Pipelines Docs](https://docs.microsoft.com/azure/devops/pipelines/)
- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [Maven Documentation](https://maven.apache.org/)
- [TestNG Documentation](https://testng.org/)
- [Cucumber Documentation](https://cucumber.io/)
- [Allure Report](https://docs.qameta.io/allure/)

## Version Info

- **Framework Version**: 1.0.0
- **Java**: 21
- **Maven**: 3.9.0
- **Cucumber**: 7.14.1
- **TestNG**: 7.9.0
- **RestAssured**: 5.4.0

---

**Last Updated**: December 2025  
**Maintained By**: DevOps Team  
**Support**: Check setup guides for specific platform issues

