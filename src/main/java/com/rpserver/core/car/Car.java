package com.rpserver.core.car;

import java.util.UUID;

public class Car {

    public enum Tip {
        EKONOMIK, SPOR, LUKS
    }

    private final String id;
    private final UUID sahip;
    private final Tip tip;
    private UUID spawnliVarlik; // su an dunyada var mi (at entity id)

    public Car(String id, UUID sahip, Tip tip) {
        this.id = id;
        this.sahip = sahip;
        this.tip = tip;
    }

    public String getId() {
        return id;
    }

    public UUID getSahip() {
        return sahip;
    }

    public Tip getTip() {
        return tip;
    }

    public UUID getSpawnliVarlik() {
        return spawnliVarlik;
    }

    public void setSpawnliVarlik(UUID spawnliVarlik) {
        this.spawnliVarlik = spawnliVarlik;
    }
}
