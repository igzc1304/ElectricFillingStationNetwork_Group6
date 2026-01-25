package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserBillingSteps {

    private final TestWorld w = Hooks.world();

    @Given("the user {string} exists")
    public void theUserExists(String userId) {
        assertThat(w.clientManager.getClient(userId)).isNotNull();
    }

    @And("user {string} has a balance of {double}")
    public void userHasABalanceOf(String userId, Double balance) {
        EvDriver client = w.clientManager.getClient(userId);
        assertThat(client.getAccount().getBalance()).isEqualTo(balance);
    }

    @When("client {string} tops up balance by {double}")
    public void clientTopsUpBalanceBy(String userId, Double amount) {
        try {
            EvDriver user = w.clientManager.getClient(userId);
            new TopUpTransaction(UUID.randomUUID().toString(), amount, LocalDateTime.now(), user);
            w.lastException = null;
        } catch (Exception e) {
            w.lastException = e;
        }
    }

    @Then("user {string} should have {int} top up transactions")
    public void userShouldHaveTopUpTransactions(String userId, Integer count) {
        EvDriver user = w.clientManager.getClient(userId);
        assertThat(user.getTopUps().size()).isEqualTo(count);
    }

    @Then("invoice status for {string} should contain items")
    public void invoiceStatusForShouldContainItems(String userId, DataTable table) {
        EvDriver user = w.clientManager.getClient(userId);
        BillingService.InvoiceStatus status = w.billingService.getInvoiceStatus(user);

        List<Map<String, String>> expected = table.asMaps(String.class, String.class);
        assertThat(status.items().size()).isGreaterThanOrEqualTo(expected.size());

        for (int i = 0; i < expected.size(); i++) {
            Map<String, String> row = expected.get(i);
            BillingService.InvoiceItem item = status.items().get(i);
            assertThat(item.locationName()).isEqualTo(row.get("locationName"));
            assertThat(item.chargingPointId()).isEqualTo(row.get("chargingPointId"));
            assertThat(item.chargingMode()).isEqualTo(row.get("chargingMode"));
        }
    }
}
