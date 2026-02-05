package steps;

import flows.CheckoutFlow;
import org.assertj.core.api.Assertions;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

/**
 * CheckoutSteps maps Gherkin steps to checkout functionality.
 *
 * This class handles all checkout-related Gherkin steps.
 * It uses CheckoutFlow to orchestrate the complete purchase process,
 * keeping the step definitions simple and readable.
 */
public class CheckoutSteps {

    private final CheckoutFlow checkoutFlow = new CheckoutFlow();

    /**
     * Step: "When I buy the product {string}"
     *
     * Completes the entire purchase flow for a product:
     * 1. Adds the product to cart
     * 2. Goes to cart
     * 3. Proceeds to checkout
     * 4. Fills shipping information
     * 5. Reviews order
     * 6. Completes purchase
     *
     * This is a high-level step that encapsulates the entire checkout
     * workflow, making feature files more readable for end-to-end scenarios.
     *
     * Example usage in feature files:
     *   When I buy the product "Sauce Labs Backpack"
     *
     * @param itemName The exact name of the product to purchase
     */
    @When("I buy the product {string}")
    public void i_buy_the_product(String itemName) {
        checkoutFlow.buyItem(itemName);
    }

    /**
     * Step: "Then I should see the order confirmation"
     *
     * Verifies that the purchase completed successfully by checking
     * if the order confirmation page is visible.
     *
     * This is the final verification that the entire checkout process
     * worked correctly.
     *
     * Example usage in feature files:
     *   Then I should see the order confirmation
     *
     * Uses AssertJ for clear, readable assertions with descriptive messages.
     */
    @Then("I should see the order confirmation")
    public void i_should_see_the_order_confirmation() {
        Assertions.assertThat(checkoutFlow.completePage().isLoaded())
                .as("Order confirmation page should be visible after successful purchase")
                .isTrue();
    }
}