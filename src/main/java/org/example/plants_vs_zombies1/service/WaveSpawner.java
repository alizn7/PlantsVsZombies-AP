package org.example.plants_vs_zombies1.service;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import org.example.plants_vs_zombies1.model.board.GameBoard;
import org.example.plants_vs_zombies1.model.zombie.Zombie;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class WaveSpawner {
    private static final Logger LOGGER = Logger.getLogger(WaveSpawner.class.getName());
    private Timeline spawnTimeline;
    private final GameBoard board;
    private final ZombieService zombieService;
    private final ZombieRenderer zombieRenderer;
    private final Random random = new Random();

    public WaveSpawner(GameBoard board, ZombieService zombieService, ZombieRenderer zombieRenderer) {
        this.board = board;
        this.zombieService = zombieService;
        this.zombieRenderer = zombieRenderer;
        recreateScheduler();
    }

    public void spawnWave(List<Zombie> zombies, int baseDelaySeconds, Runnable onWaveFinished, boolean isFinalWave) {
        if (zombies.isEmpty()) {
            LOGGER.warning("No zombies to spawn in wave");
            if (onWaveFinished != null) {
                Platform.runLater(onWaveFinished::run);
            }
            return;
        }
        if (spawnTimeline != null) {
            spawnTimeline.stop();
        }
        if (board == null) {
            LOGGER.severe("GameBoard is null in WaveSpawner");
            return;
        }
        spawnTimeline = new Timeline();
        int[] currentZombieIndex = {0};

        double delay = isFinalWave ? 0 : baseDelaySeconds;
        for (Zombie zombie : zombies) {
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(delay), e -> {
                if (currentZombieIndex[0] >= zombies.size()) return;
                Zombie currentZombie = zombies.get(currentZombieIndex[0]);
                int row = findValidRow();
                int col = board.getCols() - 1; // Always spawn in the last column (8)
                if (row != -1) {
                    spawnSingleZombie(currentZombie, row, col);
                } else {
                    LOGGER.warning("Could not find valid row for zombie " + currentZombie.getName());
                }
                currentZombieIndex[0]++;
            });
            spawnTimeline.getKeyFrames().add(keyFrame);
            if (!isFinalWave) {
                delay += 5 + random.nextDouble() * 5; // 5-10 seconds
            }
        }

        spawnTimeline.setOnFinished(e -> {
            if (onWaveFinished != null) {
                handleWaveCompletion(onWaveFinished, isFinalWave);
            }
        });
        spawnTimeline.play();
        LOGGER.fine(() -> String.format("Started spawnWave with %d zombies, isFinalWave: %b, baseDelay: %d seconds",
                zombies.size(), isFinalWave, baseDelaySeconds));
    }

    private int findValidRow() {
        int maxAttempts = board.getRows() * 2;
        int[] availableRows = new int[board.getRows()];
        int availableCount = 0;
        int col = board.getCols() - 1; // Column 8
        for (int row = 0; row < board.getRows(); row++) {
            if (board.isValidPosition(row, col) && board.getCell(row, col).getZombie() == null) {
                availableRows[availableCount++] = row;
            }
        }
        if (availableCount == 0) {
            LOGGER.warning("No valid row found for zombie spawn after checking all rows");
            return -1;
        }
        return availableRows[random.nextInt(availableCount)];
    }

    private void spawnSingleZombie(Zombie zombie, int row, int col) {
        if (row == -1) {
            LOGGER.severe("Cannot spawn zombie " + zombie.getName() + ": No valid row available");
            return;
        }
        try {
            Platform.runLater(() -> {
                zombie.setX(col);
                zombie.setY(row);
                board.placeZombie(zombie, row, col);
                LOGGER.info("Placed zombie " + zombie.getName() + " at (" + row + "," + col + ")");
                zombieService.spawnZombiesForWave(List.of(zombie));
            });
        } catch (Exception ex) {
            LOGGER.severe("Error spawning zombie " + zombie.getName() + ": " + ex.getMessage());
        }
    }

    private void handleWaveCompletion(Runnable onWaveFinished, boolean isFinalWave) {
        Platform.runLater(() -> {
            if (!isFinalWave) {
                Timeline checkZombies = new Timeline(new KeyFrame(Duration.seconds(5), checkEvent -> {
                    if (board.getZombies().isEmpty()) {
                        LOGGER.fine("All zombies dead, triggering onWaveFinished");
                        onWaveFinished.run();
                    }
                }));
                checkZombies.setCycleCount(Timeline.INDEFINITE);
                checkZombies.play();
            } else {
                onWaveFinished.run();
            }
        });
    }

    private void recreateScheduler() {
        if (spawnTimeline != null) {
            spawnTimeline.stop();
        }
        spawnTimeline = new Timeline();
    }

    public void stop() {
        if (spawnTimeline != null) {
            spawnTimeline.stop();
            LOGGER.info("WaveSpawner stopped");
        }
    }

    public boolean isShutdown() {
        return spawnTimeline == null || spawnTimeline.getStatus() == Timeline.Status.STOPPED;
    }
}