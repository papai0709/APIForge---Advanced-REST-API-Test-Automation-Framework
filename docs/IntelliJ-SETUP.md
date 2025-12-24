# IntelliJ IDEA Setup Instructions

## Opening the Project in IntelliJ IDEA

### Method 1: Import as Maven Project
1. Open IntelliJ IDEA
2. Click "Open" or "Import Project"
3. Navigate to the `Api Framework` folder
4. Select the `pom.xml` file
5. Click "Open as Project"
6. Choose "Import Maven project automatically" when prompted
7. Wait for Maven to download dependencies

### Method 2: Open Existing Project
1. Open IntelliJ IDEA
2. Click "Open"
3. Navigate to and select the `Api Framework` folder
4. Click "OK"
5. IntelliJ will automatically detect it as a Maven project

## Project Configuration

### 1. SDK Configuration
- Go to `File` → `Project Structure` → `Project`
- Set Project SDK to Java 21
- Set Project language level to "21 - Pattern matching for switch"

### 2. Maven Configuration
- Go to `File` → `Settings` → `Build, Execution, Deployment` → `Build Tools` → `Maven`
- Ensure Maven home directory is set correctly
- Check "Import Maven projects automatically"
- Set "Automatically download" for Sources and Documentation

### 3. Annotation Processing
- Go to `File` → `Settings` → `Build, Execution, Deployment` → `Compiler` → `Annotation Processors`
- Enable annotation processing
- Set "Store generated sources relative to" to "Module content root"

## Running Tests in IntelliJ

### Pre-configured Run Configurations

The project includes these run configurations:

1. **Maven Clean Install** - Build the project
2. **Maven Test** - Run all tests
3. **Smoke Tests** - Run only @smoke tagged tests
4. **Regression Tests** - Run only @regression tagged tests
5. **Run All Cucumber Tests** - TestNG runner for Cucumber tests

### Running Individual Tests

#### Running Feature Files
1. Right-click on a `.feature` file
2. Select "Run 'Feature: <name>'"

#### Running Step Definitions
1. Right-click on a step definition class
2. Select "Run '<ClassName>'"

#### Running Specific Scenarios
1. Click on a specific scenario in a feature file
2. Click the green arrow next to the scenario
3. Select "Run Scenario"

### Running with Tags
1. Open Run Configuration
2. Add VM options: `-Dcucumber.filter.tags="@smoke"`
3. Run the configuration

## IntelliJ Plugins (Recommended)

### Essential Plugins
1. **Cucumber for Java** - BDD support
2. **TestNG** - Test framework support
3. **Maven Helper** - Maven management
4. **Lombok** - Code generation (if using Lombok)
5. **SonarLint** - Code quality analysis

### Installation
1. Go to `File` → `Settings` → `Plugins`
2. Search for plugin name
3. Click "Install"
4. Restart IntelliJ when prompted

## Code Formatting

### Import Code Style
1. Go to `File` → `Settings` → `Editor` → `Code Style`
2. Click gear icon → `Import Scheme` → `IntelliJ IDEA code style XML`
3. Use provided code style settings

### Auto-formatting
- `Ctrl+Alt+L` (Windows/Linux) - Format current file
- `Ctrl+Alt+Shift+L` (Windows/Linux) - Format with options

## Debugging

### Debug Configurations
1. Set breakpoints by clicking in the gutter
2. Right-click on test class/method
3. Select "Debug '<TestName>'"

### Debug Features
- **Variables view** - Inspect variable values
- **Watches** - Monitor specific expressions
- **Call stack** - View method call hierarchy
- **Console** - View test output

## Project Explorer

### Source Folders
- `src/main/java` - Main source code (marked as Sources Root)
- `src/test/java` - Test source code (marked as Test Sources Root)
- `src/main/resources` - Main resources (marked as Resources Root)
- `src/test/resources` - Test resources (marked as Test Resources Root)

### Excluded Folders
- `target/` - Maven build output
- `test-output/` - Test artifacts
- `logs/` - Log files

## IntelliJ-Specific Features

### Live Templates
Create custom live templates for common patterns:
1. Go to `File` → `Settings` → `Editor` → `Live Templates`
2. Create templates for:
   - Step definitions
   - POJO classes
   - POM methods

### File Templates
Customize file templates:
1. Go to `File` → `Settings` → `Editor` → `File and Code Templates`
2. Modify Java Class template to include package structure

### Code Generation
- `Alt+Insert` - Generate constructors, getters, setters
- `Ctrl+O` - Override methods
- `Ctrl+I` - Implement methods

## Troubleshooting

### Common Issues

#### Maven Dependencies Not Loading
1. Right-click on `pom.xml`
2. Select "Maven" → "Reload project"
3. Or use Maven tool window → Refresh

#### Tests Not Running
1. Check Project Structure → Modules
2. Ensure test folders are marked correctly
3. Verify TestNG/JUnit plugin is enabled

#### Cucumber Steps Not Recognized
1. Install "Cucumber for Java" plugin
2. Mark `src/test/resources/features` as Resources Root
3. Configure Cucumber settings in Run Configuration

#### Import Issues
1. Check if auto-import is enabled: `File` → `Settings` → `Editor` → `General` → `Auto Import`
2. Use `Alt+Enter` for quick fixes
3. Use `Ctrl+Shift+O` to optimize imports

### Performance Optimization
1. Increase heap size: `Help` → `Change Memory Settings`
2. Exclude large folders from indexing
3. Enable power save mode if needed

## Database Tools (IntelliJ Ultimate)

If using IntelliJ Ultimate:
1. Go to `View` → `Tool Windows` → `Database`
2. Add MySQL/PostgreSQL data source
3. Use connection details from `application.properties`
4. Run SQL queries directly from IDE

## Version Control Integration

### Git Integration
1. IntelliJ automatically detects Git repository
2. Use `VCS` menu for Git operations
3. View changes in `Version Control` tool window
4. Use built-in merge tool for conflicts

## Reports and Output

### Test Results
- Test results appear in `Run` tool window
- Detailed reports in `target/` folder
- Use IntelliJ's test runner for better visualization

### Console Output
- All test logs visible in Console tab
- Filter logs by level
- Search through console output

This setup ensures optimal IntelliJ IDEA integration with your RestAssured framework!
