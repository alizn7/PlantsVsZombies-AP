package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.Bullet;
import org.example.plants_vs_zombies1.model.enums.BulletType;
import org.example.plants_vs_zombies1.model.enums.PlantType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Peashooter extends WarriorPlant {
    private static final Logger LOGGER = Logger.getLogger(Peashooter.class.getName());
    private static final double SHOOT_INTERVAL = 1.5;
    private long lastShotTime = 0;

    public Peashooter(int x, int y) {
        super("Peashooter", 1, 100, x, y, PlantType.PEASHOOTER, 25, 1500, BulletType.NORMAL);
    }

    @Override
    public List<Bullet> shoot() {
        List<Bullet> bullets = new ArrayList<>();
        long currentTime = System.nanoTime();
        if (currentTime - lastShotTime >= SHOOT_INTERVAL * 1_000_000_000) {
            Bullet bullet = new Bullet(attackPower, bulletType, y, x + 1, 1.0);
            bullets.add(bullet);
            LOGGER.info("Peashooter shoots a bullet at (" + bullet.getX() + "," + bullet.getY() + ") with damage " + bullet.getDamage());
            lastShotTime = currentTime;
        }
        return bullets;
    }

    @Override
    public void performAction() {
    }
}