Feature: Manage charging points

  As the network owner
  I want to add charging points to locations
  So that each charging point is clearly identifiable

  Scenario: Add a charging point to a location
    Given a location with id "LOC-1" exists
    And the location has no charging points
    When I add a charging point with id "CP-1" of type "AC" to location "LOC-1"
    Then the location "LOC-1" should have 1 charging point
