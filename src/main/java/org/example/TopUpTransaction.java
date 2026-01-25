package org.example;

import java.time.LocalDateTime;

public class TopUpTransaction {
    private final String id;
    private final Double amount;
    private final LocalDateTime timestamp;
    private final Account account;
    private final EvDriver evDriver;

    public TopUpTransaction(String id, Double amount, LocalDateTime timestamp, EvDriver evDriver) {
        this.id = id;
        this.amount = amount;
        this.timestamp = timestamp;
        this.evDriver = evDriver;
        this.account = evDriver.getAccount();
        this.account.topUp(amount);
        evDriver.getTopUps().add(this);
    }

    public String getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Account getAccount() {
        return account;
    }

    public EvDriver getEvDriver() {
        return evDriver;
    }

    @Override
    public String toString() {
        return "TopUpTransaction{" + "\n" +
                "\tid='" + id + '\'' + "\n" +
                "\tamount=" + amount + "\n" +
                "\ttimestamp=" + timestamp + "\n" +
                "\taccount=" + account + "\n" +
                '}';
    }
}
