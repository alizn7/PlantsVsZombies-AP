package org.example.plants_vs_zombies1.model;

import org.example.plants_vs_zombies1.model.enums.BulletType;

public class Bullet {
    private final int damage;
    private final BulletType type;
    private double x;
    private final int y;
    private final double speed;
    private boolean removed;

    public Bullet(int damage, BulletType type, int row, int col, double speed) {
        this.damage = damage;
        this.type = type;
        this.y = row;
        this.x = col;
        this.speed = speed;
        this.removed = false;
    }

    public int getDamage() {
        return damage;
    }

    public BulletType getType() {
        return type;
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return y;
    }

    public double getPreciseX() {
        return x;
    }

    public double getSpeed() {
        return speed;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(int y) {
        throw new UnsupportedOperationException("Y position of bullet cannot be changed");
    }

    public boolean isRemoved() {
        return removed;
    }

    public void markAsRemoved() {
        this.removed = true;
    }
}