package pages;

/**
 * CartPage represents the shopping cart page.
 *
 * This page shows all items added to the cart and allows
 * users to proceed to checkout or continue shopping.
 *
 * Page URL: https://www.saucedemo.com/cart.html
 */
public class CartPage extends BasePage {

    // Locator for the checkout button
    private final String checkoutButton = "[data-test='checkout']";

    /**
     * Verifies that the cart page has loaded successfully.
     *
     * @return true if the checkout button is visible
     */
    public boolean isLoaded() {
        return safeIsVisible(checkoutButton);
    }

    /**
     * Proceeds to the checkout process.
     *
     * Clicks the checkout button and waits for the checkout
     * information form to load.
     */
    public void proceedToCheckout() {
        safeClick(checkoutButton);
        waitForNetworkIdle();  // Wait for navigation to checkout page
    }
}