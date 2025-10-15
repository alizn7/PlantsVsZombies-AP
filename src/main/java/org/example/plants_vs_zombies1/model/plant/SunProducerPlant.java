package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.Sun;
import org.example.plants_vs_zombies1.model.enums.PlantType;

public abstract class SunProducerPlant extends Plant {
    protected int productionRate;
    protected int sunValue;
    private long lastSunProductionTime;

    public SunProducerPlant(String name, int sunCost, int health, int x, int y, PlantType plantType, int productionRate, int sunValue) {
        super(name, sunCost, health, x, y, plantType);
        this.productionRate = productionRate;
        this.sunValue = sunValue;
        this.lastSunProductionTime = System.currentTimeMillis();
    }

    public synchronized Sun produceSun() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSunProductionTime >= productionRate) {
            lastSunProductionTime = currentTime;
            return new Sun(sunValue, x, y);
        }
        return null;
    }

    public int getProductionRate() {
        return productionRate;
    }

    public int getSunValue() {
        return sunValue;
    }

    public synchronized void setProductionRate(int productionRate) {
        this.productionRate = productionRate;
    }

    public synchronized void setSunValue(int sunValue) {
        this.sunValue = sunValue;
    }

    @Override
    public abstract void performAction();
}