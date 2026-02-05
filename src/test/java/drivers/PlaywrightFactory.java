package drivers;

import com.microsoft.playwright.*;
import config.ConfigManager;

/**
 * PlaywrightFactory manages the lifecycle of Playwright browser instances.
 *
 * This factory uses ThreadLocal to ensure thread safety for parallel test execution.
 * Each thread gets its own Playwright instance, Browser, and Page.
 *
 * Key features:
 * - Thread-safe browser management
 * - Configuration-driven browser selection (chromium, firefox, webkit)
 * - Headless mode control via configuration
 * - Proper resource cleanup to prevent memory leaks
 */
public class PlaywrightFactory {

    // ThreadLocal ensures each thread has its own Playwright instance
    // This is crucial for parallel test execution
    private static final ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browser = new ThreadLocal<>();
    private static final ThreadLocal<Page> page = new ThreadLocal<>();

    /**
     * Gets or creates a Page instance for the current thread.
     *
     * This method is lazy-initialized - it only creates browser instances
     * when first called. Subsequent calls return the existing instance.
     *
     * @return A Page object ready for automation
     */
    public static Page getPage() {
        if (page.get() == null) {
            // Initialize Playwright - this starts the browser driver process
            Playwright pw = Playwright.create();
            playwright.set(pw);

            // Get browser type from configuration (chromium, firefox, webkit)
            String browserName = ConfigManager.get("browser");
            BrowserType browserType = switch (browserName.toLowerCase()) {
                case "firefox" -> pw.firefox();
                case "webkit" -> pw.webkit();
                default -> pw.chromium();  // Default to Chromium for stability
            };

            // Get headless mode from configuration
            // Headless = true: runs without UI (faster, used in CI/CD)
            // Headless = false: shows browser UI (useful for debugging)
            boolean headless = Boolean.parseBoolean(ConfigManager.get("headless"));

            // Launch the actual browser process
            Browser br = browserType.launch(
                    new BrowserType.LaunchOptions().setHeadless(headless)
            );
            browser.set(br);

            // Create a new page (tab) in the browser
            Page pg = br.newPage();
            page.set(pg);
        }
        return page.get();
    }

    /**
     * Closes all browser resources for the current thread.
     *
     * This method should be called after each test to:
     * - Free up system resources
     * - Prevent memory leaks
     * - Ensure a clean state for the next test
     *
     * The cleanup happens in reverse order of creation:
     * Page -> Browser -> Playwright
     */
    public static void close() {
        // Close the page (tab)
        if (page.get() != null) {
            page.get().close();
            page.remove();  // Remove from ThreadLocal to free memory
        }

        // Close the browser process
        if (browser.get() != null) {
            browser.get().close();
            browser.remove();
        }

        // Close the Playwright driver connection
        if (playwright.get() != null) {
            playwright.get().close();
            playwright.remove();
        }
    }
}