package org.example;

import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

public class StepDefinitions {

    private LocationManager locationManager;
    private ClientManager clientManager;

    @Before
    public void setUp() {
        locationManager = new LocationManager();
        locationManager.createLocation("Vienna Center", "Vienna Center");
        locationManager.getLocation("Vienna Center").getPriceModel().setPricePerKwh("AC", 0.39);
        locationManager.getLocation("Vienna Center").getPriceModel().setPricePerKwh("DC", 0.35);
        locationManager.addChargingPoint("Vienna Center", "AC-001", "AC" );
        locationManager.addChargingPoint("Vienna Center", "DC-001", "DC" );
        locationManager.addChargingPoint("Vienna Center", "DC-002", "DC" );
        locationManager.createLocation("Graz South", "Graz South");
        clientManager = new ClientManager();
        this.setupCl1();
        this.setupCl2();
        this.setupCl3();
    }

    private void setupCl1() {
        clientManager.registerClient("Cl1", "Client 1", "example@example.com");
        EvDriver client = clientManager.getClient("Cl1");
        client.getAccount().topUp(100.);
    }

    private void setupCl2() {
        clientManager.registerClient("Cl2", "Client 2", "example@example.com");
        EvDriver client = clientManager.getClient("Cl2");
        client.getAccount().topUp(100.);

        final Location location = this.locationManager.getLocation("Vienna Center");
        final ChargingPoint chargingPoint = location.getChargingPoints().stream()
                .filter(cp -> "DC-001".equals(cp.getId()))
                .findFirst()
                .get();
        chargingPoint.startChargingSession(LocalDateTime.now(), client);
    }

    private void setupCl3() {
        clientManager.registerClient("Cl3", "Client 3", "example@example.com");
        EvDriver client = clientManager.getClient("Cl3");
        client.getAccount().topUp(270.);

        final Location location = this.locationManager.getLocation("Vienna Center");
        final ChargingPoint chargingPoint = location.getChargingPoints().stream()
                .filter(cp -> "DC-002".equals(cp.getId()))
                .findFirst()
                .get();
        chargingPoint.startChargingSession(LocalDateTime.now(), client);
        chargingPoint.stopChargingSession(LocalDateTime.now(), 100.00);

        chargingPoint.startChargingSession(LocalDateTime.now(), client);
        chargingPoint.stopChargingSession(LocalDateTime.now(), 100.00);
    }

    // --- Owner Perspective: Manage Locations ---

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

    // --- Owner Perspective: Manage Charging Points ---

    @Given("a location with id {string} exists")
    public void aLocationWithIdExists(String id) {
        if (!locationManager.exists(id)) {
            locationManager.createLocation(id, "Dummy");
        }
        assertThat(locationManager.exists(id)).isTrue();
    }

    @And("the location has no charging points")
    public void theLocationHasNoChargingPoints() {
        // Eine neue Location hat standardmäßig keine Charging Points -> nichts zu tun
    }

    @When("I add a charging point with id {string} of type {string} to location {string}")
    public void iAddAChargingPointWithIdOfTypeToLocation(String cpId, String type, String locId) {
        locationManager.addChargingPoint(locId, cpId, type);
    }

    @Then("the location {string} should have {int} charging point")
    public void theLocationShouldHaveChargingPoint(String locId, Integer expectedCount) {
        Location location = locationManager.getLocation(locId);
        assertThat(location).isNotNull();
        assertThat(location.getChargingPoints().size()).isEqualTo(expectedCount);
    }

    // --- User Perspective: EV Driver Account (Clients) ---

    @Given("there is no EV driver with id {string}")
    public void thereIsNoEvDriverWithId(String evDriverId) {
        assertThat(clientManager.existsClient(evDriverId)).isFalse();
    }

    @When("I register an EV driver with id {string} and name {string} and email {string}")
    public void iRegisterAnEvDriverWithIdAndNameAndEmail(String id, String name, String email) {
        clientManager.registerClient(id, name, email);
    }

    @Then("the system should contain an EV driver with id {string}")
    public void theSystemShouldContainAnEvDriverWithId(String id) {
        assertThat(clientManager.existsClient(id)).isTrue();
        EvDriver client = clientManager.getClient(id);
        assertThat(client).isNotNull();
        assertThat(client.getId()).isEqualTo(id);
    }

    @Given("the location {string} exists")
    public void theLocationExists(String locationId) {
        assertThat(this.locationManager.getLocation(locationId)).isNotNull();
    }

    @When("I set the {string} rate for location {string} to {double} per kWh")
    public void iSetTheRateForLocationToPerKWh(String type, String locationId, Double pricePerKwh) {
        final Location location = this.locationManager.getLocation(locationId);
        final PriceModel priceModel = location.getPriceModel();
        priceModel.setPricePerKwh(type, pricePerKwh);
    }

    @Then("the {string} rate for location {string} should be {double} per kWh")
    public void theRateForLocationShouldBePerKWh(String type, String locationId, double pricePerKwh) {
        final Location location = this.locationManager.getLocation(locationId);
        final PriceModel priceModel = location.getPriceModel();
        assertThat(priceModel.getPricePerKwh(type)).isEqualTo(pricePerKwh);
    }

    @And("a {string} charging station with id {string} exists at location {string}")
    public void aChargingStationWithIdExistsAtLocation(String type, String chargingPointId, String locationId) {
        final Location location = locationManager.getLocation(locationId);
        assertThat(location.getChargingPoints().stream()
                .filter(cp -> chargingPointId.equals(cp.getId()) && type.equals(cp.getType()))
                .findFirst())
                .isPresent();
    }

    @Given("there is an EV driver with id {string}")
    public void thereIsAnEVDriverWithId(String evDriverId) {
        assertThat(clientManager.existsClient(evDriverId)).isTrue();
    }

    @And("the balance of {string} is at least {double}")
    public void theBalanceOfIsAtLeast(String evDriverId, double minBalance) {
        EvDriver client = clientManager.getClient("Cl1");
        assertThat(client.getAccount().getBalance()).isGreaterThanOrEqualTo(minBalance);
    }

    @When("user {string} start charging at station {string} at location {string}")
    public void userStartChargingAtStationAtLocation(String userId, String chargingPointId, String locationId) {
        final Location location = this.locationManager.getLocation(locationId);
        final Optional<ChargingPoint> chargingPoint = location.getChargingPoints().stream()
                .filter(cp -> chargingPointId.equals(cp.getId()))
                .findFirst();
        assertThat(chargingPoint.isPresent()).isTrue();
        final EvDriver user = clientManager.getClient(userId);
        chargingPoint.get().startChargingSession(LocalDateTime.now(), user);
    }

    @Then("a charging session should start for station {string} at {string}")
    public void aChargingSessionShouldStartForStationAt(String chargingPointId, String locationId) {
        final Location location = this.locationManager.getLocation(locationId);
        final Optional<ChargingPoint> chargingPoint = location.getChargingPoints().stream()
                .filter(cp -> chargingPointId.equals(cp.getId()))
                .findFirst();
        assertThat(chargingPoint.isPresent()).isTrue();
        assertThat(chargingPoint.get().getCurrentChargingSession()).isNotNull();
    }

    @And("the station {string} in location {string} status should become occupied")
    public void theStationInLocationStatusShouldBecome(String chargingPointId, String locationId) {
        final Location location = this.locationManager.getLocation(locationId);
        final Optional<ChargingPoint> chargingPoint = location.getChargingPoints().stream()
                .filter(cp -> chargingPointId.equals(cp.getId()))
                .findFirst();
        assertThat(chargingPoint.isPresent()).isTrue();
        assertThat(chargingPoint.get().isOccupied()).isTrue();
    }

    @Given("an active charging session exists for station {string} at {string}")
    public void anActiveChargingSessionExistsForStationAt(String chargingPointId, String locationId) {
        final Location location = this.locationManager.getLocation(locationId);
        final Optional<ChargingPoint> chargingPoint = location.getChargingPoints().stream()
                .filter(cp -> chargingPointId.equals(cp.getId()))
                .findFirst();
        assertThat(chargingPoint.isPresent()).isTrue();
        final ChargingSession.Builder cs = chargingPoint.get().getCurrentChargingSession();
        assertThat(chargingPoint.get().getCurrentChargingSession()).isNotNull();
    }

    @When("the charging session for station {string} at {string} is stopped after {double} KWh")
    public void theChargingSessionForStationAtIsStoppedAfterKWh(String chargingPointId, String locationId, Double loadedKwh) {
        final Location location = this.locationManager.getLocation(locationId);
        final Optional<ChargingPoint> chargingPoint = location.getChargingPoints().stream()
                .filter(cp -> chargingPointId.equals(cp.getId()))
                .findFirst();

        assertThat(chargingPoint.isPresent()).isTrue();
        chargingPoint.get().stopChargingSession(LocalDateTime.now(), loadedKwh);
    }

    @Then("the station {string} in location {string} status should be not occupied")
    public void theStationInLocationStatusShouldBeNotOccupied(String chargingPointId, String locationId) {
        final Location location = this.locationManager.getLocation(locationId);
        final Optional<ChargingPoint> chargingPoint = location.getChargingPoints().stream()
                .filter(cp -> chargingPointId.equals(cp.getId()))
                .findFirst();
        assertThat(chargingPoint.isPresent()).isTrue();
        assertThat(chargingPoint.get().isOccupied()).isFalse();
    }

    @And("user {string} has a balance of {double}")
    public void userHasABalanceOf(String userId, Double balance ) {
        EvDriver client = clientManager.getClient(userId);
        Double b = client.getAccount().getBalance();
        assertThat(client.getAccount().getBalance()).isEqualTo(balance);
    }

    @And("station {string} in location {string} is has a rate of {double}")
    public void stationInLocationIsHasARateOf(String chargingPointId, String locationId, Double pricePerKwh) {
        final Location location = this.locationManager.getLocation(locationId);
        assertThat(location).isNotNull();
        final Optional<ChargingPoint> chargingPoint = location.getChargingPoints().stream()
                .filter(cp -> chargingPointId.equals(cp.getId()))
                .findFirst();
        assertThat(chargingPoint.isPresent()).isTrue();
        assertThat(location.getPriceModel().getPricePerKwh(chargingPoint.get().getType())).isEqualTo(pricePerKwh);
    }

    @And("station {string} in location {string} and is occupied")
    public void stationInLocationAndIsOccupied(String chargingPointId, String locationId) {
        final Location location = this.locationManager.getLocation(locationId);
        assertThat(location).isNotNull();
        final Optional<ChargingPoint> chargingPoint = location.getChargingPoints().stream()
                .filter(cp -> chargingPointId.equals(cp.getId()))
                .findFirst();
        assertThat(chargingPoint.isPresent()).isTrue();
        assertThat(chargingPoint.get().isOccupied()).isTrue();
    }

    @And("station {string} in location {string} and is not occupied")
    public void stationInLocationAndIsNotOccupied(String chargingPointId, String locationId) {
        final Location location = this.locationManager.getLocation(locationId);
        assertThat(location).isNotNull();
        final Optional<ChargingPoint> chargingPoint = location.getChargingPoints().stream()
                .filter(cp -> chargingPointId.equals(cp.getId()))
                .findFirst();
        assertThat(chargingPoint.isPresent()).isTrue();
        assertThat(chargingPoint.get().isOccupied()).isFalse();
    }

    @Given("the user {string} exists")
    public void theUserExists(String userId) {
        assertThat(clientManager.getClient(userId)).isNotNull();
    }

    @And("client {string} has {int} invoices")
    public void clientHasInvoices(String userId, int invoiceCount) {
        EvDriver user = clientManager.getClient(userId);
        assertThat(user.getInvoices().size()).isEqualTo(invoiceCount);
    }

    @When("client {string} tops up balance by {double}")
    public void clientTopsUpBalanceBy(String userId, Double amount) {
        EvDriver user = clientManager.getClient(userId);
        new TopUpTransaction(UUID.randomUUID().toString(), amount, LocalDateTime.now(), user.getAccount());
    }
}
