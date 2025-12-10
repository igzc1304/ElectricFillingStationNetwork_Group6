package org.example;

public class ChargingNetworkApp {

    public static void main(String[] args) {
        LocationManager locationManager = new LocationManager();
        ClientManager clientManager = new ClientManager();

        // Demo-Daten aufbauen (unabhängig von Cucumber-Tests)
        locationManager.createLocation("LOC-1", "Vienna Center");
        locationManager.addChargingPoint("LOC-1", "CP-1", "AC");

        clientManager.registerClient("CUST-1", "Ivan", "ivan@example.com");

        // Einfache Ausgabe der Business-Logik
        System.out.println("=== Charging Network Demo ===");

        Location location = locationManager.getLocation("LOC-1");
        System.out.println("Location: " + location.getId() + " - " + location.getName());
        System.out.println("Charging points at location: " + location.getChargingPoints().size());

        EvDriver client = clientManager.getClient("CUST-1");
        System.out.println("Registered client: " + client.getId()
                + " - " + client.getName()
                + " (" + client.getEmail() + ")");
    }
}
