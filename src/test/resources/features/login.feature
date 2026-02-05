Feature: Login to SauceDemo

  Background:
    Given I login with username "standard_user" and password "secret_sauce"

  Scenario: Successful login with standard user

    Then I should see the products page

  Scenario: Login with invalid credentials
    When I try to login with username "locked_out_user" and password "wrong_password"
    Then I should see a login error message
