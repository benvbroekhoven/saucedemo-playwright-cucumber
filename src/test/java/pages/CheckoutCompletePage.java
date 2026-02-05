package pages;

/**
 * CheckoutCompletePage represents the order confirmation page.
 *
 * This is the final page users see after successfully placing an order.
 * It displays a confirmation message thanking the user for their order.
 *
 * Page URL: https://www.saucedemo.com/checkout-complete.html
 */
public class CheckoutCompletePage extends BasePage {

    // Locator for the confirmation header
    private final String completeHeader = "[data-test='complete-header']";

    /**
     * Verifies that the order confirmation page has loaded successfully.
     *
     * This is used to confirm that the entire checkout process
     * completed successfully.
     *
     * @return true if the confirmation header is visible
     */
    public boolean isLoaded() {
        return safeIsVisible(completeHeader);
    }

    /**
     * Gets the confirmation message text.
     *
     * Typically returns "Thank you for your order!" or similar message.
     *
     * @return The confirmation header text
     */
    public String getConfirmationText() {
        return safeGetText(completeHeader);
    }
}