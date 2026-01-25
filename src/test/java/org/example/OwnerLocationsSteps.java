package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OwnerLocationsSteps {

    private final TestWorld w = Hooks.world();

    @Given("there is no location with id {string}")
    public void thereIsNoLocationWithId(String id) {
        assertThat(w.locationManager.exists(id)).isFalse();
    }

    @When("I create a location with id {string} and name {string}")
    public void iCreateALocationWithIdAndName(String id, String name) {
        try {
            w.locationManager.createLocation(id, name);
            w.lastException = null;
        } catch (Exception e) {
            w.lastException = e;
        }
    }

    @When("I create locations")
    public void iCreateLocations(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String id = row.get("id");
            String name = row.get("name");
            iCreateALocationWithIdAndName(id, name);
        }
    }

    @When("I rename location {string} to {string}")
    public void iRenameLocationTo(String id, String newName) {
        try {
            w.locationManager.updateLocationName(id, newName);
            w.lastException = null;
        } catch (Exception e) {
            w.lastException = e;
        }
    }

    @When("I delete location {string}")
    public void iDeleteLocation(String id) {
        try {
            w.locationManager.deleteLocation(id);
            w.lastException = null;
        } catch (Exception e) {
            w.lastException = e;
        }
    }

    @Then("the location list should contain a location with id {string}")
    public void theLocationListShouldContainALocationWithId(String id) {
        assertThat(w.locationManager.exists(id)).isTrue();
    }

    @Then("location {string} should have name {string}")
    public void locationShouldHaveName(String id, String expectedName) {
        assertThat(w.locationManager.getLocation(id)).isNotNull();
        assertThat(w.locationManager.getLocation(id).getName()).isEqualTo(expectedName);
    }

    @Then("no location with id {string} should exist")
    public void noLocationWithIdShouldExist(String id) {
        assertThat(w.locationManager.exists(id)).isFalse();
    }

    @Then("an error should occur")
    public void anErrorShouldOccur() {
        assertThat(w.lastException).isNotNull();
    }
}
