package org.example.plants_vs_zombies1.model.board;

import org.example.plants_vs_zombies1.model.Bullet;
import org.example.plants_vs_zombies1.model.Sun;
import org.example.plants_vs_zombies1.model.enums.CellType;
import org.example.plants_vs_zombies1.model.plant.Plant;
import org.example.plants_vs_zombies1.model.zombie.Zombie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameBoard {
    private Cell[][] grid;
    private int rows = 5;
    private int cols = 9;
    private final List<Bullet> bullets;
    private final List<Sun> suns = new ArrayList<>();

    public GameBoard() {
        grid = new Cell[rows][cols];
        bullets = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
    }

    public boolean placePlant(Plant plant, int row, int col) {
        if (isValidPosition(row, col) && grid[row][col].getPlant() == null) {
            grid[row][col].setPlant(plant);
            plant.setBoard(this);
            updateCellType(row, col);
            return true;
        }
        return false;
    }

    public boolean placeZombie(Zombie zombie, int row, int col) {
        if (isValidPosition(row, col)) {
            grid[row][col].setZombie(zombie);
            updateCellType(row, col);
            return true;
        }
        return false;
    }

    public void placeSun(Sun sun, int row, int col) {
        if (isValidPosition(row, col)) {
            sun.setY(row);
            sun.setX(col);
            suns.add(sun);
            grid[row][col].addSun(sun);
            updateCellType(row, col);
        } else {
            System.err.println("Invalid position for sun at (" + row + "," + col + ")");
        }
    }

    public void addBullet(Bullet bullet) {
        if (bullet != null) {
            bullets.add(bullet);
        }
    }

    public int collectSun(int row, int col) {
        int total = 0;
        if (!isValidPosition(row, col)) return total;
        Iterator<Sun> iterator = suns.iterator();
        while (iterator.hasNext()) {
            Sun sun = iterator.next();
            if (sun.getX() == col && sun.getY() == row) {
                total += sun.getValue();
                iterator.remove();
                grid[row][col].getSuns().remove(sun);
            }
        }
        updateCellType(row, col);
        return total;
    }

    public void removePlant(int row, int col) {
        if (isValidPosition(row, col) && grid[row][col].getPlant() != null) {
            Plant plant = grid[row][col].getPlant();
            grid[row][col].setPlant(null);
            updateCellType(row, col);
        }
    }

    public void removeZombie(int row, int col) {
        if (isValidPosition(row, col) && grid[row][col].getZombie() != null) {
            System.out.println("Removed zombie " + grid[row][col].getZombie().getName() + " from (" + row + "," + col + ")");
            grid[row][col].setZombie(null);
            updateCellType(row, col);
        }
    }

    public void removeBullet(Bullet bullet) {
        if (bullet != null) {
            bullets.remove(bullet);
        }
    }

    private void updateCellType(int row, int col) {
        if (!isValidPosition(row, col)) return;
        Cell cell = grid[row][col];
        if (cell.getPlant() != null && cell.getZombie() != null) {
            cell.setType(CellType.PLANT_AND_ZOMBIE);
        } else if (cell.getPlant() != null) {
            cell.setType(CellType.PLANT);
        } else if (cell.getZombie() != null) {
            cell.setType(CellType.ZOMBIE);
        } else if (!cell.getSuns().isEmpty()) {
            cell.setType(CellType.SUN);
        } else {
            cell.setType(CellType.EMPTY);
        }
    }

    public Cell getCell(int row, int col) {
        if (isValidPosition(row, col)) {
            return grid[row][col];
        }
        System.err.println("Invalid cell access at (" + row + "," + col + ")");
        return null;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public List<Zombie> getZombies() {
        List<Zombie> zombies = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (isValidPosition(row, col)) {
                    Zombie zombie = grid[row][col].getZombie();
                    if (zombie != null && !zombie.isDead()) {
                        zombies.add(zombie);
                    }
                }
            }
        }
        return zombies;
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public List<Zombie> getZombiesInRow(int row) {
        List<Zombie> result = new ArrayList<>();
        if (!isValidPosition(row, 0)) return result;
        for (int col = 0; col < cols; col++) {
            if (isValidPosition(row, col)) {
                Zombie zombie = grid[row][col].getZombie();
                if (zombie != null && !zombie.isDead()) {
                    result.add(zombie);
                }
            }
        }
        return result;
    }
}