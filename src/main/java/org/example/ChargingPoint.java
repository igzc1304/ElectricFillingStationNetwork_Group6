package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChargingPoint {

    private final String id;
    private final String type;   // z.B. "AC" oder "DC"
    private final List<ChargingSession> pastChargingSessions = new ArrayList<>();
    private ChargingSession.Builder currentChargingSession;
    private final PriceModel priceModel;

    public ChargingPoint(String id, String type, PriceModel priceModel) {
        this.id = id;
        this.type = type;
        this.priceModel = priceModel;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Boolean isOccupied() {
        return this.currentChargingSession != null;
    }

    public List<ChargingSession> getPastChargingSessions() {
        return pastChargingSessions;
    }

    public ChargingSession.Builder getCurrentChargingSession() {
        return currentChargingSession;
    }

    public void startChargingSession(LocalDateTime startTime, EvDriver evDriver) {
        if (this.currentChargingSession != null) {
            throw new IllegalArgumentException("ChargingSession already ongoing.");
        }
        this.currentChargingSession = new ChargingSession.Builder().id(UUID.randomUUID().toString())
                .startTime(startTime)
                .chargingType(this.type)
                .evDriver(evDriver);
    }

    public ChargingSession stopChargingSession(LocalDateTime endTime, Double loadedEnergyKwh) {
        if(this.currentChargingSession == null) {
            throw new IllegalArgumentException("No current charging session.");
        }

        final ChargingSession finishedChargingSession = this.currentChargingSession
                .endTime(endTime)
                .loadedEnergyKwh(loadedEnergyKwh)
                .totalCost(this.priceModel.calculatePrice(loadedEnergyKwh, this.type))
                .build();
        this.pastChargingSessions.add(finishedChargingSession);
        finishedChargingSession.getEvDriver().getAccount().deduct(finishedChargingSession.getTotalCost());
        this.currentChargingSession = null;
        return finishedChargingSession;
    }

    @Override
    public String toString() {
        return "ChargingPoint{" + "\n" +
                "\tid='" + id + '\'' + "\n" +
                "\ttype='" + type + '\'' + "\n" +
                "\tpastChargingSessions=" + pastChargingSessions + "\n" +
                "\tcurrentChargingSession=" + currentChargingSession + "\n" +
                "\tpriceModel=" + priceModel + "\n" +
                '}';
    }
}
