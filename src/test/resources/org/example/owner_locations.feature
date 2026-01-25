Feature: Manage locations

  As the network owner
  I want to create, update and delete locations
  So that I can maintain the structure of my charging network

  Scenario: Create multiple locations (datatable)
    When I create locations
      | id           | name         |
      | LOC-NEW-1    | Linz Center  |
      | LOC-NEW-2    | Salzburg Nord|
    Then the location list should contain a location with id "LOC-NEW-1"
    And the location list should contain a location with id "LOC-NEW-2"

  Scenario: Update a location name
    When I create a location with id "LOC-RENAME" and name "Old Name"
    And I rename location "LOC-RENAME" to "New Name"
    Then location "LOC-RENAME" should have name "New Name"

  Scenario: Delete a location
    When I create a location with id "LOC-DELETE" and name "To Delete"
    And I delete location "LOC-DELETE"
    Then no location with id "LOC-DELETE" should exist

  Scenario: Error - creating a duplicate location id is not allowed
    When I create a location with id "LOC-DUP" and name "First"
    And I create a location with id "LOC-DUP" and name "Second"
    Then an error should occur
