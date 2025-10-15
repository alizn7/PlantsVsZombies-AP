package org.example.plants_vs_zombies1.model.zombie;

import org.example.plants_vs_zombies1.model.enums.ZombieType;

public abstract class ResistantZombie extends Zombie {
    protected int extraResistance;

    public ResistantZombie(String name, int health, double speed, int attackPower, int x, int y, ZombieType zombieType, int extraResistance) {
        super(name, health, speed, attackPower, x, y, zombieType);
        this.extraResistance = extraResistance;
    }

    public int getExtraResistance() {
        return extraResistance;
    }

    @Override
    public void takeDamage(int damage) {
        if (extraResistance > 0) {
            extraResistance = Math.max(0, extraResistance - damage);
            System.out.println("ResistantZombie " + name + " shield took " + damage + " damage, Shield: " + extraResistance);
        } else {
            super.takeDamage(damage);
        }
    }
}