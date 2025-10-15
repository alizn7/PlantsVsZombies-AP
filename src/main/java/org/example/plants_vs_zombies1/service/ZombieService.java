package org.example.plants_vs_zombies1.service;

import org.example.plants_vs_zombies1.model.board.GameBoard;
import org.example.plants_vs_zombies1.model.collision.CollisionDetector;
import org.example.plants_vs_zombies1.model.collision.CollisionHandler;
import org.example.plants_vs_zombies1.model.GameState;
import org.example.plants_vs_zombies1.model.HouseProtection;
import org.example.plants_vs_zombies1.model.plant.Plant;
import org.example.plants_vs_zombies1.model.zombie.Zombie;
import org.example.plants_vs_zombies1.model.zombie.NewspaperZombie;
import org.example.plants_vs_zombies1.util.ZombieGifManager;

import java.util.List;
import java.util.logging.Logger;

public class ZombieService {
    private static final Logger LOGGER = Logger.getLogger(ZombieService.class.getName());
    private final GameBoard gameBoard;
    private final CollisionDetector detector;
    private final CollisionHandler handler;
    private final GameState gameState;
    private final HouseProtection houseProtection;
    private final ZombieRenderer zombieRenderer;

    public ZombieService(GameBoard board,
                         CollisionDetector detector,
                         CollisionHandler handler,
                         GameState gameState,
                         HouseProtection houseProtection,
                         ZombieRenderer zombieRenderer) {
        this.gameBoard = board;
        this.detector = detector;
        this.handler = handler;
        this.gameState = gameState;
        this.houseProtection = houseProtection;
        this.zombieRenderer = zombieRenderer;
    }

    public void spawnZombiesForWave(List<Zombie> zombies) {
        for (Zombie z : zombies) {
            if (z != null && !z.isDead()) {
                int row = z.getY();
                int col = z.getX();
                gameBoard.placeZombie(z, row, col);
                zombieRenderer.renderZombie(z);
                LOGGER.info("Placed zombie " + z.getName() + " at (" + row + "," + col + ")");
            }
        }
    }

    public void moveZombies() {
        if (gameState.isGameOver() || gameBoard == null) return;
        List<Zombie> zombies = gameBoard.getZombies();
        for (Zombie z : zombies) {
            if (z == null || z.isDead()) continue;

            int row = z.getY();
            int col = z.getX();
            Plant plant = detector.detectZombiePlantCollision(z);
            if (plant != null) {
                handler.handleZombiePlantCollision(z, plant);
                zombieRenderer.updateZombieState(z, ZombieGifManager.ZombieState.ATTACKING);
                continue;
            }

            int newCol = Math.max(0, col - 1); // Move left
            if (col > 0 && gameBoard.isValidPosition(row, newCol) && gameBoard.getCell(row, newCol).getZombie() == null) {
                gameBoard.getCell(row, col).setZombie(null);
                z.setX(newCol);
                gameBoard.placeZombie(z, row, newCol);
                zombieRenderer.moveZombie(z, newCol);
                if (z instanceof NewspaperZombie newspaperZombie && newspaperZombie.isEnraged()) {
                    zombieRenderer.updateZombieState(z, ZombieGifManager.ZombieState.ATTACKING);
                } else {
                    zombieRenderer.updateZombieState(z, ZombieGifManager.ZombieState.WALKING);
                }
            } else if (col == 0) {
                houseProtection.reduceProtection(z.getAttackPower());
                gameBoard.removeZombie(row, col);
                zombieRenderer.removeZombie(z);
                if (houseProtection.isDepleted()) {
                    gameState.setGameOver(true);
                }
            }
        }
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public ZombieRenderer getZombieRenderer() {
        return zombieRenderer;
    }
}