package hooks;

import drivers.PlaywrightFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.qameta.allure.Allure;

/**
 * PlaywrightHooks manages test lifecycle events.
 *
 * Cucumber hooks are methods that run before/after scenarios to handle:
 * - Test setup (initializing browser, clearing data, etc.)
 * - Test cleanup (closing browser, capturing screenshots, etc.)
 * - Test reporting (logging, attaching evidence, etc.)
 *
 * These hooks run automatically for every scenario, ensuring consistent
 * test environment setup and cleanup.
 */
public class PlaywrightHooks {

    /**
     * Runs before each scenario to set up the test environment.
     *
     * This hook:
     * 1. Logs the scenario name to Allure report
     * 2. Initializes the Playwright page (browser)
     *
     * The @Before annotation tells Cucumber to run this method before
     * every scenario in every feature file.
     *
     * @param scenario The Cucumber scenario that's about to run
     */
    @Before
    public void setup(io.cucumber.java.Scenario scenario) {
        // Log scenario start in Allure report for better traceability
        Allure.step("Starting scenario: " + scenario.getName());

        // Initialize browser - this is lazy, so browser only starts if needed
        PlaywrightFactory.getPage();
    }

    /**
     * Runs after each scenario to clean up and capture test evidence.
     *
     * This hook:
     * 1. Takes a screenshot if the test failed (for debugging)
     * 2. Attaches the screenshot to the Allure report
     * 3. Closes the browser and frees resources
     *
     * The @After annotation tells Cucumber to run this method after
     * every scenario, regardless of pass/fail status.
     *
     * Screenshots are only captured on failure to:
     * - Save disk space
     * - Reduce report size
     * - Focus attention on failures
     *
     * @param scenario The Cucumber scenario that just completed
     */
    @After
    public void teardown(io.cucumber.java.Scenario scenario) {
        // Capture evidence if test failed
        if (scenario.isFailed()) {
            // Take screenshot of the current page state
            byte[] screenshot = PlaywrightFactory.getPage().screenshot();

            // Attach screenshot to Allure report
            // This makes debugging failures much easier
            Allure.addAttachment(
                    "Failure Screenshot",           // Attachment name
                    "image/png",                    // MIME type
                    new java.io.ByteArrayInputStream(screenshot),  // Image data
                    "png"                           // File extension
            );
        }

        // Always close browser to prevent resource leaks
        // This ensures each scenario starts with a fresh browser instance
        PlaywrightFactory.close();
    }
}