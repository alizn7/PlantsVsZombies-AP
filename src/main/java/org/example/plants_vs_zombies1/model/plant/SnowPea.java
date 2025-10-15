package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.Bullet;
import org.example.plants_vs_zombies1.model.enums.BulletType;
import org.example.plants_vs_zombies1.model.enums.PlantType;

import java.util.List;

public class SnowPea extends WarriorPlant {
    public SnowPea(int x, int y) {
        super("SnowPea", 175, 100, x, y, PlantType.SNOW_PEA, 25, 2000, BulletType.SNOW);
    }

    @Override
    public void performAction() {
        List<Bullet> bullets = shoot();
        for (Bullet bullet : bullets) {
            if (bullet != null) {
                System.out.println("SnowPea shoots a snow bullet at (" + bullet.getX() + "," + bullet.getY() + ") with damage " + bullet.getDamage() + " that slows zombies!");
            }
        }
    }
}