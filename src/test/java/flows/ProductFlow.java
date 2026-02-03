package flows;

import pages.InventoryPage;
import pages.CartPage;

import java.util.List;

public class ProductFlow {

    private final InventoryPage inventoryPage = new InventoryPage();
    private final CartPage cartPage = new CartPage();

    // Voeg één product toe
    public void addProduct(String productName) {
        inventoryPage.isLoaded();
        inventoryPage.addItemToCart(productName);
    }

    // Voeg meerdere producten toe
    public void addProducts(List<String> productNames) {
        inventoryPage.isLoaded();
        for (String product : productNames) {
            inventoryPage.addItemToCart(product.trim());
        }
    }

    // Verwijder één product
    public void removeProduct(String productName) {
        inventoryPage.isLoaded();
        inventoryPage.removeItemFromCart(productName.trim());
    }

    public void removeProducts(List<String> productNames) {
        inventoryPage.isLoaded();
        for (String product : productNames) {
            inventoryPage.removeItemFromCart(product.trim());
        }
    }

    public void clearCart(List<String> allProducts) {
        inventoryPage.isLoaded();
        for (String product : allProducts) {
            inventoryPage.removeItemFromCart(product.trim());
        }
    }


    // Ga naar de cart
    public void goToCart() {
        inventoryPage.goToCart();
        cartPage.isLoaded();
    }

    // Controleer cart count
    public int getCartCount() {
        return inventoryPage.getCartCount();
    }
}
