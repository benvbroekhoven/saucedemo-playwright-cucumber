package flows;

import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;

/**
 * EdgeCaseFlow handles unusual scenarios and error conditions.
 *
 * This flow class tests boundary conditions, error states,
 * and unusual user behaviors that might break the application.
 */
public class EdgeCaseFlow {

    private final InventoryPage inventoryPage = new InventoryPage();
    private final CartPage cartPage = new CartPage();
    private final CheckoutPage checkoutPage = new CheckoutPage();

    /**
     * Attempts to checkout with an empty cart.
     *
     * @return true if system prevented checkout (expected), false if allowed (bug)
     */
    public boolean attemptEmptyCartCheckout() {
        // Ensure cart is empty
        inventoryPage.navigateToInventory();
        inventoryPage.goToCart();

        if (!cartPage.isEmpty()) {
            throw new IllegalStateException("Cart is not empty - cannot test empty cart checkout");
        }

        // Try to proceed to checkout
        cartPage.proceedToCheckout();

        // Check if we actually reached checkout or were blocked
        return checkoutPage.isLoaded();
    }

    /**
     * Adds the same product multiple times.
     * SauceDemo typically ignores duplicates (button just stays as "Remove").
     *
     * @param productName Product to add multiple times
     * @param times       Number of times to attempt adding
     * @return Actual count in cart after all attempts (usually 1)
     */
    public int addSameProductMultipleTimes(String productName, int times) {
        inventoryPage.navigateToInventory();

        int successfulAdds = 0;
        for (int i = 0; i < times; i++) {
            if (inventoryPage.addItemToCartIfNotPresent(productName)) {
                successfulAdds++;
            }
        }

        return cartPage.getItemCount();
    }

    /**
     * Attempts rapid add/remove operations to test race conditions.
     *
     * @param productName Product to test with
     * @param cycles      Number of add/remove cycles
     * @return Final state - true if in cart, false if removed
     */
    public boolean rapidAddRemoveCycles(String productName, int cycles) {
        inventoryPage.navigateToInventory();

        boolean finalInCart = false;
        for (int i = 0; i < cycles; i++) {
            // Add
            inventoryPage.addItemToCart(productName);
            finalInCart = true;

            // Immediately remove
            inventoryPage.removeItemFromCart(productName);
            finalInCart = false;
        }

        return finalInCart;
    }

    /**
     * Fills checkout form with edge case data.
     *
     * @param firstName  First name (can be empty, special chars, etc.)
     * @param lastName   Last name
     * @param postalCode Postal code (can be invalid format)
     * @return Error message if validation failed, null if accepted
     */
    public String attemptCheckoutWithInvalidData(String firstName, String lastName, String postalCode) {
        inventoryPage.navigateToInventory();
        inventoryPage.addItemToCart("Sauce Labs Backpack");
        inventoryPage.goToCart();
        cartPage.proceedToCheckout();

        checkoutPage.fillInformation(firstName, lastName, postalCode);
        checkoutPage.continueToOverview();

        if (checkoutPage.isErrorVisible()) {
            return checkoutPage.getErrorText();
        }
        return null;
    }

    // Getter methods for steps that need direct page access
    public InventoryPage getInventoryPage() {
        return inventoryPage;
    }

    public CartPage getCartPage() {
        return cartPage;
    }
}