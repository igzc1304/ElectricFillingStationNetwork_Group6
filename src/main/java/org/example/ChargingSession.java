package org.example;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class ChargingSession {
    private final String id;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Double loadedEnergyKwh;
    private final Double totalCost;
    private final String chargingType;
    private final EvDriver evDriver;
    private final Invoice invoice;

    ChargingSession(String id, LocalDateTime startTime, LocalDateTime endTime, Double loadedEnergyKwh, Double totalCost, String chargingType, EvDriver evDriver) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.loadedEnergyKwh = loadedEnergyKwh;
        this.totalCost = totalCost;
        this.chargingType = chargingType;
        this.evDriver = evDriver;
        this.invoice = new Invoice(UUID.randomUUID().toString(), endTime, totalCost, evDriver);
        this.evDriver.getInvoices().add(this.invoice);
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public String getChargingType() {
        return chargingType;
    }

    public Duration getDurationMs() {
        return Duration.between(this.startTime, this.endTime);
    }

    public String getId() {
        return id;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public Double getLoadedEnergyKwh() {
        return loadedEnergyKwh;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
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
        private Double totalCost;
        private String chargingType;
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

        public Builder totalCost(Double totalCost) {
            this.totalCost = totalCost;
            return this;
        }

        public Builder chargingType(String chargingType) {
            this.chargingType = chargingType;
            return this;
        }

        public Builder evDriver(EvDriver evDriver) {
            this.evDriver = evDriver;
            return this;
        }

        public ChargingSession build() {
            Objects.requireNonNull(id, "id must not be null");
            Objects.requireNonNull(startTime, "startTime must not be null");
            Objects.requireNonNull(endTime, "endTime must not be null");
            Objects.requireNonNull(loadedEnergyKwh, "loadedEnergyKwh must not be null");
            Objects.requireNonNull(chargingType, "chargingType must not be null");
            Objects.requireNonNull(evDriver, "evDriver must not be null");

            return new ChargingSession(id, startTime, endTime, loadedEnergyKwh, totalCost, chargingType, evDriver);
        }
    }

    @Override
    public String toString() {
        return "ChargingSession{" +
                "\tid='" + id + '\'' + "\n" +
                "\tstartTime=" + startTime + "\n" +
                "\tendTime=" + endTime + "\n" +
                "\tloadedEnergyKwh=" + loadedEnergyKwh + "\n" +
                "\ttotalCost=" + totalCost + "\n" +
                "\tchargingType='" + chargingType + '\'' + "\n" +
                "\tevDriver=" + evDriver + "\n" +
                "\tinvoice=" + invoice + "\n" +
                '}';
    }
}