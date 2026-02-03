package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import drivers.PlaywrightFactory;

public abstract class BasePage {

    protected Page page;

    protected BasePage() {
        this.page = PlaywrightFactory.getPage();
    }

    // ---------------------------------------------------------
    // WAIT UTILITIES (Playwright Java correct)
    // ---------------------------------------------------------

    protected void waitForVisible(String selector) {
        page.waitForSelector(selector,
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    protected void waitForHidden(String selector) {
        page.waitForSelector(selector,
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

    protected void waitForAttached(String selector) {
        page.waitForSelector(selector,
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED));
    }

    protected void waitForDetached(String selector) {
        page.waitForSelector(selector,
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.DETACHED));
    }

    protected void waitForPageLoad() {
        page.waitForLoadState(LoadState.LOAD);
    }

    protected void waitForNetworkIdle() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    protected void waitForUrlContains(String partialUrl) {
        page.waitForURL("**" + partialUrl + "**");
    }

    // ---------------------------------------------------------
    // SAFE ACTIONS (met retry-mechanisme)
    // ---------------------------------------------------------

    protected void safeClick(String selector) {
        waitForVisible(selector);
        waitForAttached(selector);

        Locator loc = page.locator(selector);

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                loc.click();
                return;
            } catch (Exception e) {
                if (attempt == 3) throw e; // laatste poging → error
                page.waitForTimeout(200); // kleine backoff
            }
        }
    }

    protected void safeType(String selector, String text) {
        waitForVisible(selector);
        waitForAttached(selector);

        Locator loc = page.locator(selector);
        loc.fill(""); // clear
        loc.fill(text);
    }

    protected String safeGetText(String selector) {
        waitForVisible(selector);
        return page.locator(selector).innerText();
    }

    protected boolean safeIsVisible(String selector) {
        try {
            return page.locator(selector).isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    // ---------------------------------------------------------
    // NAVIGATION
    // ---------------------------------------------------------

    protected void navigateTo(String url) {
        page.navigate(url);
        waitForPageLoad();
    }
}
