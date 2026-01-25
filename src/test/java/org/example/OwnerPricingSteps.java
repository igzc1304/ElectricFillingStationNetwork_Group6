package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class OwnerPricingSteps {

    private final TestWorld w = Hooks.world();

    @Given("the location {string} exists")
    public void theLocationExists(String locationId) {
        assertThat(w.locationManager.getLocation(locationId)).isNotNull();
    }

    @When("I set the {string} rate for location {string} to {double} per kWh")
    public void iSetTheRateForLocationToPerKWh(String type, String locationId, Double pricePerKwh) {
        try {
            w.locationManager.getLocation(locationId).getPriceModel().setPricePerKwh(type, pricePerKwh);
            w.lastException = null;
        } catch (Exception e) {
            w.lastException = e;
        }
    }

    @When("I set the {string} time rate for location {string} to {double} per minute")
    public void iSetTheTimeRateForLocationToPerMinute(String type, String locationId, Double pricePerMinute) {
        try {
            w.locationManager.getLocation(locationId).getPriceModel().setPricePerMinute(type, pricePerMinute);
            w.lastException = null;
        } catch (Exception e) {
            w.lastException = e;
        }
    }

    @Then("the {string} rate for location {string} should be {double} per kWh")
    public void theRateForLocationShouldBePerKWh(String type, String locationId, double pricePerKwh) {
        assertThat(w.locationManager.getLocation(locationId).getPriceModel().getPricePerKwh(type)).isEqualTo(pricePerKwh);
    }

    @Then("the {string} time rate for location {string} should be {double} per minute")
    public void theTimeRateForLocationShouldBePerMinute(String type, String locationId, double pricePerMinute) {
        assertThat(w.locationManager.getLocation(locationId).getPriceModel().getPricePerMinute(type)).isEqualTo(pricePerMinute);
    }
}
