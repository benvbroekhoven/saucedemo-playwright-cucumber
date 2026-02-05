package pages;

/**
 * InventoryPage represents the product inventory/catalog page.
 *
 * This is the main page users see after logging in, showing all
 * available products. Users can add/remove items from cart here.
 *
 * Page URL: https://www.saucedemo.com/inventory.html
 */
public class InventoryPage extends BasePage {

    // Locators for inventory page elements
    private final String inventoryContainer = "[data-test='inventory-container']";
    private final String cartIcon = ".shopping_cart_link";

    /**
     * Verifies that the inventory page has loaded successfully.
     *
     * This is typically called after login to confirm navigation
     * to the products page was successful.
     *
     * @return true if the inventory container is visible
     */
    public boolean isLoaded() {
        waitForVisible(inventoryContainer);
        return safeIsVisible(inventoryContainer);
    }

    /**
     * Adds a product to the shopping cart by product name.
     *
     * This method uses XPath to find the product by its exact name,
     * then clicks the "Add to cart" button for that specific product.
     *
     * XPath breakdown:
     * - //div[@class='inventory_item']: Find all product containers
     * - [.//div[text()='%s']]: Filter to the one containing the product name
     * - //button[contains(@data-test,'add-to-cart')]: Find the add button in that product
     *
     * @param itemName The exact name of the product (e.g., "Sauce Labs Backpack")
     */
    public void addItemToCart(String itemName) {
        String itemSelector = String.format(
                "//div[@class='inventory_item'][.//div[text()='%s']]//button[contains(@data-test,'add-to-cart')]",
                itemName
        );
        safeClick("xpath=" + itemSelector);
    }

    /**
     * Clicks the cart icon to navigate to the shopping cart page.
     *
     * Waits for network idle to ensure the cart page loads completely.
     */
    public void goToCart() {
        safeClick(cartIcon);
        waitForNetworkIdle();
    }

    /**
     * Removes a product from the shopping cart by product name.
     *
     * This method only works if the item was already added to the cart.
     * The "Add to cart" button changes to "Remove" after adding an item.
     *
     * @param itemName The exact name of the product to remove
     */
    public void removeItemFromCart(String itemName) {
        String removeSelector = String.format(
                "//div[@class='inventory_item'][.//div[text()='%s']]//button[contains(@data-test,'remove')]",
                itemName
        );
        safeClick("xpath=" + removeSelector);
    }

    /**
     * Gets the current number of items in the shopping cart.
     *
     * The cart badge shows the count of items added to cart.
     * If no items are in the cart, the badge is not visible.
     *
     * @return The number of items in cart (0 if cart is empty)
     */
    public int getCartCount() {
        String badge = ".shopping_cart_badge";

        // If badge is not visible, cart is empty
        if (!safeIsVisible(badge)) {
            return 0;
        }

        // Parse the badge text to get the count
        return Integer.parseInt(safeGetText(badge));
    }
}