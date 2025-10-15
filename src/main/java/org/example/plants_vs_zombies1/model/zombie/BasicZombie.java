package org.example.plants_vs_zombies1.model.zombie;

import org.example.plants_vs_zombies1.model.enums.ZombieType;

public class BasicZombie extends Zombie {
    public BasicZombie(int y, int x, double v) {
        super("BasicZombie", 200, 8.0, 25, x, y, ZombieType.BASIC_ZOMBIE);
    }

    @Override
    public void performAction() {
        System.out.println("BasicZombie moves forward and attacks at (" + y + "," + x + ")!");
    }
}
