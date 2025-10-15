package org.example.plants_vs_zombies1.model.collision;

import org.example.plants_vs_zombies1.model.board.Cell;
import org.example.plants_vs_zombies1.model.board.GameBoard;
import org.example.plants_vs_zombies1.model.plant.Plant;
import org.example.plants_vs_zombies1.model.plant.WarriorPlant;
import org.example.plants_vs_zombies1.model.zombie.Zombie;

import java.util.ArrayList;
import java.util.List;

public class CollisionDetector {
    private final GameBoard gameBoard;

    public CollisionDetector(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public List<Zombie> detectBulletCollisions(WarriorPlant plant, int row, int bulletCol) {
        List<Zombie> hitZombies = new ArrayList<>();
        Cell cell = gameBoard.getCell(row, bulletCol);
        if (cell != null) {
            Zombie zombie = cell.getZombie();
            if (zombie != null && !zombie.isDead()) {
                hitZombies.add(zombie);
            }
        }
        return hitZombies;
    }

    public Plant detectZombiePlantCollision(Zombie zombie) {
        if (zombie == null || zombie.isDead()) return null;
        int row = zombie.getY();
        int col = zombie.getX();
        Cell cell = gameBoard.getCell(row, col);
        if (cell != null) {
            return cell.getPlant();
        }
        return null;
    }

    public List<Zombie> detectExplosionCollisions(int row, int col, int range) {
        List<Zombie> hitZombies = new ArrayList<>();
        int startRow = Math.max(0, row - range);
        int endRow = Math.min(gameBoard.getRows() - 1, row + range);
        int startCol = Math.max(0, col - range);
        int endCol = Math.min(gameBoard.getCols() - 1, col + range);

        for (int i = startRow; i <= endRow; i++) {
            for (int j = startCol; j <= endCol; j++) {
                Cell cell = gameBoard.getCell(i, j);
                if (cell != null) {
                    Zombie zombie = cell.getZombie();
                    if (zombie != null && !zombie.isDead()) {
                        hitZombies.add(zombie);
                    }
                }
            }
        }
        return hitZombies;
    }

    public List<Zombie> detectFullBoardCollisions() {
        List<Zombie> hitZombies = new ArrayList<>();
        for (int i = 0; i < gameBoard.getRows(); i++) {
            for (int j = 0; j < gameBoard.getCols(); j++) {
                Cell cell = gameBoard.getCell(i, j);
                if (cell != null) {
                    Zombie zombie = cell.getZombie();
                    if (zombie != null && !zombie.isDead()) {
                        hitZombies.add(zombie);
                    }
                }
            }
        }
        return hitZombies;
    }

    public boolean isZombieNear(int plantRow, int plantCol) {
        List<Zombie> zombiesInRow = gameBoard.getZombiesInRow(plantRow);
        for (Zombie zombie : zombiesInRow) {
            if (zombie.getX() >= plantCol && !zombie.isDead()) {
                return true;
            }
        }
        return false;
    }
}