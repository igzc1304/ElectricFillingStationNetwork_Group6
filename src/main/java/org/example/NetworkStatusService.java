package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Creates the required "network status" list: for each location the current prices
 * and the operating status of all charging points.
 */
public class NetworkStatusService {

    public List<NetworkStatusEntry> getNetworkStatus(LocationManager locationManager) {
        List<NetworkStatusEntry> entries = new ArrayList<>();
        for (Location location : locationManager.getAllLocations()) {
            for (ChargingPoint cp : location.getChargingPoints()) {
                entries.add(new NetworkStatusEntry(
                        location.getId(),
                        location.getName(),
                        cp.getId(),
                        cp.getType(),
                        cp.getStatus(),
                        location.getPriceModel().getPricePerKwh(cp.getType()),
                        location.getPriceModel().getPricePerMinute(cp.getType())
                ));
            }
        }
        entries.sort(Comparator
                .comparing(NetworkStatusEntry::locationName)
                .thenComparing(NetworkStatusEntry::chargingPointId));
        return entries;
    }

    public record NetworkStatusEntry(
            String locationId,
            String locationName,
            String chargingPointId,
            String chargingType,
            ChargingStatus status,
            double pricePerKwh,
            double pricePerMinute
    ) {}
}
