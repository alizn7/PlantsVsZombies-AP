package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.Bullet;
import org.example.plants_vs_zombies1.model.enums.BulletType;
import org.example.plants_vs_zombies1.model.enums.PlantType;

import java.util.List;

public class PuffShroom extends WarriorPlant {
    public PuffShroom(int x, int y) {
        super("PuffShroom", 0, 100, x, y, PlantType.PUFF_SHROOM, 15, 1500, BulletType.SMOKE);
    }

    @Override
    public void performAction() {
        List<Bullet> bullets = shoot();
        for (Bullet bullet : bullets) {
            if (bullet != null) {
                System.out.println("PuffShroom shoots a smoke bullet at (" + bullet.getX() + "," + bullet.getY() + ") with damage " + bullet.getDamage());
            }
        }
    }
}