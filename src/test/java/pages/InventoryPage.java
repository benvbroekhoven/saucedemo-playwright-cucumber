package pages;

public class InventoryPage extends BasePage {

    private final String inventoryContainer = "[data-test='inventory-container']";
    private final String cartIcon = ".shopping_cart_link";

    public boolean isLoaded() {
        waitForVisible(inventoryContainer);
        return safeIsVisible(inventoryContainer);
    }

    public void addItemToCart(String itemName) {
        String itemSelector = String.format(
                "//div[@class='inventory_item'][.//div[text()='%s']]//button[contains(@data-test,'add-to-cart')]",
                itemName
        );
        safeClick("xpath=" + itemSelector);

    }

    public void goToCart() {
        safeClick(cartIcon);
        waitForNetworkIdle();
    }
    public void removeItemFromCart(String itemName) {
        String removeSelector = String.format(
                "//div[@class='inventory_item'][.//div[text()='%s']]//button[contains(@data-test,'remove')]",
                itemName
        );
        safeClick("xpath=" + removeSelector);
    }


    public int getCartCount() {
        String badge = ".shopping_cart_badge";
        if (!safeIsVisible(badge)) {
            return 0;
        }
        return Integer.parseInt(safeGetText(badge));
    }

}
