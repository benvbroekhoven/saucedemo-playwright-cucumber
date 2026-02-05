package steps;

import flows.ProductFlow;
import org.assertj.core.api.Assertions;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.util.Arrays;

/**
 * ProductSteps maps Gherkin steps to product/cart functionality.
 *
 * This class handles all product-related Gherkin steps including:
 * - Adding products to cart
 * - Removing products from cart
 * - Verifying cart contents
 *
 * It delegates the actual work to ProductFlow, keeping this class
 * focused solely on the Gherkin-to-code mapping.
 */
public class ProductSteps {

    private final ProductFlow productFlow = new ProductFlow();

    /**
     * Step: "When I add the product {string} to the cart"
     *
     * Adds a single product to the shopping cart.
     *
     * Example usage in feature files:
     *   When I add the product "Sauce Labs Backpack" to the cart
     *
     * @param productName The exact name of the product to add
     */
    @When("I add the product {string} to the cart")
    public void i_add_the_product_to_the_cart(String productName) {
        productFlow.addProduct(productName);
    }

    /**
     * Step: "When I add the products {string} to the cart"
     *
     * Adds multiple products to the shopping cart.
     * Products should be comma-separated in the feature file.
     *
     * Example usage in feature files:
     *   When I add the products "Sauce Labs Backpack, Sauce Labs Bike Light" to the cart
     *
     * The string is split by commas and converted to a list for processing.
     *
     * @param products Comma-separated list of product names
     */
    @When("I add the products {string} to the cart")
    public void i_add_the_products_to_the_cart(String products) {
        // Split the comma-separated string into a list
        productFlow.addProducts(Arrays.asList(products.split(",")));
    }

    /**
     * Step: "Then the cart should contain {int} items"
     *
     * Verifies that the shopping cart contains the expected number of items.
     * The cart badge is checked to get the current count.
     *
     * Example usage in feature files:
     *   Then the cart should contain 2 items
     *   Then the cart should contain 0 items
     *
     * Uses AssertJ for clear assertion failures with descriptive messages.
     *
     * @param expectedCount The expected number of items in the cart
     */
    @Then("the cart should contain {int} items")
    public void the_cart_should_contain_items(int expectedCount) {
        Assertions.assertThat(productFlow.getCartCount())
                .as("Cart count should match expected value")
                .isEqualTo(expectedCount);
    }

    /**
     * Step: "When I remove the product {string} from the cart"
     *
     * Removes a single product from the shopping cart.
     * The product must have been added to the cart first.
     *
     * Example usage in feature files:
     *   When I remove the product "Sauce Labs Backpack" from the cart
     *
     * @param productName The exact name of the product to remove
     */
    @When("I remove the product {string} from the cart")
    public void i_remove_the_product_from_the_cart(String productName) {
        productFlow.removeProduct(productName);
    }

    /**
     * Step: "When I remove the products {string} from the cart"
     *
     * Removes multiple products from the shopping cart.
     * Products should be comma-separated in the feature file.
     *
     * Example usage in feature files:
     *   When I remove the products "Sauce Labs Backpack, Sauce Labs Bike Light" from the cart
     *
     * The string is split by commas, each product name is trimmed,
     * and converted to a list for processing.
     *
     * @param products Comma-separated list of product names to remove
     */
    @When("I remove the products {string} from the cart")
    public void i_remove_the_products_from_the_cart(String products) {
        productFlow.removeProducts(
                Arrays.stream(products.split(","))
                        .map(String::trim)  // Remove any extra whitespace
                        .toList()
        );
    }
}