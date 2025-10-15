package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.enums.PlantType;

public class CherryBomb extends ExplosivePlant {
    public CherryBomb(int x, int y) {
        super("CherryBomb", 150, 100, x, y, PlantType.CHERRY_BOMB, 100, 1);
    }

    @Override
    public void performAction() {
        if (isReadyToExplode()) {
            System.out.println("CherryBomb explodes in a 3x3 area at (" + x + "," + y + ") with damage " + blastPower + "!");
            setHasExploded(true);
        }
    }
}