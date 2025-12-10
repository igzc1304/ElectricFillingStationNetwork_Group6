package org.example;

import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.*;
import static org.assertj.core.api.Assertions.assertThat;

public class StepDefinitions {

    private LocationManager locationManager;
    private ClientManager clientManager;

    @Before
    public void setUp() {
        locationManager = new LocationManager();
        clientManager = new ClientManager();
    }

    // --- Szenario 1: Location erstellen ---

    @Given("there is no location with id {string}")
    public void thereIsNoLocationWithId(String id) {
        assertThat(locationManager.exists(id)).isFalse();
    }

    @When("I create a location with id {string} and name {string}")
    public void iCreateALocationWithIdAndName(String id, String name) {
        locationManager.createLocation(id, name);
    }

    @Then("the location list should contain a location with id {string}")
    public void theLocationListShouldContainALocationWithId(String id) {
        assertThat(locationManager.exists(id)).isTrue();
    }

    // --- Szenario 2: Charger hinzufügen ---

    @Given("a location with id {string} exists")
    public void aLocationWithIdExists(String id) {
        if (!locationManager.exists(id)) {
            locationManager.createLocation(id, "Dummy");
        }
        assertThat(locationManager.exists(id)).isTrue();
    }

    @Given("the location has no charging points")
    public void theLocationHasNoChargingPoints() {
        // nichts extra nötig, neue Location hat leere Liste
    }

    @When("I add a charging point with id {string} of type {string} to location {string}")
    public void iAddAChargingPointWithIdOfTypeToLocation(String cpId, String type, String locId) {
        locationManager.addChargingPoint(locId, cpId, type);
    }

    @Then("the location {string} should have {int} charging point")
    public void theLocationShouldHaveChargingPoint(String locId, Integer expectedCount) {
        Location location = locationManager.getLocation(locId);
        assertThat(location.getChargingPoints().size()).isEqualTo(expectedCount);
    }

    // --- Szenario 3: Client registrieren ---

    @Given("there is no client with id {string}")
    public void thereIsNoClientWithId(String clientId) {
        assertThat(clientManager.existsClient(clientId)).isFalse();
    }

    @When("I register a client with id {string} and name {string} and email {string}")
    public void iRegisterAClientWithIdAndNameAndEmail(String id, String name, String email) {
        clientManager.registerClient(id, name, email);
    }

    @Then("the system should contain a client with id {string}")
    public void theSystemShouldContainAClientWithId(String id) {
        assertThat(clientManager.existsClient(id)).isTrue();
        EvDriver client = clientManager.getClient(id);
        assertThat(client).isNotNull();
        assertThat(client.getId()).isEqualTo(id);
    }
}
