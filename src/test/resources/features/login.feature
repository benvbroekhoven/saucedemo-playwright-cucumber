Feature: Login to SauceDemo

  Scenario: Successful login with standard user
    When I login with username "standard_user" and password "secret_sauce"
    Then I should see the products page

  Scenario: Login with invalid credentials
    When I try to login with username "locked_out_user" and password "wrong_password"
    Then I should see a login error message
