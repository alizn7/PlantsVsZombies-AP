/* package org.example.plants_vs_zombies1.service;

import org.example.plants_vs_zombies1.model.Bullet;
import org.example.plants_vs_zombies1.model.board.GameBoard;
import org.example.plants_vs_zombies1.model.collision.CollisionDetector;
import org.example.plants_vs_zombies1.model.collision.CollisionHandler;
import org.example.plants_vs_zombies1.model.enums.BulletType;
import org.example.plants_vs_zombies1.model.zombie.Zombie;

import java.util.List;

public class BulletService {
    private final GameBoard board;
    private final CollisionDetector collisionDetector;
    private final CollisionHandler collisionHandler;

    public BulletService(GameBoard board, CollisionDetector collisionDetector, CollisionHandler collisionHandler) {
        this.board = board;
        this.collisionDetector = collisionDetector;
        this.collisionHandler = collisionHandler;
    }

    public void updateBullets() {
        List<Bullet> bullets = board.getBullets();
        if (bullets == null || bullets.isEmpty()) return;

        for (Bullet bullet : bullets.toArray(new Bullet[0])) {
            if (bullet == null) continue;

            int newX = bullet.getX() + (int) bullet.getSpeed();
            bullet.setX(newX);

            if (newX >= board.getCols()) {
                board.removeBullet(bullet);
                continue;
            }

            List<Zombie> zombiesInRow = board.getZombiesInRow(bullet.getY());
            boolean bulletRemoved = false;

            for (Zombie zombie : zombiesInRow) {
                if (zombie.isDead()) continue;

                if (bullet.getX() == zombie.getX() && bullet.getY() == zombie.getY()) {
                    collisionHandler.handleZombieBulletCollision(zombie, bullet.getDamage(), bullet.getType());
                    if (bullet.getType() != BulletType.SMOKE) {
                        board.removeBullet(bullet);
                        bulletRemoved = true;
                        break;
                    }
                }
            }

            if (!bulletRemoved && board.isValidPosition(bullet.getY(), newX)) {
                board.moveBullet(bullet, bullet.getY(), newX);
            }
        }
    }
}

 */