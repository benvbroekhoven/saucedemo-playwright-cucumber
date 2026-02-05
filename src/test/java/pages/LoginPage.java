package pages;

import config.ConfigManager;

/**
 * LoginPage represents the SauceDemo login page.
 *
 * This page object encapsulates all interactions with the login page,
 * including entering credentials and handling error messages.
 *
 * Page URL: https://www.saucedemo.com/
 */
public class LoginPage extends BasePage {

    // Locators for login page elements
    // Using data-test attributes for more stable selectors
    private final String usernameInput = "[data-test='username']";
    private final String passwordInput = "[data-test='password']";
    private final String loginButton = "[data-test='login-button']";
    private final String errorMessage = "[data-test='error']";

    /**
     * Opens the login page and waits for it to be ready.
     *
     * The baseUrl is loaded from configuration (dev.properties, test.properties, etc.)
     * This allows the same tests to run against different environments.
     */
    public void open() {
        navigateTo(ConfigManager.get("baseUrl"));
        waitForVisible(usernameInput);  // Wait until page is fully loaded
    }

    /**
     * Performs login with given credentials.
     *
     * This method:
     * 1. Enters username
     * 2. Enters password
     * 3. Clicks login button
     * 4. Waits for network to be idle (for page transition)
     *
     * Note: This method doesn't verify success or failure - that's
     * handled by the calling code (flow or step definition).
     *
     * @param username The username to enter
     * @param password The password to enter
     */
    public void loginAs(String username, String password) {
        safeType(usernameInput, username);
        safeType(passwordInput, password);
        safeClick(loginButton);
        waitForNetworkIdle();  // Wait for navigation or error to appear
    }

    /**
     * Checks if an error message is currently visible.
     *
     * @return true if error message is displayed, false otherwise
     */
    public boolean isErrorVisible() {
        return safeIsVisible(errorMessage);
    }

    /**
     * Gets the text of the error message.
     *
     * Should only be called when isErrorVisible() returns true.
     *
     * @return The error message text
     */
    public String getErrorText() {
        return safeGetText(errorMessage);
    }

    /**
     * Explicitly waits for an error message to appear.
     *
     * This is useful when we expect login to fail and want to
     * wait for the error before proceeding with assertions.
     */
    public void waitForError() {
        waitForVisible(errorMessage);
    }
}