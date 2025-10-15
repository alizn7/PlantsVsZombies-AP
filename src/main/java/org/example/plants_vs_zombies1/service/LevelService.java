package org.example.plants_vs_zombies1.service;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import org.example.plants_vs_zombies1.model.GameState;
import org.example.plants_vs_zombies1.model.Wave;
import org.example.plants_vs_zombies1.model.zombie.Zombie;
import org.example.plants_vs_zombies1.model.zombie.ZombieFactory;
import org.example.plants_vs_zombies1.model.enums.ZombieType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelService {
    private final GameState gameState;
    private final ZombieService zombieService;
    private final WaveSpawner waveSpawner;
    private Wave currentWave;
    private int currentWaveNumber;
    private GameStatusListener gameStatusListener;

    public LevelService(GameState gameState, ZombieService zombieService) {
        this.gameState = gameState;
        this.zombieService = zombieService;
        this.waveSpawner = new WaveSpawner(zombieService.getGameBoard(), zombieService, zombieService.getZombieRenderer());
        this.currentWaveNumber = 0;
        int waves = calculateWaves(gameState.getLevelNumber());
        gameState.setRemainingWaves(waves);
    }

    public void setGameStatusListener(GameStatusListener listener) {
        this.gameStatusListener = listener;
    }


    private int calculateWaves(int levelNumber) {
        return switch (levelNumber) {
            case 1 -> 2;
            case 2, 3 -> 4;
            case 4, 5, 6 -> 4;
            default -> 2;
        };
    }


    public void startWave() {
        if (gameState.getRemainingWaves() <= 0 || gameState.isGameOver()) {
            return;
        }

        currentWaveNumber++;
        currentWave = new Wave(currentWaveNumber, gameState.getLevelNumber());
        gameState.decrementWaves();

        List<Zombie> zombies = createZombiesForWave(currentWave);
        waveSpawner.spawnWave(zombies, 5, this::scheduleNextWaveCheck, currentWave.isFinalWave());

        if (gameStatusListener != null) {
            Platform.runLater(() -> gameStatusListener.onWaveStarted(currentWaveNumber));
        }
    }


    private List<Zombie> createZombiesForWave(Wave wave) {
        List<Zombie> zombies = new ArrayList<>();
        for (ZombieType type : wave.getZombies()) {
            int row = getRandomSpawnRow();
            int col = getSpawnColumn();
            zombies.add(ZombieFactory.createZombie(type, row, col));
        }
        return zombies;
    }


    private void scheduleNextWaveCheck() {
        if (!currentWave.isFinalWave() && gameState.getRemainingWaves() > 0 && !gameState.isGameOver()) {
            Timeline checkZombies = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
                if (zombieService.getGameBoard().getZombies().isEmpty()) {
                    startWave();
                }
            }));
            checkZombies.setCycleCount(1);
            checkZombies.play();
        }
    }


    private int getRandomSpawnRow() {
        if (zombieService.getGameBoard() == null) {
            throw new IllegalStateException("Game board is not initialized");
        }
        return new Random().nextInt(zombieService.getGameBoard().getRows());
    }


    private int getSpawnColumn() {
        if (zombieService.getGameBoard() == null) {
            throw new IllegalStateException("Game board is not initialized");
        }
        return zombieService.getGameBoard().getCols() - 1;
    }


    public boolean hasMoreWaves() {
        return gameState.getRemainingWaves() > 0;
    }


    public void stop() {
        waveSpawner.stop();
    }
}