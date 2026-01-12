Feature: Client tops up prepaid balance and views invoices
  As a client
  I want to top up my balance and view my invoices
  So I can keep charging and understand what I owe

  Background:
    Given the user "Cl3" exists
    And client "Cl3" has 2 invoices
    And user "Cl3" has a balance of 200.00

  @user @topup
  Scenario: Top up balance creates a top-up transaction
    When client "Cl3" tops up balance by 20.00
    Then user "Cl3" has a balance of 220.00
