package org.example.plants_vs_zombies1.model;

public class HouseProtection {
    private int protectionLevel;
    public static final int MAX_PROTECTION = 100;

    public HouseProtection() {
        this.protectionLevel = MAX_PROTECTION;
    }

    public void reduceProtection(int damage) {
        protectionLevel = Math.max(0, protectionLevel - damage);
        System.out.println("House protection reduced by " + damage + ", Current level: " + protectionLevel);
    }

    public boolean isDepleted() {
        return protectionLevel <= 0;
    }

    public int getProtectionLevel() {
        return protectionLevel;
    }
}