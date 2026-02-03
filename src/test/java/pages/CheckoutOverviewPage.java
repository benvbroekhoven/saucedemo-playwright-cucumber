package pages;

public class CheckoutOverviewPage extends BasePage {

    private final String finishButton = "[data-test='finish']";

    public boolean isLoaded() {
        return safeIsVisible(finishButton);
    }

    public void finishOrder() {
        safeClick(finishButton);
        waitForNetworkIdle();
    }
}
