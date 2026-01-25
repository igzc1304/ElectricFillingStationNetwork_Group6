Feature: Manage charging points

  As the network owner
  I want to manage charging points at locations
  So that each charging point is clearly identifiable and has a status

  Scenario: Add multiple charging points to a location (datatable)
    Given a location with id "LOC-CP" exists
    When I add charging points to location "LOC-CP"
      | id    | type |
      | CP-1  | AC   |
      | CP-2  | DC   |
    Then the location "LOC-CP" should have 2 charging point

  Scenario: Remove a charging point
    Given a location with id "LOC-CP-RM" exists
    When I add a charging point with id "CP-1" of type "AC" to location "LOC-CP-RM"
    And I remove charging point "CP-1" from location "LOC-CP-RM"
    Then the location "LOC-CP-RM" should have 0 charging point

  Scenario: Set charging point status to OUT_OF_ORDER
    Given a location with id "LOC-CP-STATUS" exists
    When I add a charging point with id "CP-9" of type "DC" to location "LOC-CP-STATUS"
    And I set charging point "CP-9" at location "LOC-CP-STATUS" to status "OUT_OF_ORDER"
    Then charging point "CP-9" at location "LOC-CP-STATUS" should have status "OUT_OF_ORDER"

  Scenario: Error - cannot add a charging point to a missing location
    When I add a charging point with id "CP-X" of type "AC" to location "LOC-DOES-NOT-EXIST"
    Then an error should occur
