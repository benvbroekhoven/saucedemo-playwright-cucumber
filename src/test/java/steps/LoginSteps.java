package steps;

import flows.LoginFlow;
import pages.InventoryPage;
import org.assertj.core.api.Assertions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

/**
 * LoginSteps maps Gherkin steps to login functionality.
 *
 * Step definition classes act as the glue between Cucumber feature files
 * and the automation code. They translate plain English steps into
 * method calls on flows and page objects.
 *
 * This class handles all login-related Gherkin steps.
 */
public class LoginSteps {

    private final LoginFlow loginFlow = new LoginFlow();
    private final InventoryPage inventoryPage = new InventoryPage();

    /**
     * Step: "Given/When I login with username {string} and password {string}"
     *
     * This step performs a successful login. It supports both @Given and @When
     * to handle different Gherkin contexts:
     * - @Given: Used in Background sections for test setup
     * - @When: Used for login as the main action being tested
     *
     * Example usage in feature files:
     *   Given I login with username "standard_user" and password "secret_sauce"
     *   When I login with username "standard_user" and password "secret_sauce"
     *
     * @param username The username to login with
     * @param password The password to login with
     */

    @Given("I login with username {string} and password {string}")
    public void i_login_with_username_and_password(String username, String password) {
        loginFlow.loginExpectingSuccess(username, password);
    }

    /**
     * Step: "When I try to login with username {string} and password {string}"
     *
     * This step attempts a login that is expected to fail.
     * The word "try" signals that we're testing invalid credentials.
     *
     * Example usage in feature files:
     *   When I try to login with username "invalid_user" and password "wrong_pass"
     *
     * @param username The username to attempt login with
     * @param password The password to attempt login with
     */
    @When("I try to login with username {string} and password {string}")
    public void i_try_to_login_with_username_and_password(String username, String password) {
        loginFlow.loginExpectingFailure(username, password);
    }

    /**
     * Step: "Then I should see the products page"
     *
     * This assertion verifies that login was successful by checking
     * if the inventory/products page is now visible.
     *
     * Uses AssertJ for fluent, readable assertions with clear failure messages.
     *
     * Example usage in feature files:
     *   Then I should see the products page
     */
    @Then("I should see the products page")
    public void i_should_see_the_products_page() {
        Assertions.assertThat(inventoryPage.isLoaded())
                .as("Inventory page should be visible after successful login")
                .isTrue();
    }

    /**
     * Step: "Then I should see a login error message"
     *
     * This assertion verifies that login failed by checking if an
     * error message is displayed on the login page.
     *
     * Example usage in feature files:
     *   Then I should see a login error message
     */
    @Then("I should see a login error message")
    public void i_should_see_a_login_error_message() {
        Assertions.assertThat(loginFlow.loginPage().isErrorVisible())
                .as("Error message should be visible after failed login")
                .isTrue();
    }
}