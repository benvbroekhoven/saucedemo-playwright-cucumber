package steps;

import flows.LoginFlow;
import pages.InventoryPage;
import org.assertj.core.api.Assertions;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class LoginSteps {

    private final LoginFlow loginFlow = new LoginFlow();
    private final InventoryPage inventoryPage = new InventoryPage();

    @When("I login with username {string} and password {string}")
    public void i_login_with_username_and_password(String username, String password) {
        loginFlow.loginExpectingSuccess(username, password);
    }

    @When("I try to login with username {string} and password {string}")
    public void i_try_to_login_with_username_and_password(String username, String password) {
        loginFlow.loginExpectingFailure(username, password);
    }

    @Then("I should see the products page")
    public void i_should_see_the_products_page() {
        Assertions.assertThat(inventoryPage.isLoaded())
                .as("Inventory page should be visible")
                .isTrue();
    }

    @Then("I should see a login error message")
    public void i_should_see_a_login_error_message() {
        Assertions.assertThat(loginFlow.loginPage().isErrorVisible())
                .as("Error message should be visible")
                .isTrue();
    }
}
