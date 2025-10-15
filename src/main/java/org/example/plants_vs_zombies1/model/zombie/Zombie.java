package org.example.plants_vs_zombies1.model.zombie;

import org.example.plants_vs_zombies1.model.enums.ZombieType;

public abstract class Zombie {
    protected String name;
    protected int health;
    protected double speed;
    protected int attackPower;
    protected int x;
    protected int y;
    protected ZombieType zombieType;

    public Zombie(String name, int health, double speed, int attackPower, int x, int y, ZombieType zombieType) {
        this.name = name;
        this.health = health;
        this.speed = speed * 0.5;
        this.attackPower = attackPower;
        this.x = x;
        this.y = y;
        this.zombieType = zombieType;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public double getSpeed() {
        return speed;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ZombieType getZombieType() {
        return zombieType;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void takeDamage(int damage) {
        this.health = Math.max(0, health - damage);
        System.out.println("Zombie " + name + " took " + damage + " damage, Health: " + health);
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public void setZombieType(ZombieType zombieType) {
        this.zombieType = zombieType;
    }

    public abstract void performAction();
}