Feature: Client tops up prepaid balance and views invoice status
  As a client
  I want to top up my balance and view my invoice status
  So I can keep charging and understand what I owe

  Background:
    Given the user "Cl3" exists
    And user "Cl3" has a balance of 199.2

  Scenario: Top up balance creates a top-up transaction
    When client "Cl3" tops up balance by 20.00
    Then user "Cl3" has a balance of 219.2
    And user "Cl3" should have 2 top up transactions

  Scenario: Invoice status contains item details (sorted by start time)
    Then invoice status for "Cl3" should contain items
      | locationName  | chargingPointId | chargingMode |
      | Vienna Center | DC-002          | DC           |
      | Vienna Center | DC-002          | DC           |

  Scenario: Error - top up with negative amount is not allowed
    When client "Cl3" tops up balance by -5.00
    Then an error should occur
