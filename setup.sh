#!/bin/bash

echo "========================================"
echo "RestAssured Framework - Quick Start"
echo "========================================"
echo

echo "Checking Java installation..."
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java 21 and add it to your PATH"
    echo "Download from: https://adoptium.net/"
    exit 1
else
    echo "✓ Java is installed"
    java -version
    echo
fi

echo "Checking Maven installation..."
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Apache Maven and add it to your PATH"
    echo "Download from: https://maven.apache.org/download.cgi"
    echo
    echo "Quick Maven Setup (macOS/Linux):"
    echo "1. Download Maven binary tar.gz"
    echo "2. Extract to /opt/apache-maven-3.9.6"
    echo "3. Add export PATH=/opt/apache-maven-3.9.6/bin:\$PATH to ~/.bashrc or ~/.zshrc"
    echo "4. Add export MAVEN_HOME=/opt/apache-maven-3.9.6"
    exit 1
else
    echo "✓ Maven is installed"
    mvn -version
    echo
fi

echo "Installing dependencies..."
echo "Running: mvn clean install -DskipTests"
echo
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo
    echo "ERROR: Maven build failed"
    echo "Please check the error messages above"
    exit 1
else
    echo
    echo "========================================"
    echo "✓ Setup completed successfully!"
    echo "========================================"
    echo
    echo "You can now:"
    echo "1. Open the project in IntelliJ IDEA"
    echo "2. Run tests with: mvn test"
    echo "3. Run smoke tests with: mvn test -Dcucumber.filter.tags=\"@smoke\""
    echo
    echo "For detailed setup instructions, see:"
    echo "- README.md"
    echo "- IntelliJ-SETUP.md"  
    echo "- SETUP.md"
    echo
fi
