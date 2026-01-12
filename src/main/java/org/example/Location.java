package org.example;

import java.util.ArrayList;
import java.util.List;

public class Location {

    private final String id;
    private final String name;
    private final List<ChargingPoint> chargingPoints = new ArrayList<>();
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
    public List<ChargingPoint> getChargingPoints() {
        return chargingPoints;
    }

    public void addChargingPoint(String chargingPointId, String type) {
        chargingPoints.add(new ChargingPoint(chargingPointId, type, this.priceModel));
    }
}
