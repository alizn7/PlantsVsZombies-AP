package org.example.plants_vs_zombies1.model;

import org.example.plants_vs_zombies1.util.exceptions.InsufficientSunException;

public class GameState {
    private int sunPoints;
    private int levelNumber;
    private int remainingWaves;
    private boolean isDayMode;
    private boolean isGameOver;

    public GameState(int levelNumber, boolean isDayMode) {
        this.sunPoints = 50;
        this.levelNumber = levelNumber;
        this.remainingWaves = calculateWaves(levelNumber);
        this.isDayMode = isDayMode;
        this.isGameOver = false;
    }

    public int getSunPoints() {
        return sunPoints;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public int getRemainingWaves() {
        return remainingWaves;
    }

    public boolean isDayMode() {
        return isDayMode;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setSunPoints(int sunPoints) {
        this.sunPoints = sunPoints;
    }

    public void setDayMode(boolean dayMode) {
        isDayMode = dayMode;
    }

    public void addSunPoints(int points) {
        this.sunPoints += points;
    }

    public void spendSunPoints(int points) throws InsufficientSunException {
        if (sunPoints < points) {
            throw new InsufficientSunException("Not enough sun points to plant!");
        }
        this.sunPoints -= points;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public void setRemainingWaves(int remainingWaves) {
        this.remainingWaves = remainingWaves;
    }

    public void decrementWaves() {
        if (remainingWaves > 0) {
            remainingWaves--;
            System.out.println("Waves remaining: " + remainingWaves);
        } else {
            System.out.println("No remaining waves.");
        }
    }

    public void setGameOver(boolean gameOver) {
        this.isGameOver = gameOver;
        if (gameOver) {
            System.out.println("Game Over!");
        }
    }

    public int calculateWaves(int levelNumber) {
        switch (levelNumber) {
            case 1: return 2;
            case 2: return 3;
            case 3: return 4;
            case 4: return 3;
            case 5: return 4;
            case 6: return 5;
            default: return 2;
        }
    }

}
