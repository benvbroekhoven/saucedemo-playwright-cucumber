package flows;

import pages.CartPage;
import pages.InventoryPage;
import java.util.List;

public class ProductFlow {

    private final InventoryPage inventoryPage = new InventoryPage();
    private final CartPage cartPage = new CartPage();

    public void addProduct(String productName) {
        // Ensure we're on inventory page first
        inventoryPage.navigateToInventory();

        // Verify product exists
        if (!inventoryPage.isProductDisplayed(productName)) {
            throw new IllegalArgumentException("Product not found: '" + productName + "'");
        }

        inventoryPage.addItemToCart(productName);
    }

    public void addProducts(List<String> productNames) {
        inventoryPage.isLoaded();
        for (String product : productNames) {
            addProduct(product.trim());
        }
    }

    public void removeProduct(String productName) {
        inventoryPage.navigateToInventory();

        if (!inventoryPage.isRemoveButtonVisible(productName)) {
            throw new IllegalArgumentException("Product not in cart: '" + productName + "'");
        }

        inventoryPage.removeItemFromCart(productName);
    }

    public void removeProducts(List<String> productNames) {
        inventoryPage.isLoaded();
        for (String product : productNames) {
            removeProduct(product.trim());
        }
    }

    public void clearCart(List<String> allProducts) {
        inventoryPage.isLoaded();
        for (String product : allProducts) {
            // Only remove if it's showing the Remove button
            if (inventoryPage.isRemoveButtonVisible(product)) {
                removeProduct(product);
            }
        }
    }

    public void goToCart() {
        inventoryPage.goToCart();
        cartPage.isLoaded();

        // Verify cart consistency
        if (!cartPage.isCartCountConsistent()) {
            throw new AssertionError("Cart badge count doesn't match actual items");
        }
    }

    public int getCartCount() {
        return inventoryPage.getCartCount();
    }

    /**
     * Verifies that specific products are in the cart.
     *
     * @param expectedProducts List of product names expected in cart
     * @return true if all products are present
     */
    public boolean verifyProductsInCart(List<String> expectedProducts) {
        goToCart();
        List<String> actualProducts = cartPage.getCartItemNames();
        return actualProducts.containsAll(expectedProducts);
    }
}