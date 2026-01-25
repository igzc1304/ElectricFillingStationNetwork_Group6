package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class UserChargingSteps {

    private final TestWorld w = Hooks.world();

    @Given("there is an EV driver with id {string}")
    public void thereIsAnEvDriverWithId(String id) {
        assertThat(w.clientManager.getClient(id)).isNotNull();
    }

    @And("the balance of {string} is at least {double}")
    public void theBalanceOfIsAtLeast(String userId, Double amount) {
        assertThat(w.clientManager.getClient(userId).getAccount().getBalance()).isGreaterThanOrEqualTo(amount);
    }

    @And("charging point {string} at location {string} has kWh rate {double}")
    public void chargingPointAtLocationHasKwhRate(String cpId, String locId, Double rate) {
        ChargingPoint cp = w.locationManager.getChargingPoint(locId, cpId);
        assertThat(w.locationManager.getLocation(locId).getPriceModel().getPricePerKwh(cp.getType())).isEqualTo(rate);
    }

    @And("charging point {string} at location {string} has status {string}")
    public void chargingPointAtLocationHasStatus(String cpId, String locId, String status) {
        ChargingPoint cp = w.locationManager.getChargingPoint(locId, cpId);
        assertThat(cp.getStatus()).isEqualTo(ChargingStatus.valueOf(status));
    }

    @When("user {string} starts charging at charging point {string} at location {string} at {string}")
    public void userStartsChargingAtChargingPointAtLocationAt(String userId, String cpId, String locId, String startIso) {
        try {
            ChargingPoint cp = w.locationManager.getChargingPoint(locId, cpId);
            cp.startChargingSession(LocalDateTime.parse(startIso), w.clientManager.getClient(userId));
            w.lastException = null;
        } catch (Exception e) {
            w.lastException = e;
        }
    }

    @Then("a charging session should start for charging point {string} at location {string}")
    public void aChargingSessionShouldStartForChargingPointAtLocation(String cpId, String locId) {
        ChargingPoint cp = w.locationManager.getChargingPoint(locId, cpId);
        assertThat(cp.getCurrentChargingSession()).isNotNull();
    }

    @And("charging point {string} at location {string} should become {string}")
    public void chargingPointAtLocationShouldBecome(String cpId, String locId, String status) {
        ChargingPoint cp = w.locationManager.getChargingPoint(locId, cpId);
        assertThat(cp.getStatus()).isEqualTo(ChargingStatus.valueOf(status));
    }

    @Given("an active charging session exists for charging point {string} at location {string}")
    public void anActiveChargingSessionExistsForChargingPointAtLocation(String cpId, String locId) {
        ChargingPoint cp = w.locationManager.getChargingPoint(locId, cpId);
        assertThat(cp.getCurrentChargingSession()).isNotNull();
    }

    @When("the charging session for charging point {string} at location {string} is stopped at {string} after {double} kWh")
    public void theChargingSessionForChargingPointAtLocationIsStoppedAtAfterKwh(String cpId, String locId, String endIso, Double loadedKwh) {
        try {
            ChargingPoint cp = w.locationManager.getChargingPoint(locId, cpId);
            w.lastChargingSession = cp.stopChargingSession(LocalDateTime.parse(endIso), loadedKwh);
            w.lastException = null;
        } catch (Exception e) {
            w.lastException = e;
        }
    }

    @Then("user {string} balance should be {double}")
    public void userBalanceShouldBe(String userId, Double expected) {
        // Monetary values are stored as double in this project; compare with a tiny tolerance
        // to avoid floating point representation artifacts (e.g., 3.9000000000000004).
        assertThat(w.clientManager.getClient(userId).getAccount().getBalance())
                .isCloseTo(expected, within(1e-9));
    }

    @Then("last session cost should be {double}")
    public void lastSessionCostShouldBe(Double expected) {
        assertThat(w.lastChargingSession).isNotNull();
        assertThat(w.lastChargingSession.getTotalCost())
                .isCloseTo(expected, within(1e-9));
    }

    @When("user {string} tops up by {double} at {string}")
    public void userTopsUpByAt(String userId, Double amount, String timestamp) {
        new TopUpTransaction(UUID.randomUUID().toString(), amount, LocalDateTime.parse(timestamp), w.clientManager.getClient(userId));
    }
}
