# GitHub Actions CI/CD Setup Guide

## Overview
This guide provides instructions for setting up GitHub Actions for the RestAssured Test Automation Framework.

## Quick Start

### 1. Automatic Setup
GitHub Actions is automatically enabled when you have workflow files in `.github/workflows/` directory.

### 2. Verify Setup
1. Go to repository
2. Click "Actions" tab
3. View available workflows
4. Click "CI/CD Pipeline" workflow

### 3. First Run
- Trigger: Push to main/develop or create pull request
- Check "Actions" tab for execution status

## Workflow Overview

### Triggers
```yaml
on:
  push:
    branches: [ main, develop, feature/* ]
  pull_request:
    branches: [ main, develop ]
  schedule:
    - cron: '0 2 * * *'  # Daily at 2 AM UTC
```

### Jobs (Parallel Execution)
1. **build** - Compiles project
2. **unit-tests** - Unit test suite
3. **smoke-tests** - @smoke tagged scenarios
4. **regression-tests** - @regression tagged scenarios
5. **code-quality** - SonarQube & dependency check
6. **reports** - Generate & publish reports
7. **package** - Create and deploy artifacts

## Configuration

### Required Secrets
Go to Settings → Secrets and variables → Actions

```yaml
# Required
GITHUB_TOKEN: (Auto-provided by GitHub)

# Optional
SONAR_TOKEN: SonarCloud token
MAVEN_USERNAME: Maven repository username
MAVEN_PASSWORD: Maven repository password
```

### Repository Settings

#### Enable Required Status Checks
1. Settings → Branches → Add rule
2. Require status checks:
   - build
   - unit-tests
   - code-quality

#### Concurrency Settings
Pipeline automatically cancels in-progress runs when new push/PR detected
```yaml
concurrency:
  group: ${{ github.workflow }}-${{ github.ref }}
  cancel-in-progress: true
```

## Environment Setup

### Java Version
- Default: Java 21
- Distribution: Temurin
- Cache: Maven dependencies (local .m2)

### Maven Configuration
```yaml
MAVEN_OPTS: -Xmx3072m
```

### Cache Strategy
Automatically caches:
- Maven dependencies
- Build artifacts
- Test results

## Job Dependencies

```
build
  ├── unit-tests
  │   └── code-quality
  │       └── package
  ├── smoke-tests
  │   └── reports
  └── regression-tests
      └── reports
```

## Monitoring & Notifications

### View Workflow Status
1. Actions tab shows all workflow runs
2. Click run for detailed logs
3. Download artifacts from run page

### Branch Protection Rules
Configure status checks required for merging:
```
- build: success
- unit-tests: success
- code-quality: success
```

### Email Notifications
GitHub sends automatic notifications:
- Workflow failures
- Workflow completions
- PR status checks

### Slack Integration
Add Slack notification step:
```yaml
- name: Slack Notification
  uses: slackapi/slack-github-action@v1.25
  with:
    webhook-url: ${{ secrets.SLACK_WEBHOOK }}
    payload: |
      {
        "text": "GitHub Action build result: ${{ job.status }}"
      }
```

## Artifact Management

### Published Artifacts
1. **build-artifacts** - Compiled JAR and classes
2. **unit-test-results** - JUnit test reports
3. **smoke-test-results** - Smoke test results and logs
4. **regression-test-results** - Regression test results and logs
5. **allure-report** - Allure test report
6. **test-logs** - Application logs
7. **RestAssuredFramework** - Final packaged artifact

### Artifact Retention
- Default: 90 days
- Configure in workflow or repository settings

### Download Artifacts
1. Go to workflow run
2. Click artifact name
3. Download ZIP file

## Advanced Configuration

### Custom Environment Variables
Add to workflow file:
```yaml
env:
  CUSTOM_VAR: value
  API_BASE_URL: https://api.example.com
```

### Matrix Testing
Test across multiple Java versions:
```yaml
strategy:
  matrix:
    java-version: ['20', '21', '22']
```

### Conditional Steps
Run steps based on conditions:
```yaml
if: github.ref == 'refs/heads/main'
if: github.event_name == 'push'
if: always()  # Run regardless of previous step status
```

### Manual Workflow Trigger
```yaml
on:
  workflow_dispatch:
    inputs:
      tag:
        description: 'Test tag to execute'
        required: false
        default: '@smoke'
```

## Integration Examples

### Slack Notifications
Create `.github/workflows/slack-notify.yml`:
```yaml
name: Slack Notification
on: workflow_run
jobs:
  notify:
    runs-on: ubuntu-latest
    steps:
      - uses: slackapi/slack-github-action@v1
        with:
          webhook-url: ${{ secrets.SLACK_WEBHOOK }}
          payload: |
            {
              "text": "Build completed: ${{ github.repository }}"
            }
```

### SonarCloud Integration
```yaml
- name: SonarCloud Scan
  uses: SonarSource/sonarcloud-github-action@master
  env:
    GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
    SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
```

### Deploy to Release
```yaml
- name: Create Release
  if: startsWith(github.ref, 'refs/tags/')
  uses: softprops/action-gh-release@v1
  with:
    files: target/restassured-framework-*.jar
```

## Best Practices

### Performance Optimization
1. Use job parallelization
2. Cache dependencies
3. Skip tests in package stage
4. Use continue-on-error for non-critical jobs

### Security
1. Use GitHub secrets for credentials
2. Enable branch protection
3. Require status checks
4. Review pull request changes

### Maintenance
1. Keep GitHub Actions updated
2. Monitor deprecated features
3. Test workflow changes in feature branches
4. Document custom configurations

## Troubleshooting

### Build Failures
- Check "Logs" in failed job
- Verify Java version compatibility
- Check Maven Central connectivity
- Review build output for errors

### Test Failures
- Download test artifacts
- Check application logs
- Verify API endpoints
- Review test configurations

### Artifact Issues
- Check storage quota
- Verify artifact paths exist
- Check file permissions
- Monitor artifact size

### Caching Issues
- Clear cache: Settings → Actions → Clear all caches
- Verify cache keys
- Check cache size limits

## Comparing Azure Pipelines vs GitHub Actions

| Feature | Azure Pipelines | GitHub Actions |
|---------|-----------------|----------------|
| Pricing | Free up to 1800 min/month | Free for public repos |
| Syntax | YAML | YAML |
| Parallelization | Matrix/parallel jobs | Matrix strategy |
| Caching | Native support | Native support |
| Artifacts | Long-term storage | 90-day default |
| Third-party | Extensive marketplace | Community actions |
| Complexity | Medium | Low-Medium |

## Migration from Azure Pipelines

If migrating from Azure Pipelines:
1. Copy pipeline logic to GitHub workflow
2. Adjust task syntax (Azure → GitHub Actions)
3. Configure secrets in GitHub
4. Update branch protection rules
5. Test on feature branch
6. Delete Azure pipeline

## References
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Workflow Syntax](https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions)
- [Actions Marketplace](https://github.com/marketplace?type=actions)
- [Setup Java Action](https://github.com/actions/setup-java)
- [Maven Documentation](https://maven.apache.org/)

