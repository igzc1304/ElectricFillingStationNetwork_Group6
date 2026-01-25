package org.example;

public class Account {
    private Double balance = 0.0;

    public Account() {}

    public void topUp(Double amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Top-up amount must be > 0");
        }
        this.balance += amount;
    }

    public void deduct(Double amount) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("Deduction amount must be >= 0");
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        this.balance -= amount;
    }

    public Double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Account{" + "\n" +
                "\tbalance=" + balance + "\n" +
                '}';
    }
}
