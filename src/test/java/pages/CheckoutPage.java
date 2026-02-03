package pages;

public class CheckoutPage extends BasePage {

    private final String firstNameInput = "[data-test='firstName']";
    private final String lastNameInput = "[data-test='lastName']";
    private final String postalCodeInput = "[data-test='postalCode']";
    private final String continueButton = "[data-test='continue']";

    public boolean isLoaded() {
        return safeIsVisible(firstNameInput);
    }

    public void fillInformation(String first, String last, String postal) {
        safeType(firstNameInput, first);
        safeType(lastNameInput, last);
        safeType(postalCodeInput, postal);
    }

    public void continueToOverview() {
        safeClick(continueButton);
        waitForNetworkIdle();
    }
}
