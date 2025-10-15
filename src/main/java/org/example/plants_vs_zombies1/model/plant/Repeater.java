package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.Bullet;
import org.example.plants_vs_zombies1.model.enums.BulletType;
import org.example.plants_vs_zombies1.model.enums.PlantType;

import java.util.ArrayList;
import java.util.List;

public class Repeater extends WarriorPlant {
    public Repeater(int x, int y) {
        super("Repeater", 200, 100, x, y, PlantType.REPEATER, 25, 2000, BulletType.NORMAL);
    }

    @Override
    public List<Bullet> shoot() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime >= attackSpeed) {
            lastAttackTime = currentTime;
            List<Bullet> bullets = new ArrayList<>();
            bullets.add(new Bullet(attackPower, bulletType, y, x + 1, 1));
            bullets.add(new Bullet(attackPower, bulletType, y, x + 1, 1));
            return bullets;
        }
        return new ArrayList<>();
    }

    @Override
    public void performAction() {
        List<Bullet> bullets = shoot();
        for (Bullet bullet : bullets) {
            if (bullet != null) {
                board.getBullets().add(bullet);
                System.out.println("Repeater shoots a normal bullet at (" + bullet.getX() + "," + bullet.getY() + ") with damage " + bullet.getDamage());
            }
        }
    }
}
