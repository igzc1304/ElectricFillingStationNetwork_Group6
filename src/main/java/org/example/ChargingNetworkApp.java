package org.example;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChargingNetworkApp {

    public static void main(String[] args) {
        LocationManager locationManager = new LocationManager();
        ClientManager clientManager = new ClientManager();

        // Demo-Daten aufbauen (unabhängig von Cucumber-Tests)
        locationManager.createLocation("LOC-1", "Vienna Center");
        final Location location = locationManager.getLocation("LOC-1");
        location.getPriceModel().setPricePerKwh("AC", 0.39);
        location.getPriceModel().setPricePerKwh("DC", 0.35);
        locationManager.addChargingPoint("LOC-1", "CP-1", "AC");
        ChargingPoint cp1 = location.getChargingPoints().stream()
                .filter(cp -> "CP-1".equals(cp.getId()))
                .findFirst()
                .get();

        clientManager.registerClient("CUST-1", "Ivan", "ivan@example.com");

        final EvDriver client = clientManager.getClient("CUST-1");


        // Einfache Ausgabe der Business-Logik
        System.out.println("=== Charging Network Demo ===");

        System.out.println(location);

        System.out.println(client);

        System.out.println("## Top up balance");

        new TopUpTransaction(UUID.randomUUID().toString(), 100.00, LocalDateTime.now(), client.getAccount());
        new TopUpTransaction(UUID.randomUUID().toString(), 100.00, LocalDateTime.now(), client.getAccount());
        new TopUpTransaction(UUID.randomUUID().toString(), 100.00, LocalDateTime.now(), client .getAccount());

        System.out.println(client);

        System.out.println("## simulate charging sessions");

        cp1.startChargingSession(LocalDateTime.now(), client);
        cp1.stopChargingSession(LocalDateTime.now(), 5.00);

        cp1.startChargingSession(LocalDateTime.now(), client);
        cp1.stopChargingSession(LocalDateTime.now(), 10.00);

        cp1.startChargingSession(LocalDateTime.now(), client);
        cp1.stopChargingSession(LocalDateTime.now(), 15.00);

        System.out.println(client);

    }
}
