Feature: Owner manages pricing
  As an owner
  I want to set and change AC/DC rates per location (kWh + minutes)
  So billing is correct and pricing can react to demand

  Background:
    Given the location "Vienna Center" exists

  Scenario: Define kWh and time rate for AC
    When I set the "AC" rate for location "Vienna Center" to 0.39 per kWh
    And I set the "AC" time rate for location "Vienna Center" to 0.01 per minute
    Then the "AC" rate for location "Vienna Center" should be 0.39 per kWh
    And the "AC" time rate for location "Vienna Center" should be 0.01 per minute

  Scenario: Error - negative time rate is not allowed
    When I set the "DC" time rate for location "Vienna Center" to -0.01 per minute
    Then an error should occur
