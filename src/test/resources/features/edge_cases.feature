@edge
Feature: Edge Cases and Error Handling

  Background:
    Given I login with username "standard_user" and password "secret_sauce"

  Scenario: Attempt checkout with empty cart
    When I go to the cart
    Then the cart should be empty
    When I attempt to checkout with an empty cart
    Then I should be prevented from checking out

  Scenario: Add same product multiple times
    When I add "Sauce Labs Backpack" to the cart 3 times
    And I go to the cart
    Then the cart should contain 1 items
    And the product "Sauce Labs Backpack" should be in the cart

  Scenario: Rapid add and remove operations
    When I rapidly add and remove "Sauce Labs Bike Light" 5 times
    Then the final cart state should be "empty"

  Scenario Outline: Checkout validation with invalid data
    When I try to checkout with:
      | Field       | Value         |
      | First Name  | <firstName>   |
      | Last Name   | <lastName>    |
      | Postal Code | <postalCode>  |
    Then I should see a validation error containing "<errorText>"

    Examples:
      | firstName | lastName | postalCode | errorText |
      | [empty]   | Smith    | 12345      | First Name|
      | John      | [empty]  | 12345      | Last Name |
      | John      | Smith    | [empty]    | Postal Code|
      | John      | Smith    | abc        | Postal Code|

  Scenario: Checkout with special characters in name
    When I try to checkout with:
      | Field       | Value       |
      | First Name  | Jean-Luc    |
      | Last Name   | :-)         |
      | Postal Code | 12345       |
    Then the checkout should proceed successfully

  Scenario: Checkout with very long names
    When I try to checkout with:
      | Field       | Value                                    |
      | First Name  | AVeryLongFirstNameThatMightCauseIssues   |
      | Last Name   | AnotherVeryLongLastNameForTesting        |
      | Postal Code | 12345                                    |
    Then the checkout should proceed successfully