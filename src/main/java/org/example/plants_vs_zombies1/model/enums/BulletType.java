package org.example.plants_vs_zombies1.model.enums;

public enum BulletType {
    NORMAL("Normal"),
    SNOW("Snow"),
    SMOKE("Smoke");

    private final String name;

    BulletType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
