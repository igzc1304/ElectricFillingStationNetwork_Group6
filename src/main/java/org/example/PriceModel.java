package org.example;

import java.util.Objects;

public class PriceModel {
    private final String id;
    private Double pricePerKwhAC;
    private Double pricePerKwhDC;

    public PriceModel(String id, Double pricePerKwhAC, Double pricePerKwhDC) {
        this.id = id;
        this.pricePerKwhAC = pricePerKwhAC;
        this.pricePerKwhDC = pricePerKwhDC;
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

    public void setPricePerKwh(String chargingType, Double pricePerKwh) {
        switch (chargingType) {
            case "AC" -> this.pricePerKwhAC = pricePerKwh;
            case "DC" -> this.pricePerKwhDC = pricePerKwh;
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
