🧩 SauceDemo Playwright + Cucumber Framework
A clean, modular, enterprise‑grade UI test automation framework built with:

Playwright for Java

Cucumber (BDD)

JUnit

Allure Reporting

Page Object Model + Flows architecture

This project demonstrates a scalable, maintainable approach to UI automation using modern tooling and clean design patterns.

🚀 Features
✔ Modern Automation Stack
Playwright for fast, reliable browser automation

Cucumber for readable BDD scenarios

JUnit for execution

Allure for rich reporting

✔ Clean Architecture
Page Object Model (POM)

Business‑level Flows (LoginFlow, ProductFlow, CheckoutFlow)

Reusable BasePage with safe actions

Configurable test data and environment setup

✔ Developer‑Friendly
Parallel execution ready

Clear selectors and robust waits

Easy to extend with API tests, CI/CD, or Xray integration

📁 Project Structure
Code
src
 └── test
     ├── java
     │   ├── flows/          # Business flows (LoginFlow, ProductFlow, CheckoutFlow)
     │   ├── pages/          # Page Objects
     │   ├── steps/          # Cucumber step definitions
     │   ├── runners/        # Test runner (JUnit + Cucumber)
     │   └── utils/          # Helpers, config, drivers
     └── resources
         └── features/       # Gherkin feature files
🧪 Running Tests
Run all tests:
bash
mvn clean test
Run a specific feature:
bash
mvn clean test -Dcucumber.filter.tags="@checkout"
📊 Allure Reporting
Generate and view the Allure report:

bash
allure serve allure-results
Or generate a static report:

bash
allure generate allure-results --clean
allure open
🧱 Technology Stack
Component	Version / Notes
Java	17+
Playwright	Java bindings
Cucumber	BDD framework
JUnit	Test runner
Maven	Build tool
Allure	Reporting
🧭 Flows Architecture
This framework uses a Flows layer to keep business logic separate from UI mechanics.

Examples:

LoginFlow → handles login steps

ProductFlow → add/remove products

CheckoutFlow → full purchase flow

This keeps step definitions clean and readable.

🧪 Sample Scenario
gherkin
Scenario: Buy a product successfully
  Given I login with username "standard_user" and password "secret_sauce"
  When I buy the product "Sauce Labs Backpack"
  Then I should see the order confirmation
🛠️ Setup
Install dependencies
bash
mvn install
Install Playwright browsers
bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
Install Allure (Windows)
Using Scoop:

bash
scoop install allure
Using Chocolatey:

bash
choco install allure
🔐 GitHub SSH Setup
This project uses SSH for Git operations.
If you're on a corporate network, configure:

Code
Host github.com
  HostName ssh.github.com
  Port 443
  User git
  IdentityFile C:/Users/Ben/.ssh/id_ed25519
  IdentitiesOnly yes
