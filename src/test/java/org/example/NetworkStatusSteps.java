package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class NetworkStatusSteps {

    private final TestWorld w = Hooks.world();

    @Given("the network has no locations")
    public void theNetworkHasNoLocations() {
        // Hooks seeds baseline data before each scenario; override here to create an empty network.
        w.locationManager = new LocationManager();
    }

    @Then("the network status list should contain")
    public void theNetworkStatusListShouldContain(DataTable table) {
        List<NetworkStatusService.NetworkStatusEntry> entries = w.networkStatusService.getNetworkStatus(w.locationManager);

        List<Map<String, String>> expected = table.asMaps(String.class, String.class);
        for (Map<String, String> row : expected) {
            String loc = row.get("locationName");
            String cpId = row.get("chargingPointId");
            String status = row.get("status");

            boolean found = entries.stream().anyMatch(e ->
                    e.locationName().equals(loc)
                            && e.chargingPointId().equals(cpId)
                            && e.status().name().equals(status)
            );
            assertThat(found).isTrue();
        }
    }

    @Then("the network status list should be empty")
    public void theNetworkStatusListShouldBeEmpty() {
        List<NetworkStatusService.NetworkStatusEntry> entries = w.networkStatusService.getNetworkStatus(w.locationManager);
        assertThat(entries).isEmpty();
    }
}
