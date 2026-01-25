package org.example;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * One completed charging session. Price is calculated with the prices that were
 * valid at the start of the session (snapshot).
 */
public class ChargingSession {
    private final String id;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Double loadedEnergyKwh;
    private final Long durationMinutes;
    private final Double pricePerKwhAtStart;
    private final Double pricePerMinuteAtStart;
    private final Double totalCost;
    private final String chargingType;
    private final String locationId;
    private final String locationName;
    private final String chargingPointId;
    private final EvDriver evDriver;

    ChargingSession(
            String id,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Double loadedEnergyKwh,
            Long durationMinutes,
            Double pricePerKwhAtStart,
            Double pricePerMinuteAtStart,
            Double totalCost,
            String chargingType,
            String locationId,
            String locationName,
            String chargingPointId,
            EvDriver evDriver
    ) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.loadedEnergyKwh = loadedEnergyKwh;
        this.durationMinutes = durationMinutes;
        this.pricePerKwhAtStart = pricePerKwhAtStart;
        this.pricePerMinuteAtStart = pricePerMinuteAtStart;
        this.totalCost = totalCost;
        this.chargingType = chargingType;
        this.locationId = locationId;
        this.locationName = locationName;
        this.chargingPointId = chargingPointId;
        this.evDriver = evDriver;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Double getLoadedEnergyKwh() {
        return loadedEnergyKwh;
    }

    public Long getDurationMinutes() {
        return durationMinutes;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public String getChargingType() {
        return chargingType;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getChargingPointId() {
        return chargingPointId;
    }

    public Double getPricePerKwhAtStart() {
        return pricePerKwhAtStart;
    }

    public Double getPricePerMinuteAtStart() {
        return pricePerMinuteAtStart;
    }

    public EvDriver getEvDriver() {
        return evDriver;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChargingSession that = (ChargingSession) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public static final class Builder {
        private String id;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Double loadedEnergyKwh;
        private String chargingType;
        private String locationId;
        private String locationName;
        private String chargingPointId;
        private Double pricePerKwhAtStart;
        private Double pricePerMinuteAtStart;
        private EvDriver evDriver;

        public Builder() {}

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder loadedEnergyKwh(Double loadedEnergyKwh) {
            this.loadedEnergyKwh = loadedEnergyKwh;
            return this;
        }

        public Builder chargingType(String chargingType) {
            this.chargingType = chargingType;
            return this;
        }

        public Builder locationId(String locationId) {
            this.locationId = locationId;
            return this;
        }

        public Builder locationName(String locationName) {
            this.locationName = locationName;
            return this;
        }

        public Builder chargingPointId(String chargingPointId) {
            this.chargingPointId = chargingPointId;
            return this;
        }

        public Builder pricePerKwhAtStart(Double pricePerKwhAtStart) {
            this.pricePerKwhAtStart = pricePerKwhAtStart;
            return this;
        }

        public Builder pricePerMinuteAtStart(Double pricePerMinuteAtStart) {
            this.pricePerMinuteAtStart = pricePerMinuteAtStart;
            return this;
        }

        public Builder evDriver(EvDriver evDriver) {
            this.evDriver = evDriver;
            return this;
        }

        /**
         * Build a ChargingSession and calculate cost from the snapshot prices.
         */
        public ChargingSession buildAndCalculate() {
            Objects.requireNonNull(id, "id must not be null");
            Objects.requireNonNull(startTime, "startTime must not be null");
            Objects.requireNonNull(endTime, "endTime must not be null");
            Objects.requireNonNull(loadedEnergyKwh, "loadedEnergyKwh must not be null");
            Objects.requireNonNull(chargingType, "chargingType must not be null");
            Objects.requireNonNull(locationId, "locationId must not be null");
            Objects.requireNonNull(locationName, "locationName must not be null");
            Objects.requireNonNull(chargingPointId, "chargingPointId must not be null");
            Objects.requireNonNull(pricePerKwhAtStart, "pricePerKwhAtStart must not be null");
            Objects.requireNonNull(pricePerMinuteAtStart, "pricePerMinuteAtStart must not be null");
            Objects.requireNonNull(evDriver, "evDriver must not be null");

            long minutes = Duration.between(startTime, endTime).toMinutes();
            if (minutes < 0) {
                throw new IllegalArgumentException("endTime must be after startTime");
            }
            // If session is extremely short, bill at least 0 minutes (business rule could be 1;
            // we keep 0 to avoid surprising costs in tests).
            double totalCost = (pricePerKwhAtStart * loadedEnergyKwh) + (pricePerMinuteAtStart * minutes);

            return new ChargingSession(
                    id,
                    startTime,
                    endTime,
                    loadedEnergyKwh,
                    minutes,
                    pricePerKwhAtStart,
                    pricePerMinuteAtStart,
                    totalCost,
                    chargingType,
                    locationId,
                    locationName,
                    chargingPointId,
                    evDriver
            );
        }
    }

    @Override
    public String toString() {
        return "ChargingSession{" +
                "\n\tid='" + id + '\'' +
                "\n\tstartTime=" + startTime +
                "\n\tendTime=" + endTime +
                "\n\tloadedEnergyKwh=" + loadedEnergyKwh +
                "\n\tdurationMinutes=" + durationMinutes +
                "\n\tpricePerKwhAtStart=" + pricePerKwhAtStart +
                "\n\tpricePerMinuteAtStart=" + pricePerMinuteAtStart +
                "\n\ttotalCost=" + totalCost +
                "\n\tchargingType='" + chargingType + '\'' +
                "\n\tlocationName='" + locationName + '\'' +
                "\n\tchargingPointId='" + chargingPointId + '\'' +
                "\n}";
    }
}
