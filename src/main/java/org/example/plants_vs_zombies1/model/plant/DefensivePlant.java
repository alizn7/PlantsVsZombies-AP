package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.enums.PlantType;

public abstract class DefensivePlant extends Plant {
    public DefensivePlant(String name, int sunCost, int health, int x, int y, PlantType plantType) {
        super(name, sunCost, health, x, y, plantType); // x=row, y=col
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        System.out.println("DefensivePlant " + name + " took " + damage + " damage, Health: " + health);
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    @Override
    public abstract void performAction();
}