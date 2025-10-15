package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.Bullet;
import org.example.plants_vs_zombies1.model.enums.BulletType;
import org.example.plants_vs_zombies1.model.enums.PlantType;
import org.example.plants_vs_zombies1.model.zombie.Zombie;

import java.util.List;

public class ScaredyShroom extends WarriorPlant {
    public boolean isScared;

    public ScaredyShroom(int x, int y) {
        super("ScaredyShroom", 125, 100, x, y, PlantType.SCAREDY_SHROOM, 30, 1000, BulletType.SMOKE);
        this.isScared = false;
    }

    @Override
    public void performAction() {
        updateScaredState();
        if (!isScared) {
            List<Bullet> bullets = shoot();
            for (Bullet bullet : bullets) {
                if (bullet != null) {
                    board.getBullets().add(bullet);
                    System.out.println("ScaredyShroom shoots a smoke bullet at (" + bullet.getX() + "," + bullet.getY() + ") with damage " + bullet.getDamage());
                }
            }
        } else {
            System.out.println("😨 ScaredyShroom is scared at (" + x + "," + y + ") and cannot shoot!");
        }
    }

    public void updateScaredState() {
        List<Zombie> zombies = board.getZombiesInRow(y);
        for (Zombie z : zombies) {
            if (!z.isDead() && Math.abs(z.getX() - x) <= 2) {
                isScared = true;
                return;
            }
        }
        isScared = false;
    }

    public void setScared(boolean scared) {
        this.isScared = scared;
    }
}
