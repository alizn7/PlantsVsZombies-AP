package org.example.plants_vs_zombies1.model;

import org.example.plants_vs_zombies1.model.enums.GameMode;
import org.example.plants_vs_zombies1.model.enums.PlantType;

import java.util.Arrays;
import java.util.List;

public class Level {
    private int levelNumber;
    private GameMode gameMode;
    private List<PlantType> availablePlants;

    public Level(int levelNumber, GameMode gameMode) {
        this.levelNumber = levelNumber;
        this.gameMode = gameMode;
        this.availablePlants = initializeAvailablePlants();
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public List<PlantType> getAvailablePlants() {
        return availablePlants;
    }

    private List<PlantType> initializeAvailablePlants() {
        if (gameMode == GameMode.DAY) {
            switch (levelNumber) {
                case 1:
                    return Arrays.asList(PlantType.PEASHOOTER, PlantType.SUNFLOWER);
                case 2:
                    return Arrays.asList(PlantType.PEASHOOTER, PlantType.SUNFLOWER, PlantType.CHERRY_BOMB, PlantType.WALL_NUT);
                case 3:
                    return Arrays.asList(PlantType.PEASHOOTER, PlantType.SUNFLOWER, PlantType.CHERRY_BOMB, PlantType.WALL_NUT, PlantType.SNOW_PEA, PlantType.REPEATER);
                default:
                    return Arrays.asList(PlantType.PEASHOOTER, PlantType.SUNFLOWER);
            }
        } else { // GameMode.NIGHT
            switch (levelNumber) {
                case 4:
                    return Arrays.asList(PlantType.PUFF_SHROOM, PlantType.SUN_SHROOM);
                case 5:
                    return Arrays.asList(PlantType.PUFF_SHROOM, PlantType.SUN_SHROOM, PlantType.FUME_SHROOM, PlantType.SCAREDY_SHROOM);
                case 6:
                    return Arrays.asList(PlantType.PUFF_SHROOM, PlantType.SUN_SHROOM, PlantType.FUME_SHROOM, PlantType.SCAREDY_SHROOM, PlantType.ICE_SHROOM, PlantType.DOOM_SHROOM);
                default:
                    return Arrays.asList(PlantType.PUFF_SHROOM, PlantType.SUN_SHROOM);
            }
        }
    }
}