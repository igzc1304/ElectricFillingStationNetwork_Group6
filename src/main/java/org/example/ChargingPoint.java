package org.example;

public class ChargingPoint {

    private final String id;
    private final String type;   // z.B. "AC" oder "DC"

    public ChargingPoint(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
