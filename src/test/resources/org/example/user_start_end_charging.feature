Feature: Client starts and ends charging
  As a client
  I want to select a station, start a session, and stop charging with final cost
  So I can charge my EV and understand what I paid

  Background:
    Given there is an EV driver with id "Cl1"
    And the balance of "Cl1" is at least 20.00
    And station "AC-001" in location "Vienna Center" is has a rate of 0.39
    And station "AC-001" in location "Vienna Center" and is not occupied
    And station "DC-001" in location "Vienna Center" is has a rate of 0.35
    And station "DC-001" in location "Vienna Center" and is occupied

  Scenario: Start charging
    When user "Cl1" start charging at station "AC-001" at location "Vienna Center"
    Then a charging session should start for station "AC-001" at "Vienna Center"
    And the station "AC-001" in location "Vienna Center" status should become occupied

  Scenario: Stop charging and see final cost
    Given an active charging session exists for station "DC-001" at "Vienna Center"
    And user "Cl2" has a balance of 100.00
    When the charging session for station "DC-001" at "Vienna Center" is stopped after 100.00 KWh
    Then the station "DC-001" in location "Vienna Center" status should be not occupied
    And user "Cl2" has a balance of 65.00
