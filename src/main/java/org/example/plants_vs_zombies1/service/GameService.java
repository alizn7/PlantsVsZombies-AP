package org.example.plants_vs_zombies1.service;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import org.example.plants_vs_zombies1.model.*;
import org.example.plants_vs_zombies1.model.board.GameBoard;
import org.example.plants_vs_zombies1.model.collision.CollisionDetector;
import org.example.plants_vs_zombies1.model.collision.CollisionHandler;
import org.example.plants_vs_zombies1.model.plant.*;
import org.example.plants_vs_zombies1.model.zombie.*;
import org.example.plants_vs_zombies1.model.enums.ZombieType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameService {
    private final GameBoard board;
    private final CollisionDetector detector;
    private final CollisionHandler handler;
    private final GameState gameState;
    private final HouseProtection houseProtection;
    private final Level level;
    private final ZombieService zombieServiceInstance;
    private final ZombieRenderer zombieRenderer;
    private final List<Wave> waveList = new ArrayList<>();
    private Wave currentWave;
    private int currentWaveIndex = 0;
    private boolean isGameOver = false;
    private GameStatusListener listener;
    private final ScheduledService<Void> zombieScheduledService;
    private final ScheduledService<Void> plantService;
    private final ScheduledService<Void> sunService;
    private final ScheduledService<Void> statusCheckService;
    private final WaveSpawner waveSpawner;
    private final AudioClip sunCollectSound;
    private final Random random = new Random();
    private final LevelService levelService;

    public GameService(GameBoard board,
                       CollisionDetector detector,
                       CollisionHandler handler,
                       GameState gameState,
                       HouseProtection houseProtection,
                       Level level,
                       ZombieRenderer zombieRenderer) {
        this.board = board;
        this.detector = detector;
        this.handler = handler;
        this.gameState = gameState;
        this.houseProtection = houseProtection;
        this.level = level;
        this.zombieRenderer = zombieRenderer;
        this.zombieServiceInstance = new ZombieService(board, detector, handler, gameState, houseProtection, zombieRenderer);
        this.waveSpawner = new WaveSpawner(board, zombieServiceInstance, zombieRenderer);
        this.levelService = new LevelService(gameState, zombieServiceInstance);
        initializeWaves();

        zombieScheduledService = createScheduledService(zombieServiceInstance::moveZombies, Duration.seconds(5));
        plantService = createScheduledService(this::triggerPlants, Duration.millis(1500));
        sunService = createScheduledService(this::generateSunRandomly, Duration.seconds(8));
        statusCheckService = createScheduledService(this::checkGameStatus, Duration.millis(1000));

        AudioClip tempSound = null;
        try {
            java.net.URL soundUrl = getClass().getResource("/sounds/sun_collect.mp3");
            if (soundUrl != null) {
                tempSound = new AudioClip(soundUrl.toString());
            } else {
                System.err.println("Warning: sun_collect.mp3 not found in resources/sounds!");
            }
        } catch (Exception e) {
            System.err.println("Error loading sun_collect.mp3: " + e.getMessage());
        }
        sunCollectSound = tempSound;
    }


    public void setGameStatusListener(GameStatusListener listener) {
        this.listener = listener;
        levelService.setGameStatusListener(listener);
    }


    public void startServices() {
        if (zombieScheduledService.getState() == ScheduledService.State.READY) {
            zombieScheduledService.start();
        }
        if (plantService.getState() == ScheduledService.State.READY) {
            plantService.start();
        }
        if (sunService.getState() == ScheduledService.State.READY) {
            sunService.start();
        }
        if (statusCheckService.getState() == ScheduledService.State.READY) {
            statusCheckService.start();
        }
    }


    private void initializeWaves() {
        int levelNumber = level.getLevelNumber();
        int waveCount = switch (levelNumber) {
            case 1 -> 2;
            case 2, 3 -> 4;
            case 4, 5, 6 -> 4;
            default -> 2;
        };
        for (int i = 0; i < waveCount; i++) {
            Wave wave = new Wave(i + 1, levelNumber);
            waveList.add(wave);
        }
    }

    private ScheduledService<Void> createScheduledService(Runnable task, Duration period) {
        ScheduledService<Void> service = new ScheduledService<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        task.run();
                        return null;
                    }
                };
            }
        };
        service.setPeriod(period);
        return service;
    }


    public void startNextWave() {
        if (currentWaveIndex >= waveList.size() || isGameOver || gameState.isGameOver()) {
            checkWinCondition();
            return;
        }
        if (!waveSpawner.isShutdown() && !board.getZombies().isEmpty()) {
            Timeline retryTimeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> startNextWave()));
            retryTimeline.play();
            return;
        }
        currentWave = waveList.get(currentWaveIndex++);
        gameState.decrementWaves();
        if (listener != null) {
            Platform.runLater(() -> listener.onWaveStarted(currentWave.getWaveNumber()));
        }
        List<Zombie> zombies = createZombiesForWave(currentWave);
        waveSpawner.spawnWave(zombies, 5, this::startNextWave, currentWave.isFinalWave());
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


    private int getRandomSpawnRow() {
        if (board == null) {
            throw new IllegalStateException("Game board is not initialized");
        }
        return random.nextInt(board.getRows());
    }


    private int getSpawnColumn() {
        if (board == null) {
            throw new IllegalStateException("Game board is not initialized");
        }
        return board.getCols() - 1;
    }


    public void stopGame() {
        isGameOver = true;
        gameState.setGameOver(true);
        zombieScheduledService.cancel();
        plantService.cancel();
        sunService.cancel();
        statusCheckService.cancel();
        waveSpawner.stop();
        levelService.stop();
        zombieScheduledService.reset();
        plantService.reset();
        sunService.reset();
        statusCheckService.reset();
    }


    public int collectSun(int row, int col) {
        int sunValue = board.collectSun(row, col);
        if (sunValue > 0) {
            gameState.addSunPoints(sunValue);
            if (sunCollectSound != null) {
                Platform.runLater(() -> sunCollectSound.play());
            }
        }
        return sunValue;
    }


    private void triggerPlants() {
        if (isGameOver || gameState.isGameOver()) return;

        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                Plant plant = board.getCell(row, col).getPlant();
                if (plant == null || plant.isDead()) continue;

                if (plant instanceof WarriorPlant warriorPlant) {
                    List<Zombie> zombiesInRow = board.getZombiesInRow(row);
                    if (!zombiesInRow.isEmpty()) {
                        List<Bullet> bullets = warriorPlant.shoot();
                        for (Bullet bullet : bullets) {
                            if (bullet != null) {
                                board.addBullet(bullet);
                                System.out.println("Plant " + plant.getName() + " shot bullet at (" + row + "," + col + ")");
                            }
                        }
                    }
                }
            }
        }
    }


    private void generateSunRandomly() {
        if (isGameOver || !gameState.isDayMode() || gameState.isGameOver()) return;
        int r = random.nextInt(board.getRows()), c = random.nextInt(board.getCols());
        Sun sun = new Sun(25, r, c);
        Platform.runLater(() -> {
            if (board.isValidPosition(r, c) && !gameState.isGameOver()) {
                board.placeSun(sun, r, c);
                if (listener != null) listener.onSunGenerated(sun.getValue());
                System.out.println("Placed sun at (" + r + "," + c + ")");
            }
        });
    }


    private void checkGameStatus() {
        if (isGameOver || gameState.isGameOver()) return;

        if (houseProtection.isDepleted()) {
            isGameOver = true;
            gameState.setGameOver(true);
            notifyGameOver(false);
            return;
        }

        if (currentWaveIndex >= waveList.size() && board.getZombies().isEmpty() && gameState.getRemainingWaves() <= 0) {
            isGameOver = true;
            gameState.setGameOver(true);
            notifyGameOver(true);
            return;
        }
    }


    private void checkWinCondition() {
        if (!isGameOver && !gameState.isGameOver() && currentWaveIndex >= waveList.size() && board.getZombies().isEmpty() && gameState.getRemainingWaves() <= 0) {
            isGameOver = true;
            gameState.setGameOver(true);
            notifyGameOver(true);
        }
    }


    private void notifyGameOver(boolean win) {
        if (listener != null) {
            Platform.runLater(() -> {
                listener.onGameOver(win);
                System.out.println("Notified game over: " + (win ? "Win" : "Loss"));
            });
        }
    }
}