package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.enums.PlantType;

public class WallNut extends DefensivePlant {
    public WallNut(int x, int y) {
        super("WallNut", 50, 4000, x, y, PlantType.WALL_NUT);
    }

    @Override
    public void performAction() {
        System.out.println("WallNut blocks zombies at (" + x + "," + y + ") with health " + health);
    }
}