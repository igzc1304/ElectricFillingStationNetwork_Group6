package org.example;

import java.util.ArrayList;
import java.util.List;

public class EvDriver {

    private final String id;
    private final String name;
    private final String email;
    private final Account account = new Account();
    private final List<TopUpTransaction> topUps = new ArrayList<>();
    private final List<ChargingSession> chargingSessions = new ArrayList<>();

    public EvDriver(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Account getAccount() {
        return account;
    }

    public List<TopUpTransaction> getTopUps() {
        return topUps;
    }

    public List<ChargingSession> getChargingSessions() {
        return chargingSessions;
    }

    @Override
    public String toString() {
        return "EvDriver{" + "\n" +
                "\tid='" + id + '\'' + "\n" +
                "\tname='" + name + '\'' + "\n" +
                "\temail='" + email + '\'' + "\n" +
                "\taccount=" + account + "\n" +
                "\ttopUps=" + topUps + "\n" +
                "\tchargingSessions=" + chargingSessions + "\n" +
                '}';
    }
}
