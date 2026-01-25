package org.example;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChargingNetworkApp {

    public static void main(String[] args) {
        LocationManager locationManager = new LocationManager();
        ClientManager clientManager = new ClientManager();
        NetworkStatusService networkStatusService = new NetworkStatusService();
        BillingService billingService = new BillingService();

        // Demo-Daten aufbauen (unabhängig von Cucumber-Tests)
        locationManager.createLocation("LOC-1", "Vienna Center");
        final Location location = locationManager.getLocation("LOC-1");
        location.getPriceModel().setPricePerKwh("AC", 0.39);
        location.getPriceModel().setPricePerKwh("DC", 0.35);
        location.getPriceModel().setPricePerMinute("AC", 0.01);
        location.getPriceModel().setPricePerMinute("DC", 0.02);
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

        new TopUpTransaction(UUID.randomUUID().toString(), 100.00, LocalDateTime.now(), client);
        new TopUpTransaction(UUID.randomUUID().toString(), 100.00, LocalDateTime.now(), client);

        System.out.println(client);

        System.out.println("## simulate charging sessions");

        LocalDateTime start = LocalDateTime.now();
        cp1.startChargingSession(start, client);
        cp1.stopChargingSession(start.plusMinutes(10), 5.00);

        start = start.plusHours(1);
        cp1.startChargingSession(start, client);
        cp1.stopChargingSession(start.plusMinutes(20), 10.00);

        start = start.plusHours(1);
        cp1.startChargingSession(start, client);
        cp1.stopChargingSession(start.plusMinutes(30), 15.00);

        System.out.println(client);

        System.out.println("## Network Status");
        networkStatusService.getNetworkStatus(locationManager)
                .forEach(System.out::println);

        System.out.println("## Invoice Status");
        System.out.println(billingService.getInvoiceStatus(client));

    }
}
