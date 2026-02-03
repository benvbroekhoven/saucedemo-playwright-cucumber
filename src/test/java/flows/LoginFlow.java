package flows;

import pages.InventoryPage;
import pages.LoginPage;

public class LoginFlow {

    private final LoginPage loginPage = new LoginPage();
    private final InventoryPage inventoryPage = new InventoryPage();

    // Valid login
    public void loginExpectingSuccess(String username, String password) {
        loginPage.open();
        loginPage.loginAs(username, password);
        inventoryPage.isLoaded(); // wacht tot inventory page geladen is
    }

    // Invalid login
    public void loginExpectingFailure(String username, String password) {
        loginPage.open();
        loginPage.loginAs(username, password);
        loginPage.waitForError(); // wacht op foutmelding
    }

    // Getter zodat steps foutmeldingen kunnen checken
    public LoginPage loginPage() {
        return loginPage;
    }
}
