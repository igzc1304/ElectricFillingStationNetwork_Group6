Feature: Register EV driver account

  As an EV driver
  I want to register once
  So that I can use the charging network

  Scenario: Register a new EV driver account
    Given there is no EV driver with id "CUST-1"
    When I register an EV driver with id "CUST-1" and name "Ivan" and email "ivan@example.com"
    Then the system should contain an EV driver with id "CUST-1"
