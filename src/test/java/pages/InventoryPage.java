package pages;

import com.microsoft.playwright.Page;
import config.ConfigManager;

import java.util.List;

/**
 * InventoryPage represents the product inventory/catalog page.
 *
 * This is the main page users see after logging in.
 *
 * Page URL: https://www.saucedemo.com/inventory.html
 */
public class InventoryPage extends BasePage {

    // Locators
    private final String inventoryContainer = "[data-test='inventory-container']";
    private final String inventoryItems = ".inventory_item";
    private final String inventoryItemNames = ".inventory_item_name";
    private final String inventoryItemPrices = ".inventory_item_price";
    private final String addToCartButtons = "[data-test^='add-to-cart-']";
    private final String removeButtons = "[data-test^='remove-']";
    private final String cartIcon = ".shopping_cart_link";
    private final String cartBadge = ".shopping_cart_badge";
    private final String sortDropdown = "[data-test='product-sort-container']";

    // Dynamic locators
    private final String itemByName = "//div[@class='inventory_item'][.//div[text()='%s']]";

    public boolean isLoaded() {
        boolean containerVisible = safeIsVisible(inventoryContainer);

        // Wait for at least one product to be visible
        try {
            page.waitForSelector(inventoryItemNames,
                    new Page.WaitForSelectorOptions().setTimeout(5000));
        } catch (Exception e) {
            System.out.println("WARNING: No products found after waiting");
        }

        return containerVisible && getElementCount(inventoryItems) > 0;
    }

    /**
     * Gets the total number of products displayed.
     *
     * @return Count of product items
     */
    public int getProductCount() {
        return getElementCount(inventoryItems);
    }

    /**
     * Gets all product names visible on the page.
     *
     * @return List of product names
     */
    public List<String> getAllProductNames() {
        return getAllTexts(inventoryItemNames);
    }

    /**
     * Gets all product prices visible on the page.
     *
     * @return List of price strings
     */
    public List<String> getAllProductPrices() {
        return getAllTexts(inventoryItemPrices);
    }

    /**
     * Checks if a specific product is displayed on the page.
     *
     * @param productName Exact product name
     * @return true if product is visible
     */
    public boolean isProductDisplayed(String productName) {
        return getAllProductNames().contains(productName);
    }

    /**
     * Gets the price of a specific product.
     *
     * @param productName Exact product name
     * @return Price string (e.g., "$29.99") or null if not found
     */
    public String getProductPrice(String productName) {
        String priceLocator = String.format(
                "//div[@class='inventory_item'][.//div[text()='%s']]//div[@class='inventory_item_price']",
                productName
        );
        try {
            return page.locator("xpath=" + priceLocator).textContent();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Checks if a product has "Add to Cart" button (not yet in cart).
     *
     * @param productName Exact product name
     * @return true if "Add to Cart" button is visible
     */
    public boolean isAddToCartVisible(String productName) {
        String buttonLocator = String.format(
                itemByName + "//button[contains(@data-test,'add-to-cart')]",
                productName
        );
        return safeIsVisible("xpath=" + buttonLocator);
    }

    /**
     * Checks if a product has "Remove" button (already in cart).
     *
     * @param productName Exact product name
     * @return true if "Remove" button is visible
     */
    public boolean isRemoveButtonVisible(String productName) {
        String buttonLocator = String.format(
                itemByName + "//button[contains(@data-test,'remove')]",
                productName
        );
        return safeIsVisible("xpath=" + buttonLocator);
    }

    public void addItemToCart(String productName) {
        String buttonLocator = String.format(
                "//div[@class='inventory_item'][.//div[text()='%s']]//button[contains(@data-test,'add-to-cart')]",
                productName
        );

        // Click the add button
        safeClick("xpath=" + buttonLocator);

        // Simple wait for network idle to ensure API call completes
        waitForNetworkIdle();

        // Verify button changed to "Remove" (proves item was added)
        String removeButton = String.format(
                "//div[@class='inventory_item'][.//div[text()='%s']]//button[contains(@data-test,'remove')]",
                productName
        );

        // Short explicit wait for button state change
        try {
            page.waitForSelector("xpath=" + removeButton,
                    new Page.WaitForSelectorOptions().setTimeout(5000));
        } catch (Exception e) {
            throw new RuntimeException("Failed to add '" + productName + "' - button didn't change to Remove");
        }
    }

    public void removeItemFromCart(String productName) {
        String buttonLocator = String.format(
                itemByName + "//button[contains(@data-test,'remove')]",
                productName
        );
        safeClick("xpath=" + buttonLocator);
        // Wait for button to change back to "Add to Cart"
        waitForVisible(String.format(
                itemByName + "//button[contains(@data-test,'add-to-cart')]",
                productName
        ));
    }

    public int getCartCount() {
        if (!safeIsVisible(cartBadge)) {
            return 0;
        }
        try {
            return Integer.parseInt(safeGetText(cartBadge));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void goToCart() {
        safeClick(cartIcon);
        waitForNetworkIdle();
    }

    /**
     * Sorts products by the given option.
     *
     * @param sortOption Text to select (e.g., "Price (low to high)")
     */
    public void sortBy(String sortOption) {
        page.locator(sortDropdown).selectOption(sortOption);
        waitForNetworkIdle(); // Wait for products to reorder
    }

    /**
     * Verifies that products are sorted by price in ascending order.
     *
     * @return true if sorted low to high
     */
    public boolean isSortedByPriceAscending() {
        List<String> prices = getAllProductPrices();
        double prevPrice = -1;
        for (String priceStr : prices) {
            double price = parsePrice(priceStr);
            if (price < prevPrice) return false;
            prevPrice = price;
        }
        return true;
    }

    /**
     * Verifies that products are sorted by price in descending order.
     *
     * @return true if sorted high to low
     */
    public boolean isSortedByPriceDescending() {
        List<String> prices = getAllProductPrices();
        double prevPrice = Double.MAX_VALUE;
        for (String priceStr : prices) {
            double price = parsePrice(priceStr);
            if (price > prevPrice) return false;
            prevPrice = price;
        }
        return true;
    }

    private double parsePrice(String priceStr) {
        try {
            return Double.parseDouble(priceStr.replace("$", "").trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    /**
     * Navigates to the inventory page if not already there.
     * Ensures we can add products from any page state.
     */
    public void navigateToInventory() {
        String currentUrl = page.url();
        if (!currentUrl.contains("inventory.html")) {
            System.out.println("DEBUG: Navigating to inventory from: " + currentUrl);
            navigateTo(ConfigManager.get("baseUrl") + "inventory.html");
            waitForPageLoad();
            isLoaded();
        }
    }
    /**
     * Adds a product to cart if not already added.
     * Returns false if product was already in cart.
     *
     * @param productName Product to add
     * @return true if product was newly added, false if already in cart
     */
    public boolean addItemToCartIfNotPresent(String productName) {
        if (isRemoveButtonVisible(productName)) {
            System.out.println("Product '" + productName + "' already in cart");
            return false;
        }
        addItemToCart(productName);
        return true;
    }

    /**
     * Gets how many times a product appears in the cart (usually 1).
     *
     * @param productName Product name
     * @return Count of this product in cart (0 if not in cart)
     */
    public int getProductCountInCart(String productName) {
        // This checks the button state - if "Remove" is visible, it's in cart once
        return isRemoveButtonVisible(productName) ? 1 : 0;
    }

}