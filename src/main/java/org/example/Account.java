package org.example;

public class Account {
    private Double balance = 0.0;

    public Account() {}

    public void topUp(Double amount) {
        this.balance += amount;
    }

    public void deduct(Double amount) {
        this.balance -= amount;
    }

    public Double getBalance() {
        return balance;
    }
}
