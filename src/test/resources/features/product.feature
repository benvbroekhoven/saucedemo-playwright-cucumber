Feature: Product interactions

  Background:
    When I login with username "standard_user" and password "secret_sauce"

  Scenario: Add a single product
    When I add the product "Sauce Labs Backpack" to the cart
    Then the cart should contain 1 items

  Scenario: Add multiple products
    When I add the products "Sauce Labs Backpack, Sauce Labs Bike Light" to the cart
    Then the cart should contain 2 items

  Scenario: Remove a single product
    And I add the product "Sauce Labs Backpack" to the cart
    When I remove the product "Sauce Labs Backpack" from the cart
    Then the cart should contain 0 items

  Scenario: Remove multiple products
    And I add the products "Sauce Labs Backpack, Sauce Labs Bike Light" to the cart
    When I remove the products "Sauce Labs Backpack, Sauce Labs Bike Light" from the cart
    Then the cart should contain 0 items
