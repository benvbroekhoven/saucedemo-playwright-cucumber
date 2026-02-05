package steps;

import flows.ProductFlow;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import pages.CartPage;

import java.util.List;

public class CartVerificationSteps  {

    private final ProductFlow productFlow = new ProductFlow();
    private final CartPage cartPage = new CartPage();


    @Then("the cart should contain the following items:")
    public void the_cart_should_contain_the_following_items(io.cucumber.datatable.DataTable dataTable) {
        List<String> expectedItems = dataTable.asList();
        List<String> actualItems = cartPage.getCartItemNames();

        Assertions.assertThat(actualItems)
                .as("Cart should contain exactly the expected items")
                .containsExactlyInAnyOrderElementsOf(expectedItems);
    }

    @Then("the cart should contain {int} item(s)")
    public void the_cart_should_contain_n_items(int expectedCount) {
        // Go to cart first to ensure we're on the right page
        productFlow.goToCart();

        int actualCount = cartPage.getItemCount();
        System.out.println("DEBUG: Expected " + expectedCount + ", found " + actualCount);
        System.out.println("DEBUG: Items in cart: " + cartPage.getCartItemNames());

        Assertions.assertThat(actualCount)
                .as("Cart item count should be %d but was %d", expectedCount, actualCount)
                .isEqualTo(expectedCount);
    }

    @Then("the product {string} should be in the cart")
    public void the_product_should_be_in_the_cart(String productName) {
        Assertions.assertThat(cartPage.isProductInCart(productName))
                .as("Product '%s' should be in the cart", productName)
                .isTrue();
    }

    @Then("the product {string} should not be in the cart")
    public void the_product_should_not_be_in_the_cart(String productName) {
        Assertions.assertThat(cartPage.isProductInCart(productName))
                .as("Product '%s' should not be in the cart", productName)
                .isFalse();
    }

    @When("I remove {string} from the cart")
    public void i_remove_from_the_cart(String productName) {
        cartPage.removeProduct(productName);
    }

    @Then("the cart total should be ${double}")
    public void the_cart_total_should_be(double expectedTotal) {
        double actualTotal = cartPage.calculateItemsTotal();
        Assertions.assertThat(actualTotal)
                .as("Cart total should be $%.2f but was $%.2f", expectedTotal, actualTotal)
                .isCloseTo(expectedTotal, Assertions.within(0.01));
    }

    @Then("the cart should be empty")
    public void the_cart_should_be_empty() {
        Assertions.assertThat(cartPage.getItemCount())
                .as("Cart should be empty")
                .isZero();

        Assertions.assertThat(cartPage.getCartBadgeCount())
                .as("Cart badge should not be visible when empty")
                .isZero();
    }

    @Then("the cart badge count should match the actual item count")
    public void the_cart_badge_count_should_match_the_actual_item_count() {
        Assertions.assertThat(cartPage.isCartCountConsistent())
                .as("Cart badge count should match actual number of items in cart")
                .isTrue();
    }

}