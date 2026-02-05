package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import drivers.PlaywrightFactory;

/**
 * BasePage provides common functionality for all page objects.
 *
 * This abstract class implements the Page Object Model (POM) pattern,
 * which separates page structure from test logic. All page classes
 * should extend this base class to inherit common utilities.
 *
 * Key features:
 * - Centralized wait strategies
 * - Safe action methods with retry logic
 * - Consistent error handling
 * - Network and page load synchronization
 */
public abstract class BasePage {

    // The Playwright Page instance used by all page objects
    // Protected so subclasses can access it directly if needed
    protected Page page;

    /**
     * Constructor initializes the page from PlaywrightFactory.
     * This ensures all page objects use the same browser instance.
     */
    protected BasePage() {
        this.page = PlaywrightFactory.getPage();
    }

    // ---------------------------------------------------------
    // WAIT UTILITIES - Explicit waits for various conditions
    // ---------------------------------------------------------

    /**
     * Waits for an element to be visible on the page.
     * Visible means: element exists in DOM AND is displayed (not hidden).
     *
     * @param selector CSS or XPath selector for the element
     */
    protected void waitForVisible(String selector) {
        page.waitForSelector(selector,
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    /**
     * Waits for an element to be hidden (not visible).
     * Useful for waiting for loading spinners to disappear.
     *
     * @param selector CSS or XPath selector for the element
     */
    protected void waitForHidden(String selector) {
        page.waitForSelector(selector,
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

    /**
     * Waits for an element to be attached to the DOM.
     * Attached means: element exists in DOM (but may not be visible).
     *
     * @param selector CSS or XPath selector for the element
     */
    protected void waitForAttached(String selector) {
        page.waitForSelector(selector,
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED));
    }

    /**
     * Waits for an element to be detached (removed) from the DOM.
     * Useful for waiting for modals or popups to close completely.
     *
     * @param selector CSS or XPath selector for the element
     */
    protected void waitForDetached(String selector) {
        page.waitForSelector(selector,
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.DETACHED));
    }

    /**
     * Waits for the page's 'load' event.
     * This means the HTML is fully loaded, but resources may still be loading.
     */
    protected void waitForPageLoad() {
        page.waitForLoadState(LoadState.LOAD);
    }

    /**
     * Waits for network to be idle (no active network requests).
     * This is more reliable than waitForPageLoad() for dynamic pages
     * that load data asynchronously (AJAX, API calls, etc.).
     */
    protected void waitForNetworkIdle() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    /**
     * Waits for the URL to contain a specific string.
     * Useful for verifying navigation after actions.
     *
     * @param partialUrl The text that should appear in the URL
     */
    protected void waitForUrlContains(String partialUrl) {
        page.waitForURL("**" + partialUrl + "**");
    }

    // ---------------------------------------------------------
    // SAFE ACTIONS - Resilient methods with retry logic
    // ---------------------------------------------------------

    /**
     * Safely clicks an element with automatic retry logic.
     *
     * This method:
     * 1. Waits for the element to be visible
     * 2. Waits for it to be attached to DOM
     * 3. Attempts to click with up to 3 retries
     * 4. Uses small delays between retries for stability
     *
     * Retries help handle race conditions like:
     * - Element moving due to animations
     * - Element being temporarily overlapped by other elements
     * - Brief DOM updates that detach/reattach elements
     *
     * @param selector CSS or XPath selector for the element
     * @throws Exception if all retry attempts fail
     */
    protected void safeClick(String selector) {
        waitForVisible(selector);
        waitForAttached(selector);

        Locator loc = page.locator(selector);

        // Retry mechanism: try up to 3 times before giving up
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                loc.click();
                return;  // Success! Exit the method
            } catch (Exception e) {
                if (attempt == 3) {
                    // Last attempt failed - throw the error
                    throw e;
                }
                // Small backoff before retry to allow page to stabilize
                page.waitForTimeout(200);
            }
        }
    }

    /**
     * Safely types text into an input field.
     *
     * This method:
     * 1. Waits for the element to be visible and attached
     * 2. Clears any existing text
     * 3. Fills in the new text
     *
     * Note: fill() is faster than type() because it doesn't simulate
     * individual keystrokes. Use type() if you need to trigger
     * keyboard events like onKeyPress.
     *
     * @param selector CSS or XPath selector for the input field
     * @param text The text to enter
     */
    protected void safeType(String selector, String text) {
        waitForVisible(selector);
        waitForAttached(selector);

        Locator loc = page.locator(selector);
        loc.fill("");      // Clear any existing text first
        loc.fill(text);    // Enter the new text
    }

    /**
     * Safely retrieves the inner text of an element.
     *
     * @param selector CSS or XPath selector for the element
     * @return The text content of the element
     */
    protected String safeGetText(String selector) {
        waitForVisible(selector);
        return page.locator(selector).innerText();
    }

    /**
     * Checks if an element is currently visible on the page.
     *
     * Unlike waitForVisible(), this method:
     * - Returns immediately (doesn't wait)
     * - Returns false instead of throwing an error if element is not visible
     *
     * Useful for conditional logic in tests (if element exists, do X).
     *
     * @param selector CSS or XPath selector for the element
     * @return true if element is visible, false otherwise
     */
    protected boolean safeIsVisible(String selector) {
        try {
            return page.locator(selector).isVisible();
        } catch (Exception e) {
            // Element not found or not visible - return false instead of throwing error
            return false;
        }
    }

    // ---------------------------------------------------------
    // NAVIGATION
    // ---------------------------------------------------------

    /**
     * Navigates to a URL and waits for the page to load.
     *
     * @param url The full URL to navigate to (e.g., "https://example.com")
     */
    protected void navigateTo(String url) {
        page.navigate(url);
        waitForPageLoad();  // Ensure page is loaded before continuing
    }
}