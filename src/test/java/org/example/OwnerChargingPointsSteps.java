package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OwnerChargingPointsSteps {

    private final TestWorld w = Hooks.world();

    @Given("a location with id {string} exists")
    public void aLocationWithIdExists(String id) {
        if (!w.locationManager.exists(id)) {
            w.locationManager.createLocation(id, id);
        }
        assertThat(w.locationManager.exists(id)).isTrue();
    }

    @And("the location has no charging points")
    public void theLocationHasNoChargingPoints() {
        // noop
    }

    @When("I add a charging point with id {string} of type {string} to location {string}")
    public void iAddAChargingPointWithIdOfTypeToLocation(String cpId, String type, String locId) {
        try {
            w.locationManager.addChargingPoint(locId, cpId, type);
            w.lastException = null;
        } catch (Exception e) {
            w.lastException = e;
        }
    }

    @When("I add charging points to location {string}")
    public void iAddChargingPointsToLocation(String locId, DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            iAddAChargingPointWithIdOfTypeToLocation(row.get("id"), row.get("type"), locId);
        }
    }

    @When("I remove charging point {string} from location {string}")
    public void iRemoveChargingPointFromLocation(String cpId, String locId) {
        try {
            w.locationManager.removeChargingPoint(locId, cpId);
            w.lastException = null;
        } catch (Exception e) {
            w.lastException = e;
        }
    }

    @When("I set charging point {string} at location {string} to status {string}")
    public void iSetChargingPointAtLocationToStatus(String cpId, String locId, String status) {
        try {
            w.locationManager.setChargingPointStatus(locId, cpId, ChargingStatus.valueOf(status));
            w.lastException = null;
        } catch (Exception e) {
            w.lastException = e;
        }
    }

    @Then("the location {string} should have {int} charging point")
    public void theLocationShouldHaveChargingPoint(String locId, Integer expectedCount) {
        Location location = w.locationManager.getLocation(locId);
        assertThat(location).isNotNull();
        assertThat(location.getChargingPoints().size()).isEqualTo(expectedCount);
    }

    @Then("charging point {string} at location {string} should have status {string}")
    public void chargingPointAtLocationShouldHaveStatus(String cpId, String locId, String status) {
        ChargingPoint cp = w.locationManager.getChargingPoint(locId, cpId);
        assertThat(cp).isNotNull();
        assertThat(cp.getStatus()).isEqualTo(ChargingStatus.valueOf(status));
    }
}
