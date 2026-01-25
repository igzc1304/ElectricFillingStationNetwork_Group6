Feature: Network status
  As a customer or owner
  I want to see the current status of the charging network
  So that I can see prices and whether charging points are free, occupied or out of order

  Scenario: List network status with prices and operating status
    Then the network status list should contain
      | locationName  | chargingPointId | status   |
      | Vienna Center | AC-001          | FREE     |
      | Vienna Center | DC-001          | OCCUPIED |
      | Vienna Center | DC-002          | FREE     |

  Scenario: Edge - network status is empty when there are no locations
    Given the network has no locations
    Then the network status list should be empty
