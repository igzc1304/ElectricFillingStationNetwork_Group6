package org.example;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Shared test state per scenario.
 */
public class TestWorld {
    public LocationManager locationManager;
    public ClientManager clientManager;
    public NetworkStatusService networkStatusService;
    public BillingService billingService;

    // Keep track of last relevant session for assertions
    public ChargingSession lastChargingSession;
    public Exception lastException;

    public void reset() {
        locationManager = new LocationManager();
        clientManager = new ClientManager();
        networkStatusService = new NetworkStatusService();
        billingService = new BillingService();
        lastChargingSession = null;
        lastException = null;
    }

    /**
     * Baseline data used by multiple features.
     */
    public void seedBaseline() {
        locationManager.createLocation("Vienna Center", "Vienna Center");
        Location vienna = locationManager.getLocation("Vienna Center");
        vienna.getPriceModel().setPricePerKwh("AC", 0.39);
        vienna.getPriceModel().setPricePerKwh("DC", 0.35);
        vienna.getPriceModel().setPricePerMinute("AC", 0.01);
        vienna.getPriceModel().setPricePerMinute("DC", 0.02);
        locationManager.addChargingPoint("Vienna Center", "AC-001", "AC");
        locationManager.addChargingPoint("Vienna Center", "DC-001", "DC");
        locationManager.addChargingPoint("Vienna Center", "DC-002", "DC");

        locationManager.createLocation("Graz South", "Graz South");
        Location graz = locationManager.getLocation("Graz South");
        graz.getPriceModel().setPricePerKwh("AC", 0.30);
        graz.getPriceModel().setPricePerKwh("DC", 0.40);
        graz.getPriceModel().setPricePerMinute("AC", 0.00);
        graz.getPriceModel().setPricePerMinute("DC", 0.00);

        // Clients
        clientManager.registerClient("Cl1", "Client 1", "example@example.com");
        
        clientManager.registerClient("Cl2", "Client 2", "example@example.com");
        clientManager.registerClient("Cl3", "Client 3", "example@example.com");

        new TopUpTransaction(UUID.randomUUID().toString(), 100.0, LocalDateTime.parse("2026-01-01T09:00:00"), clientManager.getClient("Cl1"));
        new TopUpTransaction(UUID.randomUUID().toString(), 100.0, LocalDateTime.parse("2026-01-01T09:10:00"), clientManager.getClient("Cl2"));
        new TopUpTransaction(UUID.randomUUID().toString(), 270.0, LocalDateTime.parse("2026-01-01T09:20:00"), clientManager.getClient("Cl3"));

        // One active session on DC-001 for Cl2
        ChargingPoint dc001 = locationManager.getChargingPoint("Vienna Center", "DC-001");
        dc001.startChargingSession(LocalDateTime.parse("2026-01-01T10:00:00"), clientManager.getClient("Cl2"));

        // Two finished sessions on DC-002 for Cl3
        ChargingPoint dc002 = locationManager.getChargingPoint("Vienna Center", "DC-002");
        dc002.startChargingSession(LocalDateTime.parse("2026-01-01T08:00:00"), clientManager.getClient("Cl3"));
        dc002.stopChargingSession(LocalDateTime.parse("2026-01-01T08:30:00"), 100.0); // cost: 35 + 0.02*30 = 35.6

        dc002.startChargingSession(LocalDateTime.parse("2026-01-01T12:00:00"), clientManager.getClient("Cl3"));
        dc002.stopChargingSession(LocalDateTime.parse("2026-01-01T12:10:00"), 100.0); // cost: 35 + 0.02*10 = 35.2
    }
}
