package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.Sun;
import org.example.plants_vs_zombies1.model.enums.PlantType;

public class Sunflower extends SunProducerPlant {
    public Sunflower(int x, int y) {
        super("Sunflower", 50, 300, x, y, PlantType.SUNFLOWER, 12000, 25);
    }

    @Override
    public void performAction() {
        Sun sun = produceSun();
        if (sun != null) {
            board.placeSun(sun, y, x);
            System.out.println("Sunflower produced " + sun.getValue() + " sun at (" + x + "," + y + ")!");
        }
    }
}