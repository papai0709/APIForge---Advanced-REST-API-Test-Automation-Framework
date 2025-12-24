@echo off
REM Azure Pipeline & GitHub Actions Quick Setup Script for Windows

setlocal enabledelayedexpansion

echo.
echo ==========================================
echo RestAssured Framework - CI/CD Setup
echo ==========================================
echo.

REM Check if we're in the right directory
if not exist "pom.xml" (
    echo.
    echo Error: pom.xml not found. Please run this script from the project root directory.
    pause
    exit /b 1
)

echo This script will help you set up CI/CD pipelines for your RestAssured Framework.
echo.
echo Choose your setup option:
echo 1) Azure Pipelines
echo 2) GitHub Actions
echo 3) Both (Azure + GitHub)
echo 4) View Documentation
echo 5) Exit
echo.

set /p choice="Enter your choice (1-5): "

if "%choice%"=="1" (
    cls
    echo.
    echo ================================
    echo AZURE PIPELINES SETUP
    echo ================================
    echo.

    echo Checking Azure Pipelines File...
    if exist "azure-pipelines.yml" (
        echo [OK] azure-pipelines.yml found
    ) else (
        echo [ERROR] azure-pipelines.yml not found
    )

    echo.
    echo AZURE PIPELINES SETUP CHECKLIST:
    echo.
    echo 1. Create Azure DevOps Project
    echo    - Go to https://dev.azure.com
    echo    - Create new organization or use existing
    echo    - Create new project
    echo.

    echo 2. Connect GitHub Repository
    echo    - Project Settings ^> Service connections
    echo    - New service connection ^> GitHub
    echo    - Authorize GitHub access
    echo.

    echo 3. Create New Pipeline
    echo    - Pipelines ^> New pipeline
    echo    - Select GitHub as source
    echo    - Select your repository
    echo    - Select 'Existing Azure Pipelines YAML file'
    echo    - Choose 'azure-pipelines.yml' from root
    echo.

    echo 4. Configure Variables
    echo    - Pipeline ^> More options ^> Variables
    echo    - Add:
    echo      * JAVA_VERSION: 21
    echo      * MVN_VERSION: 3.9.0
    echo      * BUILD_CONFIGURATION: Release
    echo.

    echo 5. Configure Service Connections (Optional)
    echo    - For SonarQube: Project Settings ^> Service connections ^> SonarQube
    echo    - For Maven Repo: Project Settings ^> Service connections ^> Maven
    echo.

    echo 6. Run Pipeline
    echo    - Push to main/develop branch
    echo    - Or click 'Run pipeline' manually
    echo.

    echo Full Setup Guide: AZURE-PIPELINE-SETUP.md
    echo.
    echo To view the setup guide, run:
    echo   type AZURE-PIPELINE-SETUP.md
    echo   or open it in your text editor

    pause
    exit /b 0
)

if "%choice%"=="2" (
    cls
    echo.
    echo ================================
    echo GITHUB ACTIONS SETUP
    echo ================================
    echo.

    echo Checking GitHub Actions Workflow...
    if exist ".github\workflows\ci-cd.yml" (
        echo [OK] .github\workflows\ci-cd.yml found
    ) else (
        echo [ERROR] .github\workflows\ci-cd.yml not found
    )

    echo.
    echo GITHUB ACTIONS SETUP CHECKLIST:
    echo.
    echo 1. Push Code to GitHub
    echo    - If not already pushed:
    echo    - git push origin main
    echo.

    echo 2. Enable GitHub Actions
    echo    - Repository ^> Actions
    echo    - GitHub Actions should be enabled by default
    echo.

    echo 3. Configure Secrets (Optional)
    echo    - Repository ^> Settings ^> Secrets and variables ^> Actions
    echo    - Add secrets if needed:
    echo      * SONAR_TOKEN (for SonarCloud)
    echo      * MAVEN_USERNAME (for Maven repo)
    echo      * MAVEN_PASSWORD (for Maven repo)
    echo.

    echo 4. Enable Branch Protection (Recommended)
    echo    - Repository ^> Settings ^> Branches
    echo    - Add rule for 'main' and 'develop'
    echo    - Require status checks (build, unit-tests)
    echo.

    echo 5. Trigger First Workflow
    echo    - Push to main or develop branch
    echo    - Go to Actions tab to monitor
    echo    - View workflow status and logs
    echo.

    echo 6. Configure Notifications (Optional)
    echo    - Repository ^> Settings ^> Notifications
    echo    - Choose notification preferences
    echo.

    echo Full Setup Guide: GITHUB-ACTIONS-SETUP.md
    echo.
    echo To view the setup guide, run:
    echo   type GITHUB-ACTIONS-SETUP.md
    echo   or open it in your text editor

    pause
    exit /b 0
)

if "%choice%"=="3" (
    cls
    echo.
    echo ================================
    echo DUAL SETUP: AZURE + GITHUB
    echo ================================
    echo.

    echo You've chosen to set up both Azure Pipelines and GitHub Actions!
    echo.
    echo ADVANTAGES:
    echo   * Redundancy - one fails, other catches it
    echo   * Multi-team support - different teams use different tools
    echo   * Gradual migration - move from one to other at your pace
    echo   * Flexibility - choose best tool for different workflows
    echo.

    echo CONSIDERATIONS:
    echo   - Both pipelines will execute on code changes
    echo   - May consume more CI/CD credits
    echo   - Requires managing two separate configurations
    echo.

    echo SETUP STEPS:
    echo.
    echo Azure Pipelines Setup:
    echo   1. Go to https://dev.azure.com
    echo   2. Create project and connect GitHub
    echo   3. Use azure-pipelines.yml from root
    echo   4. Configure variables and service connections
    echo.

    echo GitHub Actions Setup:
    echo   1. Push code to GitHub repository
    echo   2. Workflow auto-activates from .github\workflows\ci-cd.yml
    echo   3. Configure secrets in repository settings
    echo   4. Enable branch protection rules
    echo.

    echo Full Setup Guides:
    echo   - AZURE-PIPELINE-SETUP.md
    echo   - GITHUB-ACTIONS-SETUP.md

    pause
    exit /b 0
)

if "%choice%"=="4" (
    cls
    echo.
    echo ================================
    echo DOCUMENTATION
    echo ================================
    echo.

    echo Available Documentation:
    echo.
    echo 1. CI-CD-SETUP.md
    echo    - Overview of both platforms
    echo    - Feature comparison
    echo    - Pipeline overview
    echo    - Performance metrics
    echo.

    echo 2. AZURE-PIPELINE-SETUP.md
    echo    - Azure Pipelines specific setup
    echo    - Service connections configuration
    echo    - Stage configuration details
    echo    - Environment-specific setup
    echo.

    echo 3. GITHUB-ACTIONS-SETUP.md
    echo    - GitHub Actions specific setup
    echo    - Workflow configuration
    echo    - Integration examples
    echo    - Troubleshooting guide
    echo.

    set /p doc_choice="Which document would you like to view? (1-3): "

    if "%doc_choice%"=="1" (
        type CI-CD-SETUP.md | more
    ) else if "%doc_choice%"=="2" (
        type AZURE-PIPELINE-SETUP.md | more
    ) else if "%doc_choice%"=="3" (
        type GITHUB-ACTIONS-SETUP.md | more
    ) else (
        echo Invalid choice
    )

    pause
    exit /b 0
)

if "%choice%"=="5" (
    echo Exiting setup script. Goodbye!
    exit /b 0
)

echo.
echo Error: Invalid choice. Please run the script again and select 1-5.
pause
exit /b 1

