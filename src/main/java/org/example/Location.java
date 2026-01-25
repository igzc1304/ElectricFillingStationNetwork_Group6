package org.example;

import java.util.*;

public class Location {

    private final String id;
    private String name;
    private final Map<String, ChargingPoint> chargingPoints = new LinkedHashMap<>();
    private final PriceModel priceModel;

    public Location(String id, String name) {
        this.id = id;
        this.name = name;
        this.priceModel = new PriceModel(id, (double) 0, (double) 0);
    }

    public PriceModel getPriceModel() {
        return priceModel;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Location name must not be blank");
        }
        this.name = name;
    }

    public List<ChargingPoint> getChargingPoints() {
        return new ArrayList<>(chargingPoints.values());
    }

    public void addChargingPoint(String chargingPointId, String type) {
        if (chargingPoints.containsKey(chargingPointId)) {
            throw new IllegalArgumentException("ChargingPoint with id " + chargingPointId + " already exists at location " + id);
        }
        chargingPoints.put(chargingPointId, new ChargingPoint(chargingPointId, type, this.priceModel, this));
    }

    public ChargingPoint getChargingPoint(String chargingPointId) {
        return chargingPoints.get(chargingPointId);
    }

    public void removeChargingPoint(String chargingPointId) {
        if (!chargingPoints.containsKey(chargingPointId)) {
            throw new IllegalArgumentException("ChargingPoint " + chargingPointId + " does not exist at location " + id);
        }
        chargingPoints.remove(chargingPointId);
    }

    @Override
    public String toString() {
        return "Location{" + "\n" +
                "\tid='" + id + '\'' + "\n" +
                "\tname='" + name + '\'' + "\n" +
                "\tchargingPoints=" + chargingPoints.values() + "\n" +
                "\tpriceModel=" + priceModel + "\n" +
                '}';
    }
}
