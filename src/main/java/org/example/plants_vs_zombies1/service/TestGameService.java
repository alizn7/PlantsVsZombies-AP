package org.example.plants_vs_zombies1.service;

import org.example.plants_vs_zombies1.model.GameState;
import org.example.plants_vs_zombies1.model.Sun;
import org.example.plants_vs_zombies1.model.board.Cell;
import org.example.plants_vs_zombies1.model.board.GameBoard;
import org.example.plants_vs_zombies1.model.enums.PlantType;
import org.example.plants_vs_zombies1.model.enums.ZombieType;
import org.example.plants_vs_zombies1.model.plant.*;
import org.example.plants_vs_zombies1.model.zombie.*;

import java.util.Random;

public class TestGameService {
    private final GameBoard board;
    private final GameState gameState;

    public TestGameService(GameBoard board, GameState gameState) {
        this.board = board;
        this.gameState = gameState;
    }

    public void generateSunRandomly() {
        try {
            if (gameState.isDayMode()) {
                Random random = new Random();
                int row = random.nextInt(board.getRows());
                int col = random.nextInt(board.getCols());
                Sun sun = new Sun(25, row, col);
                board.placeSun(sun, row, col);
                System.out.println("☀ Sun appeared at (" + row + "," + col + ")");
            }
        } catch (Exception e) {
            System.err.println("Error generating random sun: " + e.getMessage());
        }
    }

    public void collectAllSuns() {
        try {
            int collected = 0;
            for (int row = 0; row < board.getRows(); row++) {
                for (int col = 0; col < board.getCols(); col++) {
                    collected += board.collectSun(row, col);
                }
            }
            if (collected > 0) {
                gameState.addSunPoints(collected);
                System.out.println("🟡 Collected " + collected + " sun(s), Total: " + gameState.getSunPoints());
            }
        } catch (Exception e) {
            System.err.println("Error collecting suns: " + e.getMessage());
        }
    }

    public boolean plant(PlantType type, int row, int col) {
        try {
            if (!board.isValidPosition(row, col)) {
                System.out.println("❌ Invalid position (" + row + "," + col + ") for planting " + type);
                return false;
            }

            Cell cell = board.getCell(row, col);
            if (cell == null || cell.getPlant() != null) {
                System.out.println("❌ Cell (" + row + "," + col + ") is occupied or null for planting " + type);
                return false;
            }

            Plant plant = createPlant(type, row, col);
            if (plant == null) {
                System.out.println("❌ Invalid plant type: " + type);
                return false;
            }

            if (gameState.getSunPoints() < plant.getCost()) {
                System.out.println("❌ Not enough sun to plant " + plant.getName() + " (Needed: " + plant.getCost() + ", Available: " + gameState.getSunPoints() + ")");
                return false;
            }

            boolean success = board.placePlant(plant, row, col);
            if (success) {
                gameState.addSunPoints(-plant.getCost());
                System.out.println("✅ Planted " + plant.getName() + " at (" + row + "," + col + "), Remaining Sun: " + gameState.getSunPoints());
            } else {
                System.out.println("❌ Failed to plant " + plant.getName() + " at (" + row + "," + col + ")");
            }
            return success;
        } catch (Exception e) {
            System.err.println("Error planting " + type + ": " + e.getMessage());
            return false;
        }
    }

    public void addZombieBasic(int row, int col) {
        try {
            if (board.isValidPosition(row, col)) {
                board.placeZombie(new BasicZombie(row, col, 25.0), row, col);
                System.out.println("🧟 BasicZombie added at (" + row + "," + col + ")");
            } else {
                System.out.println("❌ Invalid position (" + row + "," + col + ") for BasicZombie");
            }
        } catch (Exception e) {
            System.err.println("Error adding BasicZombie: " + e.getMessage());
        }
    }

    public void addZombieConehead(int row, int col) {
        try {
            if (board.isValidPosition(row, col)) {
                board.placeZombie(new ConeheadZombie(row, col, 125.0), row, col);
                System.out.println("🧟 ConeheadZombie added at (" + row + "," + col + ")");
            } else {
                System.out.println("❌ Invalid position (" + row + "," + col + ") for ConeheadZombie");
            }
        } catch (Exception e) {
            System.err.println("Error adding ConeheadZombie: " + e.getMessage());
        }
    }

    public void addZombieFlag(int row, int col) {
        try {
            if (board.isValidPosition(row, col)) {
                board.placeZombie(new FlagZombie(row, col, 250.0), row, col);
                System.out.println("🚩 FlagZombie added at (" + row + "," + col + ")");
            } else {
                System.out.println("❌ Invalid position (" + row + "," + col + ") for FlagZombie");
            }
        } catch (Exception e) {
            System.err.println("Error adding FlagZombie: " + e.getMessage());
        }
    }

    public void addZombieScreenDoor(int row, int col) {
        try {
            if (board.isValidPosition(row, col)) {
                board.placeZombie(new ScreenDoorZombie(row, col, 250.0), row, col);
                System.out.println("🛡 ScreenDoorZombie added at (" + row + "," + col + ")");
            } else {
                System.out.println("❌ Invalid position (" + row + "," + col + ") for ScreenDoorZombie");
            }
        } catch (Exception e) {
            System.err.println("Error adding ScreenDoorZombie: " + e.getMessage());
        }
    }

    public void addZombieNewspaper(int row, int col) {
        try {
            if (board.isValidPosition(row, col)) {
                board.placeZombie(new NewspaperZombie(row, col, 250.0), row, col);
                System.out.println("📰 NewspaperZombie added at (" + row + "," + col + ")");
            } else {
                System.out.println("❌ Invalid position (" + row + "," + col + ") for NewspaperZombie");
            }
        } catch (Exception e) {
            System.err.println("Error adding NewspaperZombie: " + e.getMessage());
        }
    }

    public void addSunPoints(int points) {
        try {
            gameState.addSunPoints(points);
            System.out.println("☀ Manually added " + points + " sun(s), Total: " + gameState.getSunPoints());
        } catch (Exception e) {
            System.err.println("Error adding sun points: " + e.getMessage());
        }
    }

    public int getTotalSunPoints() {
        return gameState.getSunPoints();
    }

    private Plant createPlant(PlantType type, int row, int col) {
        try {
            switch (type) {
                case PEASHOOTER:
                    return new Peashooter(col, row);
                case SUNFLOWER:
                    return new Sunflower(col, row);
                case REPEATER:
                    return new Repeater(col, row);
                case SNOW_PEA:
                    return new SnowPea(col, row);
                case WALL_NUT:
                    return new WallNut(col, row);
                case PUFF_SHROOM:
                    return new PuffShroom(col, row);
                case SUN_SHROOM:
                    return new SunShroom(col, row);
                case FUME_SHROOM:
                    return new FumeShroom(col, row);
                case ICE_SHROOM:
                    return new IceShroom(col, row);
                case DOOM_SHROOM:
                    return new DoomShroom(col, row);
                case SCAREDY_SHROOM:
                    return new ScaredyShroom(col, row);
                case CHERRY_BOMB:
                    return new CherryBomb(col, row);
                default:
                    return null;
            }
        } catch (Exception e) {
            System.err.println("Error creating plant " + type + ": " + e.getMessage());
            return null;
        }
    }

    public void generateAndCollectSun() {
        try {
            generateSunRandomly();

            collectAllSuns();
        } catch (Exception e) {
            System.err.println("Error generating and collecting sun: " + e.getMessage());
        }
    }

    public void addZombieRandomly() {
        try {
            Random random = new Random();
            int row = random.nextInt(board.getRows());
            int col = board.getCols() - 1;
            ZombieType[] zombieTypes = ZombieType.values();
            ZombieType randomZombieType = zombieTypes[random.nextInt(zombieTypes.length)];

            switch (randomZombieType) {
                case BASIC_ZOMBIE:
                    addZombieBasic(row, col);
                    break;
                case CONEHEAD_ZOMBIE:
                    addZombieConehead(row, col);
                    break;
                case FLAG_ZOMBIE:
                    addZombieFlag(row, col);
                    break;
                case SCREEN_DOOR_ZOMBIE:
                    addZombieScreenDoor(row, col);
                    break;
                case NEWSPAPER_ZOMBIE:
                    addZombieNewspaper(row, col);
                    break;
                default:
                    System.out.println("❌ No suitable zombie found.");
            }
        } catch (Exception e) {
            System.err.println("Error adding zombie randomly: " + e.getMessage());
        }
    }


}

