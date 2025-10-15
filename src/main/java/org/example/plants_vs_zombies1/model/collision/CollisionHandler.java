package org.example.plants_vs_zombies1.model.collision;

import org.example.plants_vs_zombies1.model.Bullet;
import org.example.plants_vs_zombies1.model.board.GameBoard;
import org.example.plants_vs_zombies1.model.plant.Plant;
import org.example.plants_vs_zombies1.model.plant.ExplosivePlant;
import org.example.plants_vs_zombies1.model.zombie.Zombie;
import org.example.plants_vs_zombies1.model.zombie.ScreenDoorZombie;
import org.example.plants_vs_zombies1.model.zombie.NewspaperZombie;
import org.example.plants_vs_zombies1.model.enums.BulletType;
import org.example.plants_vs_zombies1.service.ZombieRenderer;

import java.util.List;
import java.util.logging.Logger;

public class CollisionHandler {
    private static final Logger LOGGER = Logger.getLogger(CollisionHandler.class.getName());
    private final GameBoard gameBoard;
    private final ZombieRenderer zombieRenderer;

    public CollisionHandler(GameBoard gameBoard, ZombieRenderer zombieRenderer) {
        this.gameBoard = gameBoard;
        this.zombieRenderer = zombieRenderer;
    }

    public void handleBulletCollision(Bullet bullet, List<Zombie> hitZombies) {
        if (bullet == null || bullet.isRemoved() || hitZombies.isEmpty()) {
            return;
        }

        Zombie zombie = hitZombies.get(0);
        if (zombie == null || zombie.isDead()) {
            return;
        }

        int zombieRow = zombie.getY();
        int zombieCol = zombie.getX();
        if (bullet.getY() != zombieRow || bullet.getX() != zombieCol) {
            LOGGER.fine("Bullet at (" + bullet.getY() + "," + bullet.getX() + ") missed zombie at (" + zombieRow + "," + zombieCol + ")");
            return;
        }

        int damage = bullet.getDamage();
        if (zombie instanceof ScreenDoorZombie) {
            ScreenDoorZombie sdZombie = (ScreenDoorZombie) zombie;
            sdZombie.takeDamage(damage, bullet.getType());
        } else if (zombie instanceof NewspaperZombie) {
            NewspaperZombie npZombie = (NewspaperZombie) zombie;
            npZombie.takeDamage(damage);
        } else {
            zombie.takeDamage(damage);
            if (bullet.getType() == BulletType.SNOW) {
                zombie.setSpeed(zombie.getSpeed() / 2);
                LOGGER.info("SnowPea slows " + zombie.getName() + " at (" + zombieRow + "," + zombieCol + "), New speed: " + zombie.getSpeed());
            }
        }

        LOGGER.fine(() -> "Zombie " + zombie.getName() + " took " + damage + " damage at (" + zombieRow + "," + zombieCol + "), Health: " + zombie.getHealth());
        bullet.markAsRemoved();
        gameBoard.removeBullet(bullet);

        if (zombie.isDead()) {
            gameBoard.removeZombie(zombieRow, zombieCol);
            zombieRenderer.showDeathGifs(zombie);
        }
    }

    public void handleZombiePlantCollision(Zombie zombie, Plant plant) {
        if (zombie == null || zombie.isDead() || plant == null || plant.isDead()) return;
        int plantRow = plant.getY();
        int plantCol = plant.getX();
        plant.takeDamage(zombie.getAttackPower());
        LOGGER.info("Zombie " + zombie.getName() + " attacks plant " + plant.getName() + " at (" + plantRow + "," + plantCol + "), Plant health: " + plant.getHealth());
        if (plant.isDead()) {
            gameBoard.removePlant(plantRow, plantCol);
            LOGGER.info("Plant " + plant.getName() + " destroyed at (" + plantRow + "," + plantCol + ")");
        }
    }

    public void handleExplosionCollision(ExplosivePlant plant, List<Zombie> hitZombies) {
        int plantRow = plant.getY();
        int plantCol = plant.getX();
        for (Zombie zombie : hitZombies) {
            if (zombie == null || zombie.isDead()) continue;
            int zombieRow = zombie.getY();
            int zombieCol = zombie.getX();
            zombie.takeDamage(plant.getBlastPower());
            LOGGER.info("Explosion from " + plant.getName() + " hits " + zombie.getName() + " at (" + zombieRow + "," + zombieCol + "), Zombie health: " + zombie.getHealth());
            if (zombie.isDead()) {
                gameBoard.removeZombie(zombieRow, zombieCol);
                zombieRenderer.showDeathGifs(zombie);
            }
        }
        gameBoard.removePlant(plantRow, plantCol);
    }

    public void handleIceShroomCollision(List<Zombie> hitZombies) {
        for (Zombie zombie : hitZombies) {
            if (zombie == null || zombie.isDead()) continue;
            zombie.setSpeed(zombie.getSpeed() / 2);
            LOGGER.info("IceShroom slows " + zombie.getName() + " at (" + zombie.getY() + "," + zombie.getX() + ")");
        }
    }

    public void handleDoomShroomCollision(List<Zombie> hitZombies) {
        for (Zombie zombie : hitZombies) {
            if (zombie == null || zombie.isDead()) continue;
            int zombieRow = zombie.getY();
            int zombieCol = zombie.getX();
            zombie.takeDamage(150);
            LOGGER.info("DoomShroom hits " + zombie.getName() + " at (" + zombieRow + "," + zombieCol + "), Zombie health: " + zombie.getHealth());
            if (zombie.isDead()) {
                gameBoard.removeZombie(zombieRow, zombieCol);
                zombieRenderer.showDeathGifs(zombie);
            }
        }
    }
}