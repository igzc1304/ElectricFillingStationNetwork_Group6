package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAccountSteps {

    private final TestWorld w = Hooks.world();

    @Given("there is no EV driver with id {string}")
    public void thereIsNoEvDriverWithId(String evDriverId) {
        assertThat(w.clientManager.existsClient(evDriverId)).isFalse();
    }

    @When("I register an EV driver with id {string} and name {string} and email {string}")
    public void iRegisterAnEvDriverWithIdAndNameAndEmail(String id, String name, String email) {
        try {
            w.clientManager.registerClient(id, name, email);
            w.lastException = null;
        } catch (Exception e) {
            w.lastException = e;
        }
    }

    @Then("the system should contain an EV driver with id {string}")
    public void theSystemShouldContainAnEvDriverWithId(String id) {
        assertThat(w.clientManager.existsClient(id)).isTrue();
        EvDriver client = w.clientManager.getClient(id);
        assertThat(client).isNotNull();
        assertThat(client.getId()).isEqualTo(id);
    }
}
