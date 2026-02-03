package flows;

import pages.*;

public class CheckoutFlow {

    private final InventoryPage inventoryPage = new InventoryPage();
    private final CartPage cartPage = new CartPage();
    private final CheckoutPage checkoutPage = new CheckoutPage();
    private final CheckoutOverviewPage overviewPage = new CheckoutOverviewPage();
    private final CheckoutCompletePage completePage = new CheckoutCompletePage();

    public void buyItem(String itemName) {
        inventoryPage.addItemToCart(itemName);
        inventoryPage.goToCart();

        cartPage.isLoaded();
        cartPage.proceedToCheckout();

        checkoutPage.isLoaded();
        checkoutPage.fillInformation("Ben", "Automation", "3000");
        checkoutPage.continueToOverview();

        overviewPage.isLoaded();
        overviewPage.finishOrder();

        completePage.isLoaded();
    }

    public CheckoutCompletePage completePage() {
        return completePage;
    }
}
