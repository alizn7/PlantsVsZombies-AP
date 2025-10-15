package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.enums.PlantType;

public class DoomShroom extends ExplosivePlant {
    public DoomShroom(int x, int y) {
        super("DoomShroom", 125, 100, x, y, PlantType.DOOM_SHROOM, 150, 9);
    }

    @Override
    public void performAction() {
        if (isReadyToExplode()) {
            System.out.println("DoomShroom performs a deadly explosion on the board from (" + x + "," + y + ") with damage " + blastPower + "!");
            setHasExploded(true);
        }
    }
}