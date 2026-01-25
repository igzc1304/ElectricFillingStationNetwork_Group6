package org.example;

import java.util.Objects;

public class PriceModel {
    private final String id;
    private Double pricePerKwhAC;
    private Double pricePerKwhDC;
    private Double pricePerMinuteAC;
    private Double pricePerMinuteDC;

    public PriceModel(String id, Double pricePerKwhAC, Double pricePerKwhDC) {
        this.id = id;
        this.pricePerKwhAC = pricePerKwhAC;
        this.pricePerKwhDC = pricePerKwhDC;
        this.pricePerMinuteAC = 0.0;
        this.pricePerMinuteDC = 0.0;
    }

    public Double getPricePerKwh(String chargingType) {
        return switch (chargingType) {
            case "AC" -> this.pricePerKwhAC;
            case "DC" -> this.pricePerKwhDC;
            default -> throw new IllegalStateException("Unexpected value: " + chargingType);
        };
    }

    public Double calculatePrice(Double kwh, String chargingType) {
        return this.getPricePerKwh(chargingType) * kwh;
    }

    public Double getPricePerMinute(String chargingType) {
        return switch (chargingType) {
            case "AC" -> this.pricePerMinuteAC;
            case "DC" -> this.pricePerMinuteDC;
            default -> throw new IllegalStateException("Unexpected value: " + chargingType);
        };
    }

    /**
     * Total price based on kWh and minutes.
     */
    public Double calculatePrice(Double kwh, long minutes, String chargingType) {
        return (this.getPricePerKwh(chargingType) * kwh) + (this.getPricePerMinute(chargingType) * minutes);
    }

    public void setPricePerKwh(String chargingType, Double pricePerKwh) {
        switch (chargingType) {
            case "AC" -> this.pricePerKwhAC = pricePerKwh;
            case "DC" -> this.pricePerKwhDC = pricePerKwh;
            default -> throw new IllegalStateException("Unexpected value: " + chargingType);
        }
    }

    public void setPricePerMinute(String chargingType, Double pricePerMinute) {
        if (pricePerMinute == null || pricePerMinute < 0) {
            throw new IllegalArgumentException("Price per minute must be >= 0");
        }
        switch (chargingType) {
            case "AC" -> this.pricePerMinuteAC = pricePerMinute;
            case "DC" -> this.pricePerMinuteDC = pricePerMinute;
            default -> throw new IllegalStateException("Unexpected value: " + chargingType);
        }
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "PriceModel{" + "\n" +
                "\tid='" + id + '\'' + "\n" +
                "\tpricePerKwhAC=" + pricePerKwhAC + "\n" +
                "\tpricePerKwhDC=" + pricePerKwhDC + "\n" +
                "\tpricePerMinuteAC=" + pricePerMinuteAC + "\n" +
                "\tpricePerMinuteDC=" + pricePerMinuteDC + "\n" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PriceModel that = (PriceModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
