package org.example;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LocationManager {

    private final Map<String, Location> locations = new HashMap<>();

    public boolean exists(String id) {
        return locations.containsKey(id);
    }

    // --- Location CRUD ---

    public void createLocation(String id, String name) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Location id must not be blank");
        }
        if (locations.containsKey(id)) {
            throw new IllegalArgumentException("Location with id " + id + " already exists");
        }
        locations.put(id, new Location(id, name));
    }

    public void updateLocationName(String id, String newName) {
        Location location = locations.get(id);
        if (location == null) {
            throw new IllegalArgumentException("Location " + id + " does not exist");
        }
        location.setName(newName);
    }

    public void deleteLocation(String id) {
        if (!locations.containsKey(id)) {
            throw new IllegalArgumentException("Location " + id + " does not exist");
        }
        locations.remove(id);
    }

    public Location getLocation(String id) {
        return locations.get(id);
    }

    public Collection<Location> getAllLocations() {
        return locations.values();
    }

    // --- ChargingPoint CRUD & status ---

    public void addChargingPoint(String locationId, String chargingPointId, String type) {
        Location location = requireLocation(locationId);
        location.addChargingPoint(chargingPointId, type);
    }

    public void removeChargingPoint(String locationId, String chargingPointId) {
        Location location = requireLocation(locationId);
        location.removeChargingPoint(chargingPointId);
    }

    public ChargingPoint getChargingPoint(String locationId, String chargingPointId) {
        Location location = requireLocation(locationId);
        return location.getChargingPoint(chargingPointId);
    }

    public void setChargingPointStatus(String locationId, String chargingPointId, ChargingStatus status) {
        ChargingPoint cp = requireChargingPoint(locationId, chargingPointId);
        cp.setStatus(status);
    }

    private Location requireLocation(String id) {
        Location location = locations.get(id);
        if (location == null) {
            throw new IllegalArgumentException("Location " + id + " does not exist");
        }
        return location;
    }

    private ChargingPoint requireChargingPoint(String locationId, String chargingPointId) {
        Location location = requireLocation(locationId);
        ChargingPoint cp = location.getChargingPoint(chargingPointId);
        if (cp == null) {
            throw new IllegalArgumentException("ChargingPoint " + chargingPointId + " does not exist at location " + locationId);
        }
        return cp;
    }
}
