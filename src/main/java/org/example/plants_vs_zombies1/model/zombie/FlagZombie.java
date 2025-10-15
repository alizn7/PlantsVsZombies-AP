package org.example.plants_vs_zombies1.model.zombie;

import org.example.plants_vs_zombies1.model.enums.ZombieType;

public class FlagZombie extends SpecialZombie {
    public FlagZombie(int y, int x, double v) {
        super("FlagZombie", 200, 10.0, 25, x, y, ZombieType.FLAG_ZOMBIE, "HighSpeed");
    }

    @Override
    public void performAction() {
        System.out.println("FlagZombie moves forward with high speed at (" + y + "," + x + ")!");
    }
}