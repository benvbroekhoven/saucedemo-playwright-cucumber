package pages;

public class CartPage extends BasePage {

    private final String checkoutButton = "[data-test='checkout']";

    public boolean isLoaded() {
        return safeIsVisible(checkoutButton);
    }

    public void proceedToCheckout() {
        safeClick(checkoutButton);
        waitForNetworkIdle();
    }
}
