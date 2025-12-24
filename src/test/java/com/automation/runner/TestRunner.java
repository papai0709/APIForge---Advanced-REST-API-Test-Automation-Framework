package com.automation.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * TestNG runner for Cucumber tests
 * Configures Cucumber options and enables parallel execution
 */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.automation.stepdefinitions", "com.automation.hooks"},
    tags = "@smoke or @regression",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/html",
        "json:target/cucumber-reports/Cucumber.json",
        "junit:target/surefire-reports/Cucumber.xml",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
    },
    monochrome = true,
    publish = false
)
public class TestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
