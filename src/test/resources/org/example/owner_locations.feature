Feature: Manage locations

  As the network owner
  I want to create locations
  So that I can maintain the structure of my charging network

  Scenario: Create a new charging location
    Given there is no location with id "LOC-1"
    When I create a location with id "LOC-1" and name "Vienna Center"
    Then the location list should contain a location with id "LOC-1"
