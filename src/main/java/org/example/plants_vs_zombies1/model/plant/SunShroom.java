package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.Sun;
import org.example.plants_vs_zombies1.model.enums.PlantType;

public class SunShroom extends Plant {
    private long plantedTime;
    private long lastProduceTime;
    private boolean isGrownUp = false;

    private final long growUpTime = 24000;
    private final long produceInterval = 24000;

    public SunShroom(int x, int y) {
        super("SunShroom", 75, 100, x, y, PlantType.SUN_SHROOM);
        this.plantedTime = System.currentTimeMillis();
        this.lastProduceTime = plantedTime - produceInterval;
    }

    @Override
    public void performAction() {
        long currentTime = System.currentTimeMillis();

        if (!isGrownUp && currentTime - plantedTime >= growUpTime) {
            isGrownUp = true;
            System.out.println("🌞 SunShroom at (" + x + "," + y + ") has grown up!");
        }

        if (currentTime - lastProduceTime >= produceInterval) {
            int sunValue = isGrownUp ? 25 : 15;
            Sun sun = new Sun(sunValue, y, x);
            board.placeSun(sun, y, x);
            lastProduceTime = currentTime;
            System.out.println("SunShroom produced " + sunValue + " sun at (" + y + "," + x + ")!");
        }
    }
}
