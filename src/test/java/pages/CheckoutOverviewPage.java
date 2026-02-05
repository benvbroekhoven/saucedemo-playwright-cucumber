package pages;

/**
 * CheckoutOverviewPage represents the order summary/review page.
 *
 * This page shows a summary of the order including all items,
 * prices, and totals. Users can review and complete their order here.
 *
 * Page URL: https://www.saucedemo.com/checkout-step-two.html
 */
public class CheckoutOverviewPage extends BasePage {

    // Locator for the finish button
    private final String finishButton = "[data-test='finish']";

    /**
     * Verifies that the checkout overview page has loaded successfully.
     *
     * @return true if the finish button is visible
     */
    public boolean isLoaded() {
        return safeIsVisible(finishButton);
    }

    /**
     * Completes the order by clicking the finish button.
     *
     * This is the final step in the checkout process.
     * After clicking, the user is taken to the order confirmation page.
     */
    public void finishOrder() {
        safeClick(finishButton);
        waitForNetworkIdle();  // Wait for navigation to confirmation page
    }
}