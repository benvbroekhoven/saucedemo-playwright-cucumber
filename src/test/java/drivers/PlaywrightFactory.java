package drivers;

import com.microsoft.playwright.*;
import config.ConfigManager;

public class PlaywrightFactory {

    private static final ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browser = new ThreadLocal<>();
    private static final ThreadLocal<Page> page = new ThreadLocal<>();

    public static Page getPage() {
        if (page.get() == null) {
            Playwright pw = Playwright.create();
            playwright.set(pw);

            String browserName = ConfigManager.get("browser");
            BrowserType browserType = switch (browserName.toLowerCase()) {
                case "firefox" -> pw.firefox();
                case "webkit" -> pw.webkit();
                default -> pw.chromium();
            };

            boolean headless = Boolean.parseBoolean(ConfigManager.get("headless"));

            Browser br = browserType.launch(
                    new BrowserType.LaunchOptions().setHeadless(headless)
            );
            browser.set(br);

            Page pg = br.newPage();
            page.set(pg);
        }
        return page.get();
    }

    public static void close() {
        if (page.get() != null) {
            page.get().close();
            page.remove();
        }
        if (browser.get() != null) {
            browser.get().close();
            browser.remove();
        }
        if (playwright.get() != null) {
            playwright.get().close();
            playwright.remove();
        }
    }
}
