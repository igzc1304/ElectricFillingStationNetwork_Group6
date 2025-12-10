Feature: Manage Locations

  Scenario: Create a new charging location
    Given there is no location with id "LOC-1"
    When I create a location with id "LOC-1" and name "Vienna Center"
    Then the location list should contain a location with id "LOC-1"

  Scenario: Add a charging point to a location
    Given a location with id "LOC-1" exists
    And the location has no charging points
    When I add a charging point with id "CP-1" of type "AC" to location "LOC-1"
    Then the location "LOC-1" should have 1 charging point

  Scenario: Register a new client
    Given there is no client with id "CUST-1"
    When I register a client with id "CUST-1" and name "Ivan" and email "ivan@example.com"
    Then the system should contain a client with id "CUST-1"
