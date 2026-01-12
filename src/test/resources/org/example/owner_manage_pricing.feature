Feature: Owner manages pricing
  As an owner
  I want to set and change AC/DC rates per location
  So billing is correct and pricing can react to demand

  Background:
    Given the location "Vienna Center" exists

  @owner @pricing
  Scenario: Define a rate for AC/DC
    When I set the "AC" rate for location "Vienna Center" to 0.39 per kWh
    Then the "AC" rate for location "Vienna Center" should be 0.39 per kWh

  @owner @pricing
  Scenario: Set unique rates per location
    Given the location "Graz South" exists
    When I set the "AC" rate for location "Vienna Center" to 0.39 per kWh
    And I set the "DC" rate for location "Graz South" to 0.35 per kWh
    Then the "AC" rate for location "Vienna Center" should be 0.39 per kWh
    And the "DC" rate for location "Graz South" should be 0.35 per kWh

