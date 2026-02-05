package pages;

/**
 * CheckoutPage represents the checkout information form.
 *
 * This is the first step of checkout where users enter their
 * shipping information (first name, last name, postal code).
 *
 * Page URL: https://www.saucedemo.com/checkout-step-one.html
 */
public class CheckoutPage extends BasePage {

    // Locators for checkout form fields
    private final String firstNameInput = "[data-test='firstName']";
    private final String lastNameInput = "[data-test='lastName']";
    private final String postalCodeInput = "[data-test='postalCode']";
    private final String continueButton = "[data-test='continue']";

    /**
     * Verifies that the checkout page has loaded successfully.
     *
     * @return true if the first name input field is visible
     */
    public boolean isLoaded() {
        return safeIsVisible(firstNameInput);
    }

    /**
     * Fills in the checkout information form.
     *
     * @param first First name
     * @param last Last name
     * @param postal Postal/ZIP code
     */
    public void fillInformation(String first, String last, String postal) {
        safeType(firstNameInput, first);
        safeType(lastNameInput, last);
        safeType(postalCodeInput, postal);
    }

    /**
     * Proceeds to the checkout overview page.
     *
     * Clicks continue button after filling in information,
     * then navigates to the order summary page.
     */
    public void continueToOverview() {
        safeClick(continueButton);
        waitForNetworkIdle();  // Wait for navigation to overview page
    }
}