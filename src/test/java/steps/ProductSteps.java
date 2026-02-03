package steps;

import flows.ProductFlow;
import org.assertj.core.api.Assertions;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.util.Arrays;

public class ProductSteps {

    private final ProductFlow productFlow = new ProductFlow();

    @When("I add the product {string} to the cart")
    public void i_add_the_product_to_the_cart(String productName) {
        productFlow.addProduct(productName);
    }

    @When("I add the products {string} to the cart")
    public void i_add_the_products_to_the_cart(String products) {
        productFlow.addProducts(Arrays.asList(products.split(",")));
    }

    @Then("the cart should contain {int} items")
    public void the_cart_should_contain_items(int expectedCount) {
        Assertions.assertThat(productFlow.getCartCount())
                .as("Cart count should match expected value")
                .isEqualTo(expectedCount);
    }
    @When("I remove the product {string} from the cart")
    public void i_remove_the_product_from_the_cart(String productName) {
        productFlow.removeProduct(productName);
    }

    @When("I remove the products {string} from the cart")
    public void i_remove_the_products_from_the_cart(String products) {
        productFlow.removeProducts(
                Arrays.stream(products.split(","))
                        .map(String::trim)
                        .toList()
        );
    }

}
