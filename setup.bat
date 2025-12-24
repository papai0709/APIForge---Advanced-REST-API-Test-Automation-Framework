@echo off
echo ========================================
echo RestAssured Framework - Quick Start
echo ========================================
echo.

echo Checking Java installation...
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 21 and add it to your PATH
    echo Download from: https://adoptium.net/
    pause
    exit /b 1
) else (
    echo ✓ Java is installed
    java -version
    echo.
)

echo Checking Maven installation...
mvn -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Apache Maven and add it to your PATH
    echo Download from: https://maven.apache.org/download.cgi
    echo.
    echo Quick Maven Setup:
    echo 1. Download Maven binary zip
    echo 2. Extract to C:\apache-maven-3.9.6
    echo 3. Add C:\apache-maven-3.9.6\bin to your PATH
    echo 4. Set MAVEN_HOME=C:\apache-maven-3.9.6
    pause
    exit /b 1
) else (
    echo ✓ Maven is installed
    mvn -version
    echo.
)

echo Installing dependencies...
echo Running: mvn clean install -DskipTests
echo.
mvn clean install -DskipTests

if errorlevel 1 (
    echo.
    echo ERROR: Maven build failed
    echo Please check the error messages above
    pause
    exit /b 1
) else (
    echo.
    echo ========================================
    echo ✓ Setup completed successfully!
    echo ========================================
    echo.
    echo You can now:
    echo 1. Open the project in IntelliJ IDEA
    echo 2. Run tests with: mvn test
    echo 3. Run smoke tests with: mvn test -Dcucumber.filter.tags="@smoke"
    echo.
    echo For detailed setup instructions, see:
    echo - README.md
    echo - IntelliJ-SETUP.md
    echo - SETUP.md
    echo.
)

pause
