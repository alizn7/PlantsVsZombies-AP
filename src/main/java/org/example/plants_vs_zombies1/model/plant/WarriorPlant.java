package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.Bullet;
import org.example.plants_vs_zombies1.model.enums.BulletType;
import org.example.plants_vs_zombies1.model.enums.PlantType;

import java.util.ArrayList;
import java.util.List;

public abstract class WarriorPlant extends Plant {
    protected int attackPower;
    protected int attackSpeed;
    protected BulletType bulletType;
    protected long lastAttackTime;

    public WarriorPlant(String name, int sunCost, int health, int x, int y, PlantType plantType, int attackPower, int attackSpeed, BulletType bulletType) {
        super(name, sunCost, health, x, y, plantType);
        this.attackPower = attackPower;
        this.attackSpeed = attackSpeed;
        this.bulletType = bulletType;
        this.lastAttackTime = System.currentTimeMillis();
    }

    public List<Bullet> shoot() {
        List<Bullet> bullets = new ArrayList<>();
        bullets.add(new Bullet(attackPower, bulletType, y, x + 1, 1.0));
        return bullets;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public abstract void performAction();
}