Feature: Cart Verification with Comprehensive Checks

  Background:
    Given I login with username "standard_user" and password "secret_sauce"

  Scenario: Add multiple items and verify cart contents
    When I add the products "Sauce Labs Backpack, Sauce Labs Bike Light" to the cart
    And I go to the cart
    Then the cart should contain 2 items
    And the cart should contain the following items:
      | Sauce Labs Backpack |
      | Sauce Labs Bike Light |
    And the product "Sauce Labs Backpack" should be in the cart
    And the cart total should be $39.98

  Scenario: Remove item from cart
    When I add the product "Sauce Labs Backpack" to the cart
    And I go to the cart
    And I remove "Sauce Labs Backpack" from the cart
    Then the cart should be empty
    And the product "Sauce Labs Backpack" should not be in the cart

  Scenario: Cart badge consistency check
    When I add the product "Sauce Labs Backpack" to the cart
    Then the cart should contain 1 items
    When I add the product "Sauce Labs Bike Light" to the cart
    Then the cart should contain 2 items
    When I go to the cart
    Then the cart badge count should match the actual item count