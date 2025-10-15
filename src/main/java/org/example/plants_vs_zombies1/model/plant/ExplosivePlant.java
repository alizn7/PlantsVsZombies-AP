package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.enums.PlantType;

public abstract class ExplosivePlant extends Plant {
    protected int blastPower;
    protected int blastRange;
    private long plantingTime;
    private boolean hasExploded;
    private static final long EXPLOSION_DELAY = 1000;

    public ExplosivePlant(String name, int sunCost, int health, int x, int y, PlantType plantType, int blastPower, int blastRange) {
        super(name, sunCost, health, x, y, plantType);
        this.blastPower = blastPower;
        this.blastRange = blastRange;
        this.plantingTime = System.currentTimeMillis();
        this.hasExploded = false;
    }

    public synchronized boolean isReadyToExplode() {
        if (hasExploded) {
            return false;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - plantingTime >= EXPLOSION_DELAY) {
            hasExploded = true;
            return true;
        }
        return false;
    }

    public int getBlastPower() {
        return blastPower;
    }

    public int getBlastRange() {
        return blastRange;
    }

    public synchronized void setHasExploded(boolean hasExploded) {
        this.hasExploded = hasExploded;
    }

    @Override
    public abstract void performAction();
}