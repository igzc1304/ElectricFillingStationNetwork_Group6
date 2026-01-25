package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Builds the required billing / invoice status view for a client.
 *
 * Requirement highlights:
 * - invoice list sorted by start time
 * - item number, location name, charging point, mode, duration, energy, price
 * - include top-ups and outstanding balance
 */
public class BillingService {

    public InvoiceStatus getInvoiceStatus(EvDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("driver must not be null");
        }

        List<InvoiceItem> items = new ArrayList<>();
        driver.getChargingSessions().stream()
                .sorted(Comparator.comparing(ChargingSession::getStartTime))
                .forEach(cs -> items.add(new InvoiceItem(
                        items.size() + 1,
                        cs.getStartTime(),
                        cs.getLocationName(),
                        cs.getChargingPointId(),
                        cs.getChargingType(),
                        cs.getDurationMinutes(),
                        cs.getLoadedEnergyKwh(),
                        cs.getTotalCost()
                )));

        List<TopUpEntry> topUps = driver.getTopUps().stream()
                .sorted(Comparator.comparing(TopUpTransaction::getTimestamp))
                .map(t -> new TopUpEntry(t.getTimestamp(), t.getAmount()))
                .toList();

        return new InvoiceStatus(items, topUps, driver.getAccount().getBalance());
    }

    public record InvoiceStatus(
            List<InvoiceItem> items,
            List<TopUpEntry> topUps,
            double outstandingBalance
    ) {}

    public record InvoiceItem(
            int itemNumber,
            LocalDateTime startTime,
            String locationName,
            String chargingPointId,
            String chargingMode,
            long durationMinutes,
            double loadedEnergyKwh,
            double price
    ) {}

    public record TopUpEntry(LocalDateTime timestamp, double amount) {}
}
