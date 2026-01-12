package org.example;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LocationManager {

    private final Map<String, Location> locations = new HashMap<>();

    public boolean exists(String id) {
        return locations.containsKey(id);
    }

    public void createLocation(String id, String name) {
        if (locations.containsKey(id)) {
            throw new IllegalArgumentException("Location with id " + id + " already exists");
        }
        locations.put(id, new Location(id, name));
    }

    public void addChargingPoint(String locationId, String chargingPointId, String type) {
        Location location = locations.get(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Location " + locationId + " does not exist");
        }
        location.addChargingPoint(chargingPointId, type);
    }


    public Location getLocation(String id) {
        return locations.get(id);
    }

    public Collection<Location> getAllLocations() {
        return locations.values();
    }
}
