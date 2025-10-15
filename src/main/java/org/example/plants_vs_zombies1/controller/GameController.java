package org.example.plants_vs_zombies1.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.plants_vs_zombies1.model.*;
import org.example.plants_vs_zombies1.model.board.GameBoard;
import org.example.plants_vs_zombies1.model.collision.CollisionDetector;
import org.example.plants_vs_zombies1.model.collision.CollisionHandler;
import org.example.plants_vs_zombies1.model.enums.GameMode;
import org.example.plants_vs_zombies1.model.enums.PlantType;
import org.example.plants_vs_zombies1.model.plant.Peashooter;
import org.example.plants_vs_zombies1.model.plant.Plant;
import org.example.plants_vs_zombies1.model.plant.Sunflower;
import org.example.plants_vs_zombies1.model.zombie.Zombie;
import org.example.plants_vs_zombies1.service.*;
import org.example.plants_vs_zombies1.util.PlantGifManager;
import org.example.plants_vs_zombies1.util.SoundManager;
import org.example.plants_vs_zombies1.util.exceptions.InsufficientSunException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class GameController {
    @FXML private AnchorPane rootPane;
    @FXML private ImageView backgroundImage;
    @FXML private GridPane gameGrid;
    @FXML private Label sunLabel;
    @FXML private Label statusLabel;
    @FXML private Button backButton;
    @FXML private VBox plantSelectionBox;

    private GameBoard board;
    private GameState gameState;
    private HouseProtection houseProtection;
    private Level level;
    private CollisionDetector detector;
    private CollisionHandler handler;
    private SunService sunService;
    private PlantService plantService;
    private ZombieService zombieService;
    private LevelService levelService;
    private GameService gameService;
    private ZombieRenderer zombieRenderer;
    private PlantType selectedPlant;
    private Timeline uiUpdater;
    private Timeline sunGenerator;
    private Timeline waveStarter;
    private Timeline bulletUpdater;
    private boolean isPaused = false;
    private final Map<String, ImageView> sunViews = new HashMap<>();
    private final Map<Bullet, ImageView> bulletViews = new ConcurrentHashMap<>();

    private static final double PLANT_WIDTH = 80;
    private static final double PLANT_HEIGHT = 80;
    private static final double SUN_WIDTH = 60;
    private static final double SUN_HEIGHT = 60;
    private static final double BULLET_SPEED = 400.0;
    private static final int UI_UPDATE_INTERVAL = 1000;
    private static final int BULLET_UPDATE_INTERVAL = 16;
    private static final double CELL_WIDTH = 80.0;
    private static final double BULLET_WIDTH = 50;
    private static final double BULLET_HEIGHT = 50;

    public void setLevelData(Level level) {
        this.level = level;
        gameState.setLevelNumber(level.getLevelNumber());
        gameState.setDayMode(level.getGameMode() == GameMode.DAY);
        gameState.setRemainingWaves(gameState.calculateWaves(level.getLevelNumber()));
        String bg = level.getGameMode() == GameMode.DAY ? "/day2.jpg" : "/media/night.jpg";
        backgroundImage.setImage(new Image(getClass().getResource(bg).toExternalForm()));
        updateStatusLabel();
    }

    @FXML
    public void initialize() {
        cleanup();

        board = new GameBoard();
        gameState = new GameState(1, true);
        gameState.setSunPoints(50);
        houseProtection = new HouseProtection();
        if (level == null) level = new Level(1, GameMode.DAY);

        zombieRenderer = new ZombieRenderer(gameGrid, board);
        detector = new CollisionDetector(board);
        handler = new CollisionHandler(board, zombieRenderer);
        sunService = new SunService(board);
        plantService = new PlantService(board, detector, handler);
        zombieService = new ZombieService(board, detector, handler, gameState, houseProtection, zombieRenderer);
        levelService = new LevelService(gameState, zombieService);
        gameService = new GameService(board, detector, handler, gameState, houseProtection, level, zombieRenderer);

        SoundManager.stopBackgroundMusic();
        SoundManager.playBackgroundMusic("grasswalk.mp3");

        Random random = new Random();
        sunGenerator = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            if (!isPaused && !gameState.isGameOver()) {
                int row = random.nextInt(board.getRows());
                int col = random.nextInt(board.getCols());
                System.out.println("Generating sun at (" + row + "," + col + ")");
                Platform.runLater(() -> {
                    if (!gameState.isGameOver()) {
                        sunService.addSunToCell(row, col);
                        updateCell(row, col);
                    }
                });
            }
        }));
        sunGenerator.setCycleCount(Animation.INDEFINITE);
        sunGenerator.play();

        waveStarter = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            if (!isPaused && !gameState.isGameOver()) {
                levelService.startWave();
                System.out.println("Wave started after delay");
            }
        }));
        waveStarter.setCycleCount(1);
        waveStarter.play();

        gameService.setGameStatusListener(new GameStatusListener() {
            @Override
            public void onGameOver(boolean isWin) {
                endGame(isWin);
            }

            @Override
            public void onWaveStarted(int waveNumber) {
                Platform.runLater(() -> statusLabel.setText("Wave " + waveNumber + " Started"));
            }

            @Override
            public void onSunGenerated(int value) {}
        });

        updateSunLabel();
        updateStatusLabel();
        setupGrid();
        gameService.startServices();
        gameService.startNextWave();

        plantSelectionBox.getChildren().addAll(
                org.example.plants_vs_zombies1.util.CardLoaderService.loadCardsForLevel(level, pt -> selectedPlant = pt)
        );

        uiUpdater = new Timeline(new KeyFrame(Duration.millis(UI_UPDATE_INTERVAL), e -> refreshUI()));
        uiUpdater.setCycleCount(Animation.INDEFINITE);
        uiUpdater.play();

        bulletUpdater = new Timeline(new KeyFrame(Duration.millis(BULLET_UPDATE_INTERVAL), e -> updateBullets()));
        bulletUpdater.setCycleCount(Animation.INDEFINITE);
        bulletUpdater.play();

        backButton.setOnAction(e -> openOptionsMenu());
    }

    private void cleanup() {
        if (gameService != null) {
            gameService.stopGame();
        }
        if (uiUpdater != null) {
            uiUpdater.stop();
        }
        if (sunGenerator != null) {
            sunGenerator.stop();
        }
        if (waveStarter != null) {
            waveStarter.stop();
        }
        if (bulletUpdater != null) {
            bulletUpdater.stop();
        }
        SoundManager.stopBackgroundMusic();
    }

    private void openOptionsMenu() {
        try {
            gameService.stopGame();
            uiUpdater.stop();
            sunGenerator.stop();
            waveStarter.stop();
            bulletUpdater.stop();
            SoundManager.stopBackgroundMusic();
            isPaused = true;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/plants_vs_zombies1/view/Options.fxml"));
            Parent root = loader.load();

            OptionsMenuController optionsController = loader.getController();
            optionsController.setCurrentLevel(level);
            optionsController.setGameStage((Stage) backButton.getScene().getWindow());
            optionsController.setGameController(this);

            Stage optionStage = new Stage();
            optionStage.setTitle("Options");
            optionStage.setScene(new Scene(root));
            optionStage.initOwner((Stage) backButton.getScene().getWindow());
            optionStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void resumeGameLoop() {
        if (isPaused) {
            gameService.startServices();
            gameService.startNextWave();
            uiUpdater.play();
            sunGenerator.play();
            bulletUpdater.play();
            SoundManager.stopBackgroundMusic();
            SoundManager.playBackgroundMusic("grasswalk.mp3");
            isPaused = false;
        }
    }

    private void setupGrid() {
        gameGrid.getChildren().clear();
        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                AnchorPane cell = new AnchorPane();
                GridPane.setRowIndex(cell, r);
                GridPane.setColumnIndex(cell, c);
                cell.setOnMouseClicked(this::onCellClicked);
                gameGrid.add(cell, c, r);
            }
        }
    }

    @FXML
    private void onCellClicked(MouseEvent ev) {
        AnchorPane cell = (AnchorPane) ev.getSource();
        Integer col = GridPane.getColumnIndex(cell);
        Integer row = GridPane.getRowIndex(cell);
        if (row == null || col == null) return;

        if (selectedPlant != null) {
            Plant plant = switch (selectedPlant) {
                case PEASHOOTER -> new Peashooter(col, row);
                case SUNFLOWER -> new Sunflower(col, row);
                default -> null;
            };

            if (plant != null) {
                try {
                    if (gameState.getSunPoints() < plant.getCost()) {
                        throw new InsufficientSunException("Not enough sun to plant " + selectedPlant);
                    }

                    boolean success = plantService.plant(plant, row, col);
                    if (success) {
                        gameState.spendSunPoints(plant.getCost());
                        updateSunLabel();
                        SoundManager.playSfx("plant2.mp3");
                    }
                } catch (InsufficientSunException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            selectedPlant = null;
        } else {
            int got = sunService.collectSunsAt(row, col);
            if (got > 0) {
                gameState.addSunPoints(got);
                updateSunLabel();
                SoundManager.playSfx("sun_collect.mp3");
                String cellKey = row + "," + col;
                ImageView sunView = sunViews.remove(cellKey);
                if (sunView != null) {
                    cell.getChildren().remove(sunView);
                }
            }
        }
        refreshUI();
    }

    private void refreshUI() {
        Platform.runLater(() -> {
            updateSunLabel();
            updateStatusLabel();
            for (int r = 0; r < board.getRows(); r++) {
                for (int c = 0; c < board.getCols(); c++) {
                    updateCell(r, c);
                }
            }
            zombieRenderer.renderZombies();
        });
    }

    private void updateBullets() {
        Platform.runLater(() -> {
            for (Bullet bullet : board.getBullets()) {
                ImageView bulletView = bulletViews.computeIfAbsent(bullet, b -> {
                    ImageView iv = new ImageView(new Image(getClass().getResource("/media/pea.png").toExternalForm()));
                    iv.setFitWidth(BULLET_WIDTH);
                    iv.setFitHeight(BULLET_HEIGHT);
                    AnchorPane cell = getCellAt(bullet.getY(), bullet.getX());
                    if (cell != null) {
                        cell.getChildren().add(iv);
                        startBulletMovement(bullet, iv);
                    }
                    return iv;
                });
            }

            board.getBullets().removeIf(bullet -> {
                ImageView view = bulletViews.get(bullet);
                if (bullet.getPreciseX() >= board.getCols()) {
                    if (view != null) {
                        AnchorPane cell = getCellAt(bullet.getY(), bullet.getX());
                        if (cell != null) cell.getChildren().remove(view);
                        bulletViews.remove(bullet);
                    }
                    return true;
                }
                return false;
            });
        });
    }

    private void startBulletMovement(Bullet bullet, ImageView bulletView) {
        double startX = bullet.getPreciseX() * CELL_WIDTH;
        double totalDistance = (board.getCols() - bullet.getPreciseX()) * CELL_WIDTH;
        double duration = totalDistance / BULLET_SPEED;

        TranslateTransition move = new TranslateTransition(Duration.seconds(duration), bulletView);
        move.setFromX(startX);
        move.setToX(board.getCols() * CELL_WIDTH);
        move.setInterpolator(javafx.animation.Interpolator.LINEAR);
        move.setOnFinished(e -> {
            AnchorPane cell = getCellAt(bullet.getY(), bullet.getX());
            if (cell != null) cell.getChildren().remove(bulletView);
            bulletViews.remove(bullet);
            board.removeBullet(bullet);
        });

        Timeline collisionCheck = new Timeline(new KeyFrame(Duration.millis(10), ev -> {
            double preciseX = bullet.getPreciseX();
            bullet.setX(preciseX + (BULLET_SPEED * 0.01 / CELL_WIDTH));
            int col = (int) preciseX;
            if (col < board.getCols()) {
                List<Zombie> hitZombies = detector.detectBulletCollisions(null, bullet.getY(), col);
                if (!hitZombies.isEmpty()) {
                    handler.handleBulletCollision(bullet, hitZombies);
                    AnchorPane cell = getCellAt(bullet.getY(), col);
                    if (cell != null) cell.getChildren().remove(bulletView);
                    bulletViews.remove(bullet);
                    board.removeBullet(bullet);
                    move.stop();
                }
            }
        }));
        collisionCheck.setCycleCount(Animation.INDEFINITE);
        move.setOnFinished(e -> collisionCheck.stop());
        collisionCheck.play();
        move.play();
    }

    private AnchorPane getCellAt(int row, int col) {
        return (AnchorPane) gameGrid.getChildren().stream()
                .filter(n -> GridPane.getRowIndex(n) == row && GridPane.getColumnIndex(n) == col)
                .findFirst().orElse(null);
    }

    private void updateCell(int row, int col) {
        AnchorPane cell = getCellAt(row, col);
        if (cell == null) return;

        var cellModel = board.getCell(row, col);
        Plant plant = cellModel.getPlant();
        int sunCount = cellModel.getSuns().size();
        String cellKey = row + "," + col;

        ImageView plantView = (ImageView) cell.getChildren().stream()
                .filter(n -> n instanceof ImageView iv && iv.getImage().getUrl().contains("plants"))
                .findFirst().orElse(null);

        if (plant != null) {
            String gif = PlantGifManager.getGifPath(plant.getPlantType());
            if (gif != null) {
                String gifUrl = getClass().getResource(gif).toExternalForm();
                if (plantView == null || !plantView.getImage().getUrl().equals(gifUrl)) {
                    if (plantView != null) cell.getChildren().remove(plantView);
                    ImageView iv = new ImageView(new Image(gifUrl, true));
                    iv.setCache(false);
                    iv.setFitWidth(PLANT_WIDTH);
                    iv.setFitHeight(PLANT_HEIGHT);
                    cell.getChildren().add(iv);
                }
            }
        } else if (plantView != null) {
            cell.getChildren().remove(plantView);
        }

        ImageView sunView = sunViews.get(cellKey);

        if (sunCount > 0) {
            if (sunView == null || !cell.getChildren().contains(sunView)) {
                sunView = new ImageView(new Image(getClass().getResource("/media/sun.png").toExternalForm()));
                sunView.setFitWidth(SUN_WIDTH);
                sunView.setFitHeight(SUN_HEIGHT);
                cell.getChildren().add(sunView);
                sunViews.put(cellKey, sunView);
            }
        } else {
            if (sunView != null && cell.getChildren().contains(sunView)) {
                cell.getChildren().remove(sunView);
                sunViews.remove(cellKey);
            }
        }
    }

    private void updateSunLabel() {
        if (sunLabel != null) {
            sunLabel.setText(String.valueOf(gameState.getSunPoints()));
        }
    }

    private void updateStatusLabel() {
        if (statusLabel != null) {
            statusLabel.setText(String.format(
                    "Level %d - %s | Waves: %d",
                    gameState.getLevelNumber(),
                    gameState.isDayMode() ? "Day" : "Night",
                    gameState.getRemainingWaves()
            ));
        }
    }

    private void endGame(boolean win) {
        cleanup();

        Platform.runLater(() -> {
            try {
                if (win) {
                    SoundManager.stopBackgroundMusic();
                    SoundManager.playBackgroundMusic("win_music.mp3");

                    Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
                    winAlert.setTitle("Victory!");
                    winAlert.setHeaderText("You Win!");
                    winAlert.setContentText("Congratulations! You have defeated all zombies in Level " + level.getLevelNumber() + ".");
                    winAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            // Update player's unlocked level and stats
                            Player player = SaveLoadService.getCurrentPlayer();
                            if (player != null) {
                                player.setCurrentLevel(level.getLevelNumber() + 1);
                                player.setWins(player.getWins() + 1);
                                player.setScore(player.getScore() + (level.getLevelNumber() * 100));
                                SaveLoadService service = new SaveLoadService();
                                if (!service.updatePlayer(player)) {
                                    System.err.println("Error updating player progress");
                                }
                            } else {
                                System.err.println("Error: No current player set");
                            }

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/plants_vs_zombies1/view/level_select.fxml"));
                                Parent root = loader.load();
                                Stage stage = (Stage) gameGrid.getScene().getWindow();
                                stage.setScene(new Scene(root));
                                stage.setTitle("Level Select");
                                SoundManager.stopBackgroundMusic();
                                SoundManager.playBackgroundMusic();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                System.err.println("Error loading level_select.fxml: " + ex.getMessage());
                            }
                        }
                    });
                } else {
                    SoundManager.stopBackgroundMusic();
                    SoundManager.playBackgroundMusic("05_losemusic.mp3");

                    Player player = SaveLoadService.getCurrentPlayer();
                    if (player != null) {
                        player.setLosses(player.getLosses() + 1);
                        SaveLoadService service = new SaveLoadService();
                        if (!service.updatePlayer(player)) {
                            System.err.println("Error updating player progress");
                        }
                    } else {
                        System.err.println("Error: No current player set");
                    }

                    Alert lossAlert = new Alert(Alert.AlertType.INFORMATION);
                    lossAlert.setTitle("Game Over");
                    lossAlert.setHeaderText("You Lost!");
                    lossAlert.setContentText("The zombies have reached your house. Try again!");
                    lossAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/plants_vs_zombies1/view/level_select.fxml"));
                                Parent root = loader.load();
                                Stage stage = (Stage) gameGrid.getScene().getWindow();
                                stage.setScene(new Scene(root));
                                stage.setTitle("Level Select");
                                SoundManager.stopBackgroundMusic();
                                SoundManager.playBackgroundMusic();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                System.err.println("Error loading level_select.fxml: " + ex.getMessage());
                            }
                        }
                    });
                    if (statusLabel != null) {
                        statusLabel.setText("Game Over");
                    }
                    System.out.println("Game Over");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}