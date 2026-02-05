# SauceDemo Playwright Cucumber Test Automation Framework

## 📋 Project Overview

This is a test automation framework for the SauceDemo e-commerce application using:
- **Playwright** for browser automation
- **Cucumber** for BDD (Behavior Driven Development)
- **JUnit** as the test runner
- **Allure** for detailed HTML test reporting
- **AssertJ** for fluent assertions

## 🏗️ Project Architecture

The framework follows the **Page Object Model (POM)** pattern with an additional **Flow Layer** for better separation of concerns:

```
src/test/java/
├── config/          # Configuration management
├── drivers/         # Browser driver factory
├── pages/           # Page Objects (UI element interactions)
├── flows/           # Business logic and multi-page workflows
├── steps/           # Cucumber step definitions (Gherkin mapping)
├── hooks/           # Test lifecycle management (@Before/@After)
└── runners/         # Test execution entry point

src/test/resources/
├── config/          # Environment-specific properties files
└── features/        # Cucumber feature files (Gherkin scenarios)
```

### Architecture Layers Explained

#### 1. **Config Layer** (`config/`)
- **Purpose**: Manages environment-specific configurations
- **Key Class**: `ConfigManager.java`
- **What it does**: Loads properties from files like `dev.properties`, `test.properties`
- **Why**: Allows the same tests to run against different environments without code changes

#### 2. **Driver Layer** (`drivers/`)
- **Purpose**: Manages Playwright browser instances
- **Key Class**: `PlaywrightFactory.java`
- **What it does**: Creates and manages browser lifecycle with thread-safety for parallel execution
- **Why**: Centralizes browser management and prevents memory leaks

#### 3. **Page Layer** (`pages/`)
- **Purpose**: Represents web pages and their elements
- **Pattern**: Page Object Model (POM)
- **Key Classes**:
    - `BasePage.java` - Common utilities for all pages
    - `LoginPage.java` - Login page interactions
    - `InventoryPage.java` - Product catalog interactions
    - `CartPage.java` - Shopping cart interactions
    - `CheckoutPage.java` - Checkout form interactions
    - `CheckoutOverviewPage.java` - Order review interactions
    - `CheckoutCompletePage.java` - Order confirmation
- **What they do**: Encapsulate element locators and low-level interactions (click, type, etc.)
- **Why**: Keeps element selectors in one place, making tests easier to maintain when UI changes

#### 4. **Flow Layer** (`flows/`)
- **Purpose**: Orchestrates multi-page workflows and business logic
- **Key Classes**:
    - `LoginFlow.java` - Login scenarios (success/failure)
    - `ProductFlow.java` - Product management (add/remove items)
    - `CheckoutFlow.java` - Complete purchase workflow
- **What they do**: Chain multiple page interactions to complete business processes
- **Why**:
    - Keeps page objects focused on element interactions only
    - Provides reusable business logic
    - Makes step definitions cleaner and more readable

#### 5. **Steps Layer** (`steps/`)
- **Purpose**: Maps Gherkin steps to automation code
- **Key Classes**:
    - `LoginSteps.java` - Login-related steps
    - `ProductSteps.java` - Product-related steps
    - `CheckoutSteps.java` - Checkout-related steps
- **What they do**: Act as the "glue" between feature files and automation code
- **Why**: Enables writing tests in plain English (Gherkin) that non-technical stakeholders can understand

#### 6. **Hooks Layer** (`hooks/`)
- **Purpose**: Manages test lifecycle (setup/teardown)
- **Key Class**: `PlaywrightHooks.java`
- **What it does**:
    - `@Before`: Initializes browser before each scenario
    - `@After`: Captures screenshots on failure and closes browser
- **Why**: Ensures consistent test environment and proper cleanup

#### 7. **Runner Layer** (`runners/`)
- **Purpose**: Entry point for test execution
- **Key Class**: `TestRunner.java`
- **What it does**: Configures Cucumber to find features, steps, and generate reports
- **Why**: Single place to configure all test execution settings

## 🎯 Design Patterns Used

### 1. **Page Object Model (POM)**
- **What**: Each web page is represented by a Java class
- **Why**:
    - Reduces code duplication
    - Makes tests easier to maintain
    - Separates test logic from UI details

### 2. **Flow Pattern** (Custom Layer)
- **What**: Business logic layer between pages and steps
- **Why**:
    - Pages stay focused on element interactions
    - Step definitions stay simple and readable
    - Business logic is reusable across tests

### 3. **Factory Pattern**
- **What**: `PlaywrightFactory` creates browser instances
- **Why**:
    - Centralizes browser creation logic
    - Ensures thread safety for parallel execution
    - Makes it easy to switch browsers via configuration

### 4. **Singleton Pattern**
- **What**: `ConfigManager` loads configuration once
- **Why**:
    - Improves performance (loads once, used many times)
    - Ensures consistent configuration across all tests

## 🚀 How Tests Execute (Flow Diagram)

```
1. JUnit starts TestRunner
   ↓
2. Cucumber finds feature files in src/test/resources/features/
   ↓
3. For each scenario:
   
   3a. PlaywrightHooks @Before runs
       - Logs scenario name to Allure
       - Initializes browser via PlaywrightFactory
   
   3b. Cucumber matches Gherkin steps to step definitions
       - "Given I login..." → LoginSteps.i_login_with_username_and_password()
       - "When I add product..." → ProductSteps.i_add_the_product_to_the_cart()
       - "Then cart should contain..." → ProductSteps.the_cart_should_contain_items()
   
   3c. Step definitions call Flow classes
       - LoginSteps calls LoginFlow.loginExpectingSuccess()
       - ProductSteps calls ProductFlow.addProduct()
   
   3d. Flow classes call Page Objects
       - LoginFlow calls LoginPage.open() and LoginPage.loginAs()
       - ProductFlow calls InventoryPage.addItemToCart()
   
   3e. Page Objects interact with browser
       - LoginPage uses PlaywrightFactory.getPage() to interact with elements
       - Uses BasePage utilities (safeClick, safeType, etc.)
   
   3f. PlaywrightHooks @After runs
       - Takes screenshot if test failed
       - Closes browser
       - Attaches evidence to Allure report
   
   ↓
4. Allure generates HTML report with results
```

## 📝 Example Test Flow

Let's trace how this test executes:

```gherkin
Scenario: Add a single product
  Given I login with username "standard_user" and password "secret_sauce"
  When I add the product "Sauce Labs Backpack" to the cart
  Then the cart should contain 1 items
```

**Execution Steps:**

1. **Hook @Before runs**
    - `PlaywrightHooks.setup()` initializes browser

2. **Given step executes**
    - Cucumber calls `LoginSteps.i_login_with_username_and_password("standard_user", "secret_sauce")`
    - LoginSteps calls `LoginFlow.loginExpectingSuccess()`
    - LoginFlow calls `LoginPage.open()` → navigates to baseUrl
    - LoginFlow calls `LoginPage.loginAs()` → enters credentials and clicks login
    - LoginFlow calls `InventoryPage.isLoaded()` → waits for products page

3. **When step executes**
    - Cucumber calls `ProductSteps.i_add_the_product_to_the_cart("Sauce Labs Backpack")`
    - ProductSteps calls `ProductFlow.addProduct()`
    - ProductFlow calls `InventoryPage.addItemToCart()`
    - InventoryPage finds product by name using XPath and clicks "Add to cart" button

4. **Then step executes**
    - Cucumber calls `ProductSteps.the_cart_should_contain_items(1)`
    - ProductSteps calls `ProductFlow.getCartCount()`
    - ProductFlow calls `InventoryPage.getCartCount()`
    - InventoryPage reads cart badge and returns count
    - AssertJ assertion verifies count equals 1

5. **Hook @After runs**
    - `PlaywrightHooks.teardown()` closes browser
    - If test failed, screenshot would be attached to Allure report

## 🔧 Configuration

### Environment Configuration (`src/test/resources/config/`)

**dev.properties**:
```properties
baseUrl=https://www.saucedemo.com/
browser=chromium
headless=true
```

Switch environments with:
```bash
mvn test -Denv=test    # Uses test.properties
mvn test -Denv=prod    # Uses prod.properties
```

### Browser Configuration

Change browser in properties file:
- `chromium` - Google Chrome/Chromium
- `firefox` - Mozilla Firefox
- `webkit` - Safari engine

### Headless Mode

- `headless=true` - Runs without UI (faster, for CI/CD)
- `headless=false` - Shows browser (for debugging)

## 📊 Reporting

### Allure Reports

Generate and view report:
```bash
mvn clean test                    # Run tests
allure serve target/allure-results # Open report in browser
```

### CI/CD Integration

The project includes GitHub Actions workflow (`.github/workflows/deploy.yml`) that:
1. Runs tests on every push
2. Generates Allure report
3. Publishes report to GitHub Pages
4. Maintains historical trends

## 🧪 Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Specific Feature
```bash
mvn test -Dcucumber.features=src/test/resources/features/login.feature
```

### Run by Tag
```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

### Run in Different Browser
```bash
mvn test -Denv=dev -Dbrowser=firefox
```

## 🛠️ Key Technologies

### Playwright
- **What**: Modern browser automation framework by Microsoft
- **Why**: Fast, reliable, supports multiple browsers
- **Features**: Auto-waiting, network interception, screenshots

### Cucumber
- **What**: BDD framework that uses Gherkin language
- **Why**: Enables collaboration between technical and non-technical team members
- **Features**: Plain English test scenarios, reusable steps

### Page Object Model
- **What**: Design pattern for organizing page interactions
- **Why**: Makes tests maintainable and reduces duplication
- **Features**: Element locators in one place, reusable methods

### Allure
- **What**: Test reporting framework
- **Why**: Beautiful, detailed HTML reports with screenshots
- **Features**: Historical trends, categories, attachments

## 🔍 Debugging Tips

### 1. Run with Browser Visible
Set `headless=false` in dev.properties

### 2. Add Wait/Pause
```java
page.waitForTimeout(5000);  // Pause for 5 seconds
```

### 3. Take Manual Screenshot
```java
page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("debug.png")));
```

### 4. Print Element Info
```java
System.out.println("Element visible: " + safeIsVisible(selector));
System.out.println("Element text: " + safeGetText(selector));
```

### 5. Use Playwright Inspector
```bash
mvn exec:java -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="codegen https://www.saucedemo.com"
```

## 📚 Best Practices

1. **Keep Page Objects Simple**
    - Only element interactions
    - No business logic
    - No assertions

2. **Use Flow Classes for Business Logic**
    - Multi-page workflows
    - Complex operations
    - Reusable processes

3. **Step Definitions Should Be Thin**
    - Just map Gherkin to code
    - Call flows, not pages directly
    - Use descriptive step names

4. **Always Use Waits**
    - Wait for elements to be visible
    - Wait for network idle after navigation
    - Don't use hard-coded sleeps

5. **Write Readable Tests**
    - Use descriptive variable names
    - Add comments explaining "why", not "what"
    - Keep scenarios focused and short

## 🤝 Contributing

When adding new tests:
1. Create feature file in `src/test/resources/features/`
2. Create/update page objects in `pages/`
3. Create/update flows in `flows/`
4. Create/update step definitions in `steps/`
5. Run tests locally before committing
6. Verify Allure report looks correct

## 📞 Support

For questions or issues:
- Check existing feature files for examples
- Review page objects for available methods
- Examine flows for reusable logic
- Run with `headless=false` for visual debugging