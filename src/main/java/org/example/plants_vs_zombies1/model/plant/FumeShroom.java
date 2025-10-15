package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.Bullet;
import org.example.plants_vs_zombies1.model.enums.BulletType;
import org.example.plants_vs_zombies1.model.enums.PlantType;

import java.util.List;

public class FumeShroom extends WarriorPlant {
    public FumeShroom(int x, int y) {
        super("FumeShroom", 75, 100, x, y, PlantType.FUME_SHROOM, 40, 2000, BulletType.SMOKE);
    }

    @Override
    public void performAction() {
        List<Bullet> bullets = shoot();
        for (Bullet bullet : bullets) {
            if (bullet != null) {
                System.out.println("FumeShroom shoots a smoke bullet at (" + bullet.getX() + "," + bullet.getY() + ") with damage " + bullet.getDamage() + " that can pass through zombies!");
            }
        }
    }
}