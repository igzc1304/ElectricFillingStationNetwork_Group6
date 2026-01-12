package org.example;

import java.time.LocalDateTime;

public class TopUpTransaction {
    private final String id;
    private final Double amount;
    private final LocalDateTime timestamp;
    private final Account account;

    public TopUpTransaction(String id, Double amount, LocalDateTime timestamp, Account account) {
        this.id = id;
        this.amount = amount;
        this.timestamp = timestamp;
        this.account = account;
        this.account.topUp(amount);
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
