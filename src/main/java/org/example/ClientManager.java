package org.example;

import java.util.HashMap;
import java.util.Map;

public class ClientManager {

    private final Map<String, EvDriver> clients = new HashMap<>();

    public boolean existsClient(String id) {
        return clients.containsKey(id);
    }

    // --- Client CRUD ---
    public void registerClient(String id, String name, String email) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Client id must not be blank");
        }
        if (clients.containsKey(id)) {
            throw new IllegalArgumentException("Client with id " + id + " already exists");
        }
        clients.put(id, new EvDriver(id, name, email));
    }

    public EvDriver getClient(String id) {
        return clients.get(id);
    }

    public void deleteClient(String id) {
        if (!clients.containsKey(id)) {
            throw new IllegalArgumentException("Client " + id + " does not exist");
        }
        clients.remove(id);
    }
}
