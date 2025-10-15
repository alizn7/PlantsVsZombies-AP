package org.example.plants_vs_zombies1.model.zombie;

import org.example.plants_vs_zombies1.model.enums.ZombieType;

public class ConeheadZombie extends ResistantZombie {
    public ConeheadZombie(int y, int x, double v) {
        super("ConeheadZombie", 300, 13, 25, x, y, ZombieType.CONEHEAD_ZOMBIE, 200);
    }

    @Override
    public void performAction() {
        System.out.println("ConeheadZombie moves forward with extra resistance at (" + y + "," + x + ")!");
    }
}