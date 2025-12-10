package org.example;
import java.util.ArrayList;
import java.util.List;

public class Location {

    private final String id;
    private final String name;
    private final List<ChargingPoint> chargingPoints = new ArrayList<>();

    public Location(String id, String name) {
        this.id = id;
        this.name = name;
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

    public void addChargingPoint(ChargingPoint chargingPoint) {
        chargingPoints.add(chargingPoint);
    }
}
