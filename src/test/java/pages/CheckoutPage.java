package pages;

/**
 * CheckoutPage represents the checkout information form.
 *
 * Page URL: https://www.saucedemo.com/checkout-step-one.html
 */
public class CheckoutPage extends BasePage {

    private final String firstNameInput = "[data-test='firstName']";
    private final String lastNameInput = "[data-test='lastName']";
    private final String postalCodeInput = "[data-test='postalCode']";
    private final String continueButton = "[data-test='continue']";
    private final String cancelButton = "[data-test='cancel']";
    private final String errorMessage = "[data-test='error']";

    public boolean isLoaded() {
        return safeIsVisible(firstNameInput) &&
                safeIsVisible(lastNameInput) &&
                safeIsVisible(postalCodeInput) &&
                safeIsVisible(continueButton);
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
     * Gets the current value of the first name field.
     *
     * @return Current text in first name field
     */
    public String getFirstNameValue() {
        return page.locator(firstNameInput).inputValue();
    }

    /**
     * Gets the current value of the last name field.
     *
     * @return Current text in last name field
     */
    public String getLastNameValue() {
        return page.locator(lastNameInput).inputValue();
    }

    /**
     * Gets the current value of the postal code field.
     *
     * @return Current text in postal code field
     */
    public String getPostalCodeValue() {
        return page.locator(postalCodeInput).inputValue();
    }

    /**
     * Checks if the form has been filled (basic validation).
     *
     * @return true if all fields have values
     */
    public boolean isFormFilled() {
        return !getFirstNameValue().isEmpty() &&
                !getLastNameValue().isEmpty() &&
                !getPostalCodeValue().isEmpty();
    }

    public void continueToOverview() {
        safeClick(continueButton);
        waitForNetworkIdle();
    }

    public void cancelCheckout() {
        safeClick(cancelButton);
        waitForNetworkIdle();
    }

    /**
     * Attempts to continue with empty fields to trigger validation.
     * Useful for testing error handling.
     */
    public void attemptContinueWithEmptyFields() {
        // Clear all fields first
        page.locator(firstNameInput).fill("");
        page.locator(lastNameInput).fill("");
        page.locator(postalCodeInput).fill("");
        safeClick(continueButton);
    }

    public boolean isErrorVisible() {
        return safeIsVisible(errorMessage);
    }

    public String getErrorText() {
        return safeGetText(errorMessage);
    }

    /**
     * Checks for specific error message.
     *
     * @param expectedError Partial error text to check for
     * @return true if error contains expected text
     */
    public boolean hasError(String expectedError) {
        return isErrorVisible() && getErrorText().contains(expectedError);
    }

    /**
     * Attempts to proceed to checkout with validation.
     * Returns false if checkout cannot proceed (e.g., empty cart redirects back).
     *
     * @return true if successfully reached checkout page
     */
    public boolean attemptCheckout() {
        String currentUrl = page.url();

        // Try to click continue
        safeClick(continueButton);
        waitForNetworkIdle();

        // Check if we're still on checkout page (success) or redirected (failure)
        return page.url().contains("checkout-step");
    }

    /**
     * Checks if an error message about empty cart is displayed.
     *
     * @return true if empty cart error is shown
     */
    public boolean hasEmptyCartError() {
        return isErrorVisible() && getErrorText().toLowerCase().contains("cart");
    }
}