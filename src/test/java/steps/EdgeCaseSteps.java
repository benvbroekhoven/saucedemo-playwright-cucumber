package steps;

import flows.EdgeCaseFlow;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;

/**
 * EdgeCaseSteps maps Gherkin steps to edge case testing functionality.
 *
 * These steps test boundary conditions, error states, and unusual
 * user behaviors to ensure the application handles them gracefully.
 */
public class EdgeCaseSteps {

    private final EdgeCaseFlow edgeCaseFlow = new EdgeCaseFlow();
    private String lastErrorMessage = null;

    // -------------------- EMPTY CART CHECKOUT --------------------

    /**
     * Step: "When I attempt to checkout with an empty cart"
     *
     * Tries to proceed to checkout without any items in cart.
     * Most e-commerce sites should prevent this or show a warning.
     */
    @When("I attempt to checkout with an empty cart")
    public void i_attempt_to_checkout_with_an_empty_cart() {
        boolean reachedCheckout = edgeCaseFlow.attemptEmptyCartCheckout();

        // Store result for assertion - we expect this to be blocked
        // If we reached checkout with empty cart, that's potentially a bug
        lastErrorMessage = reachedCheckout ? "REACHED_CHECKOUT" : "BLOCKED";
    }

    /**
     * Step: "Then I should be prevented from checking out"
     *
     * Verifies that the system blocked checkout with empty cart.
     */
    @Then("I should be prevented from checking out")
    public void i_should_be_prevented_from_checking_out() {
        Assertions.assertThat(lastErrorMessage)
                .as("System should block checkout with empty cart")
                .isEqualTo("BLOCKED");
    }

    // -------------------- DUPLICATE PRODUCTS --------------------

    /**
     * Step: "When I add {string} to the cart {int} times"
     *
     * Attempts to add the same product multiple times.
     * Tests how the system handles duplicate additions.
     *
     * @param productName Product to add repeatedly
     * @param times Number of times to attempt adding
     */
    @When("I add {string} to the cart {int} times")
    public void i_add_to_the_cart_times(String productName, int times) {
        int finalCount = edgeCaseFlow.addSameProductMultipleTimes(productName, times);
        System.out.println("Added '" + productName + "' " + times + " times, final count: " + finalCount);
    }

    /**
     * Step: "Then the cart should contain only {int} of {string}"
     *
     * Verifies that duplicate additions were handled correctly
     * (usually by ignoring duplicates or incrementing quantity).
     *
     * @param expectedCount Expected quantity in cart
     * @param productName Product name
     */
    @Then("the cart should contain only {int} of {string}")
    public void the_cart_should_contain_only_of(int expectedCount, String productName) {
        // Navigate to cart and verify
        edgeCaseFlow.getCartPage().navigateToCart();
        int actualCount = edgeCaseFlow.getCartPage().getProductQuantity(productName);

        Assertions.assertThat(actualCount)
                .as("Cart should contain %d of '%s' but contained %d", expectedCount, productName, actualCount)
                .isEqualTo(expectedCount);
    }

    // -------------------- RAPID OPERATIONS --------------------

    /**
     * Step: "When I rapidly add and remove {string} {int} times"
     *
     * Tests race conditions by quickly adding and removing the same item.
     *
     * @param productName Product to test
     * @param cycles Number of rapid cycles
     */
    @When("I rapidly add and remove {string} {int} times")
    public void i_rapidly_add_and_remove_times(String productName, int cycles) {
        boolean finalInCart = edgeCaseFlow.rapidAddRemoveCycles(productName, cycles);
        System.out.println("After " + cycles + " cycles, product in cart: " + finalInCart);
    }

    /**
     * Step: "Then the final cart state should be {string}"
     *
     * @param expectedState "empty" or "contains item"
     */
    @Then("the final cart state should be {string}")
    public void the_final_cart_state_should_be(String expectedState) {
        edgeCaseFlow.getInventoryPage().goToCart();
        boolean isEmpty = edgeCaseFlow.getCartPage().isEmpty();

        if (expectedState.equals("empty")) {
            Assertions.assertThat(isEmpty).as("Cart should be empty").isTrue();
        } else {
            Assertions.assertThat(isEmpty).as("Cart should contain items").isFalse();
        }
    }

    // -------------------- INVALID CHECKOUT DATA --------------------

    /**
     * Step: "When I try to checkout with:"
     *
     * Attempts checkout with invalid or edge case data from a data table.
     *
     * Data table format:
     *   | Field      | Value        |
     *   | First Name | [empty]      |
     *   | Last Name  | Smith        |
     *   | Postal Code| abc          |
     */
    @When("I try to checkout with:")
    public void i_try_to_checkout_with(io.cucumber.datatable.DataTable dataTable) {
        String firstName = dataTable.cell(1, 1);  // Row 1, Column 1
        String lastName = dataTable.cell(2, 1);
        String postalCode = dataTable.cell(3, 1);

        // Handle [empty] marker
        if ("[empty]".equals(firstName)) firstName = "";
        if ("[empty]".equals(lastName)) lastName = "";
        if ("[empty]".equals(postalCode)) postalCode = "";

        lastErrorMessage = edgeCaseFlow.attemptCheckoutWithInvalidData(firstName, lastName, postalCode);
    }

    /**
     * Step: "Then I should see a validation error containing {string}"
     *
     * @param expectedText Text that should appear in error message
     */
    @Then("I should see a validation error containing {string}")
    public void i_should_see_a_validation_error_containing(String expectedText) {
        Assertions.assertThat(lastErrorMessage)
                .as("Should see validation error containing '%s'", expectedText)
                .isNotNull()
                .containsIgnoringCase(expectedText);
    }

    /**
     * Step: "Then the checkout should proceed successfully"
     *
     * Verifies that unusual but valid data was accepted.
     */
    @Then("the checkout should proceed successfully")
    public void the_checkout_should_proceed_successfully() {
        Assertions.assertThat(lastErrorMessage)
                .as("Checkout should proceed without errors")
                .isNull();
    }
}