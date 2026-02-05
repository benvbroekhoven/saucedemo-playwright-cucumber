package pages;

import config.ConfigManager;

import java.util.ArrayList;
import java.util.List;

/**
 * CartPage represents the shopping cart page with comprehensive verification.
 *
 * This page shows all items added to the cart and allows
 * users to proceed to checkout or continue shopping.
 *
 * Page URL: https://www.saucedemo.com/cart.html
 */
public class CartPage extends BasePage {

    // Locators for cart page elements
    private final String checkoutButton = "[data-test='checkout']";
    private final String continueShoppingButton = "[data-test='continue-shopping']";
    private final String cartContainer = ".cart_list";
    private final String cartItems = ".cart_item";
    private final String cartItemNames = ".cart_item .inventory_item_name";
    private final String cartItemPrices = ".cart_item .inventory_item_price";
    private final String removeButtons = "[data-test^='remove-']";
    private final String cartBadge = ".shopping_cart_badge";

    // Individual item locators (dynamic)
    private final String itemByName = "//div[@class='cart_item'][.//div[text()='%s']]";

    /**
     * Verifies that the cart page has loaded successfully.
     * Checks multiple critical elements to ensure page is fully loaded.
     *
     * @return true if all critical elements are visible
     */
    public boolean isLoaded() {
        return safeIsVisible(checkoutButton) &&
                safeIsVisible(continueShoppingButton) &&
                safeIsVisible(cartContainer);
    }

    /**
     * Gets the number of items currently in the cart.
     *
     * @return Count of cart items (0 if empty)
     */
    public int getItemCount() {
        return getElementCount(cartItems);
    }

    /**
     * Gets all product names currently in the cart.
     *
     * @return List of product names (empty if cart is empty)
     */
    public List<String> getCartItemNames() {
        return getAllTexts(cartItemNames);
    }

    /**
     * Gets all product prices currently in the cart.
     *
     * @return List of price strings (e.g., "$29.99")
     */
    public List<String> getCartItemPrices() {
        return getAllTexts(cartItemPrices);
    }

    /**
     * Checks if a specific product is in the cart.
     *
     * @param productName Exact name of the product
     * @return true if product is in cart
     */
    public boolean isProductInCart(String productName) {
        return getCartItemNames().contains(productName);
    }

    /**
     * Gets the quantity of a specific product in cart.
     * Usually 1, but useful if your cart supports multiple quantities.
     *
     * @param productName Exact name of the product
     * @return Quantity (0 if not found)
     */
    public int getProductQuantity(String productName) {
        String itemLocator = String.format(itemByName + "//div[@class='cart_quantity']", productName);
        try {
            String qtyText = page.locator("xpath=" + itemLocator).textContent();
            return Integer.parseInt(qtyText.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Removes a specific product from the cart.
     *
     * @param productName Exact name of the product to remove
     */
    public void removeProduct(String productName) {
        String removeButton = String.format(
                "//div[@class='cart_item'][.//div[text()='%s']]//button[contains(@data-test,'remove')]",
                productName
        );
        safeClick("xpath=" + removeButton);
        // Wait for item to be removed from DOM
        waitForDetached("xpath=" + String.format(itemByName, productName));
    }

    /**
     * Gets the displayed cart badge count.
     *
     * @return Number shown on cart icon (0 if badge not visible)
     */
    public int getCartBadgeCount() {
        if (!safeIsVisible(cartBadge)) {
            return 0;
        }
        String badgeText = safeGetText(cartBadge);
        try {
            return Integer.parseInt(badgeText);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Verifies that cart badge count matches actual item count.
     * Useful for catching UI synchronization bugs.
     *
     * @return true if badge count equals actual cart items
     */
    public boolean isCartCountConsistent() {
        return getCartBadgeCount() == getItemCount();
    }

    /**
     * Proceeds to the checkout process.
     */
    public void proceedToCheckout() {
        safeClick(checkoutButton);
        waitForNetworkIdle();
    }

    /**
     * Continues shopping (returns to inventory).
     */
    public void continueShopping() {
        safeClick(continueShoppingButton);
        waitForNetworkIdle();
    }

    /**
     * Gets detailed information about all items in cart.
     * Useful for comprehensive assertions.
     *
     * @return List of CartItem objects containing name, price, quantity
     */
    public List<CartItem> getCartItems() {
        List<String> names = getCartItemNames();
        List<String> prices = getCartItemPrices();
        List<CartItem> items = new ArrayList<>();

        for (int i = 0; i < names.size(); i++) {
            String price = i < prices.size() ? prices.get(i) : "Unknown";
            int qty = getProductQuantity(names.get(i));
            items.add(new CartItem(names.get(i), price, qty));
        }
        return items;
    }

    /**
     * Calculates total price of all items in cart.
     * Note: This is item total, not including tax.
     *
     * @return Total as double (0.0 if cart empty or prices can't be parsed)
     */
    public double calculateItemsTotal() {
        double total = 0.0;
        for (String priceStr : getCartItemPrices()) {
            try {
                // Remove $ sign and parse
                String numeric = priceStr.replace("$", "").trim();
                total += Double.parseDouble(numeric);
            } catch (NumberFormatException e) {
                // Log warning but continue with other items
                System.err.println("Could not parse price: " + priceStr);
            }
        }
        return total;
    }

    // Inner class to represent cart items
    public static class CartItem {
        private final String name;
        private final String price;
        private final int quantity;

        public CartItem(String name, String price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public String getName() { return name; }
        public String getPrice() { return price; }
        public int getQuantity() { return quantity; }

        @Override
        public String toString() {
            return String.format("%s (Qty: %d) - %s", name, quantity, price);
        }
    }
    /**
     * Checks if the cart is completely empty.
     *
     * @return true if no items in cart and badge is not visible
     */
    public boolean isEmpty() {
        return getItemCount() == 0 && getCartBadgeCount() == 0;
    }

    /**
     * Gets the empty cart message if displayed.
     *
     * @return Empty cart message text, or null if not empty
     */
    public String getEmptyCartMessage() {
        String emptyMessageSelector = "[data-test='empty-cart']";
        if (safeIsVisible(emptyMessageSelector)) {
            return safeGetText(emptyMessageSelector);
        }
        return null;
    }
    public void navigateToCart() {
        if (!page.url().contains("cart.html")) {
            navigateTo(ConfigManager.get("baseUrl") + "cart.html");
            waitForPageLoad();
            isLoaded();
        }
    }
}