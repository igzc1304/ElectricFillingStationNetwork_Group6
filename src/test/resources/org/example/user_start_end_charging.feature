Feature: Client starts and ends charging
  As a client
  I want to select a charging point, start a session, and stop charging with final cost
  So I can charge my EV and understand what I paid

  Background:
    Given there is an EV driver with id "Cl1"
    And the balance of "Cl1" is at least 20.00
    And charging point "AC-001" at location "Vienna Center" has kWh rate 0.39
    And charging point "AC-001" at location "Vienna Center" has status "FREE"

  Scenario: Start charging sets status OCCUPIED
    When user "Cl1" starts charging at charging point "AC-001" at location "Vienna Center" at "2026-01-01T11:00:00"
    Then a charging session should start for charging point "AC-001" at location "Vienna Center"
    And charging point "AC-001" at location "Vienna Center" should become "OCCUPIED"

  Scenario: Stop charging deducts kWh+minutes cost
    When user "Cl1" starts charging at charging point "AC-001" at location "Vienna Center" at "2026-01-01T11:00:00"
    And the charging session for charging point "AC-001" at location "Vienna Center" is stopped at "2026-01-01T11:10:00" after 10.0 kWh
    Then charging point "AC-001" at location "Vienna Center" should become "FREE"
    # cost = 10*0.39 + 10min*0.01 = 3.9 + 0.1 = 4.0 ; 100 - 4 = 96
    And user "Cl1" balance should be 96.0
    And last session cost should be 4.0

  Scenario: Price snapshot - changing price mid-session does not change cost
    When user "Cl1" starts charging at charging point "AC-001" at location "Vienna Center" at "2026-01-01T11:00:00"
    And I set the "AC" rate for location "Vienna Center" to 1.00 per kWh
    And the charging session for charging point "AC-001" at location "Vienna Center" is stopped at "2026-01-01T11:00:00" after 10.0 kWh
    # duration 0 -> only kWh ; cost must still use 0.39 = 3.9
    Then last session cost should be 3.9

  Scenario: Error - cannot start charging on an OUT_OF_ORDER charging point
    When I set charging point "AC-001" at location "Vienna Center" to status "OUT_OF_ORDER"
    And user "Cl1" starts charging at charging point "AC-001" at location "Vienna Center" at "2026-01-01T11:00:00"
    Then an error should occur
