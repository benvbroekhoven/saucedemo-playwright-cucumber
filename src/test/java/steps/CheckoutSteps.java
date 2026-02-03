package steps;

import flows.CheckoutFlow;
import org.assertj.core.api.Assertions;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class CheckoutSteps {

    private final CheckoutFlow checkoutFlow = new CheckoutFlow();

    @When("I buy the product {string}")
    public void i_buy_the_product(String itemName) {
        checkoutFlow.buyItem(itemName);
    }

    @Then("I should see the order confirmation")
    public void i_should_see_the_order_confirmation() {
        Assertions.assertThat(checkoutFlow.completePage().isLoaded())
                .as("Order confirmation page should be visible")
                .isTrue();
    }
}
