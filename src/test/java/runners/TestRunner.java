package runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

/**
 * TestRunner is the entry point for running Cucumber tests.
 *
 * This class configures and executes all feature files using JUnit.
 * It doesn't contain test logic itself - it just tells Cucumber:
 * - Where to find feature files
 * - Where to find step definitions
 * - How to format and report results
 *
 * Run this class to execute all tests in the project.
 */
@RunWith(Cucumber.class)  // Tells JUnit to use Cucumber's test runner
@CucumberOptions(
        // Location of feature files (Gherkin scenarios)
        features = "src/test/resources/features",

        // Packages containing step definitions and hooks
        // Cucumber scans these packages to find @Given, @When, @Then, @Before, @After
        glue = {"steps", "hooks"},

        // Test reporting and formatting plugins
        plugin = {
                "pretty",                                        // Console output with colors
                "summary",                                       // Summary statistics at the end
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"  // Allure HTML report
        },

        // Makes console output more readable by removing ANSI color codes
        // when running in environments that don't support colors
        monochrome = true

        // Additional useful options (commented out, can be enabled as needed):
        // tags = "@smoke",                    // Run only scenarios tagged with @smoke
        // dryRun = true,                      // Check if all steps have definitions without running tests
        // snippets = SnippetType.CAMELCASE,  // Generate step definitions in camelCase
        // strict = true                       // Fail if there are undefined or pending steps
)
public class TestRunner {
    // Empty class - all configuration is done via @CucumberOptions annotation
    // JUnit will use the Cucumber runner to find and execute tests
}