package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChargingPoint {

    private final String id;
    private final String type;   // "AC" or "DC"
    private ChargingStatus status = ChargingStatus.FREE;

    private final List<ChargingSession> pastChargingSessions = new ArrayList<>();
    private ChargingSession.Builder currentChargingSession;

    private final PriceModel priceModel;
    private final Location location;

    public ChargingPoint(String id, String type, PriceModel priceModel, Location location) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ChargingPoint id must not be blank");
        }
        if (!"AC".equals(type) && !"DC".equals(type)) {
            throw new IllegalArgumentException("ChargingPoint type must be AC or DC");
        }
        this.id = id;
        this.type = type;
        this.priceModel = priceModel;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    public ChargingStatus getStatus() {
        return status;
    }

    public void setStatus(ChargingStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("status must not be null");
        }
        // Do not allow setting OUT_OF_ORDER while occupied
        if (this.status == ChargingStatus.OCCUPIED && status == ChargingStatus.OUT_OF_ORDER) {
            throw new IllegalArgumentException("Cannot set OUT_OF_ORDER while charging session is active");
        }
        this.status = status;
    }

    public Boolean isOccupied() {
        return this.status == ChargingStatus.OCCUPIED;
    }

    public List<ChargingSession> getPastChargingSessions() {
        return pastChargingSessions;
    }

    public ChargingSession.Builder getCurrentChargingSession() {
        return currentChargingSession;
    }

    public void startChargingSession(LocalDateTime startTime, EvDriver evDriver) {
        if (this.status == ChargingStatus.OUT_OF_ORDER) {
            throw new IllegalArgumentException("ChargingPoint is OUT_OF_ORDER");
        }
        if (this.currentChargingSession != null || this.status == ChargingStatus.OCCUPIED) {
            throw new IllegalArgumentException("ChargingSession already ongoing.");
        }
        if (evDriver == null) {
            throw new IllegalArgumentException("evDriver must not be null");
        }
        if (evDriver.getAccount().getBalance() <= 0) {
            throw new IllegalArgumentException("User must top up before charging");
        }

        // Snapshot prices at the start of the session (requirement)
        double pricePerKwhAtStart = this.priceModel.getPricePerKwh(this.type);
        double pricePerMinuteAtStart = this.priceModel.getPricePerMinute(this.type);

        this.currentChargingSession = new ChargingSession.Builder()
                .id(UUID.randomUUID().toString())
                .locationId(this.location.getId())
                .locationName(this.location.getName())
                .chargingPointId(this.id)
                .startTime(startTime)
                .chargingType(this.type)
                .pricePerKwhAtStart(pricePerKwhAtStart)
                .pricePerMinuteAtStart(pricePerMinuteAtStart)
                .evDriver(evDriver);

        this.status = ChargingStatus.OCCUPIED;
    }

    public ChargingSession stopChargingSession(LocalDateTime endTime, Double loadedEnergyKwh) {
        if (this.currentChargingSession == null) {
            throw new IllegalArgumentException("No current charging session.");
        }
        if (loadedEnergyKwh == null || loadedEnergyKwh < 0) {
            throw new IllegalArgumentException("loadedEnergyKwh must be >= 0");
        }

        final ChargingSession finishedChargingSession = this.currentChargingSession
                .endTime(endTime)
                .loadedEnergyKwh(loadedEnergyKwh)
                .buildAndCalculate();

        this.pastChargingSessions.add(finishedChargingSession);
        finishedChargingSession.getEvDriver().getAccount().deduct(finishedChargingSession.getTotalCost());
        finishedChargingSession.getEvDriver().getChargingSessions().add(finishedChargingSession);

        this.currentChargingSession = null;
        this.status = ChargingStatus.FREE;
        return finishedChargingSession;
    }

    @Override
    public String toString() {
        return "ChargingPoint{" + "\n" +
                "\tid='" + id + '\'' + "\n" +
                "\ttype='" + type + '\'' + "\n" +
                "\tstatus=" + status + "\n" +
                "\tpastChargingSessions=" + pastChargingSessions + "\n" +
                "\tcurrentChargingSession=" + currentChargingSession + "\n" +
                "\tpriceModel=" + priceModel + "\n" +
                '}';
    }
}
