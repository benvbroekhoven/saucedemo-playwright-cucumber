Feature: Checkout process

  Background:
    When I login with username "standard_user" and password "secret_sauce"

  Scenario: Buy a product successfully
    When I buy the product "Sauce Labs Backpack"
    Then I should see the order confirmation
