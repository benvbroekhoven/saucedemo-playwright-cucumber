package pages;

import config.ConfigManager;

public class LoginPage extends BasePage {

    private final String usernameInput = "[data-test='username']";
    private final String passwordInput = "[data-test='password']";
    private final String loginButton = "[data-test='login-button']";
    private final String errorMessage = "[data-test='error']";

    public void open() {
        navigateTo(ConfigManager.get("baseUrl"));
        waitForVisible(usernameInput);
    }

    public void loginAs(String username, String password) {
        safeType(usernameInput, username);
        safeType(passwordInput, password);
        safeClick(loginButton);
        waitForNetworkIdle();
    }

    public boolean isErrorVisible() {
        return safeIsVisible(errorMessage);
    }

    public String getErrorText() {
        return safeGetText(errorMessage);
    }

    public void waitForError() {
        waitForVisible(errorMessage);
    }
}
