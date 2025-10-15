package org.example.plants_vs_zombies1.model.zombie;

import org.example.plants_vs_zombies1.model.enums.ZombieType;

public abstract class SpecialZombie extends Zombie {
    protected String specialAbility;

    public SpecialZombie(String name, int health, double speed, int attackPower, int x, int y, ZombieType zombieType, String specialAbility) {
        super(name, health, speed, attackPower, x, y, zombieType);
        this.specialAbility = specialAbility;
    }

    public String getSpecialAbility() {
        return specialAbility;
    }
}