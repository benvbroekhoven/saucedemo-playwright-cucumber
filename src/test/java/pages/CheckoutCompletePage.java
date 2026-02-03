package pages;

public class CheckoutCompletePage extends BasePage {

    private final String completeHeader = "[data-test='complete-header']";

    public boolean isLoaded() {
        return safeIsVisible(completeHeader);
    }

    public String getConfirmationText() {
        return safeGetText(completeHeader);
    }
}
