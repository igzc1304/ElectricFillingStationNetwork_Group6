Feature: Register EV driver account

  As an EV driver
  I want to register once
  So that I can use the charging network

  Scenario: Register a new EV driver account
    Given there is no EV driver with id "CUST-NEW"
    When I register an EV driver with id "CUST-NEW" and name "Ivan" and email "ivan@example.com"
    Then the system should contain an EV driver with id "CUST-NEW"

  Scenario: Error - registering the same id twice is not allowed
    Given there is no EV driver with id "CUST-DUP"
    When I register an EV driver with id "CUST-DUP" and name "A" and email "a@example.com"
    And I register an EV driver with id "CUST-DUP" and name "B" and email "b@example.com"
    Then an error should occur
