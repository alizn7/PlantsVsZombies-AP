package org.example.plants_vs_zombies1.service;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.example.plants_vs_zombies1.model.board.GameBoard;
import org.example.plants_vs_zombies1.model.zombie.Zombie;
import org.example.plants_vs_zombies1.util.ZombieGifManager;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ZombieRenderer {
    private static final Logger LOGGER = Logger.getLogger(ZombieRenderer.class.getName());
    private final GridPane gameGrid;
    private final GameBoard gameBoard;
    private final Map<Zombie, ImageView> zombieViews = new HashMap<>();

    private static final double ZOMBIE_WIDTH = 85;
    private static final double ZOMBIE_HEIGHT = 90;
    private static final double HEAD_WIDTH = 60;
    private static final double HEAD_HEIGHT = 60;

    public ZombieRenderer(GridPane gameGrid, GameBoard gameBoard) {
        this.gameGrid = gameGrid;
        this.gameBoard = gameBoard;
    }

    public void renderZombies() {
        Platform.runLater(() -> {
            zombieViews.entrySet().removeIf(entry -> {
                Zombie zombie = entry.getKey();
                if (zombie.isDead()) {
                    showDeathGifs(zombie);
                    return true;
                }
                return false;
            });

            for (Zombie zombie : gameBoard.getZombies()) {
                renderZombie(zombie);
            }
        });
    }

    public void renderZombie(Zombie zombie) {
        if (zombie == null || zombie.isDead()) {
            if (zombie != null && zombie.isDead()) {
                showDeathGifs(zombie);
            }
            return;
        }

        Platform.runLater(() -> {
            int row = zombie.getY(); // Row (0 to 4)
            int col = zombie.getX(); // Column (0 to 8)

            if (!gameBoard.isValidPosition(row, col)) {
                LOGGER.warning("Invalid position for zombie " + zombie.getName() + " at (" + row + "," + col + ")");
                return;
            }

            ImageView zombieView = zombieViews.get(zombie);
            if (zombieView != null && zombieView.getParent() != null) {
                LOGGER.fine("Zombie " + zombie.getName() + " at (" + row + "," + col + ") already rendered");
                return;
            }

            boolean isAttacking = gameBoard.getCell(row, col).getPlant() != null;
            String imagePath = ZombieGifManager.getGifPath(zombie.getZombieType(), isAttacking ? ZombieGifManager.ZombieState.ATTACKING : ZombieGifManager.ZombieState.WALKING);

            if (imagePath == null || getClass().getResource(imagePath) == null) {
                LOGGER.severe("Cannot load GIF for zombie " + zombie.getName() + " at path: " + imagePath);
                return;
            }

            if (zombieView == null) {
                zombieView = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
                zombieView.setFitWidth(ZOMBIE_WIDTH);
                zombieView.setFitHeight(ZOMBIE_HEIGHT);
                zombieViews.put(zombie, zombieView);
                LOGGER.info("Placed zombie " + zombie.getName() + " at (" + row + "," + col + ")");
            } else {
                zombieView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
                LOGGER.info("Updated zombie to " + (isAttacking ? "ATTACKING" : "WALKING") + " at (" + row + "," + col + ")");
            }

            AnchorPane cellPane = getCellAt(row, col);
            if (cellPane != null && !cellPane.getChildren().contains(zombieView)) {
                cellPane.getChildren().removeIf(node -> node instanceof ImageView
                        && (((ImageView) node).getImage().getUrl().contains("zombie_die")
                        || ((ImageView) node).getImage().getUrl().contains("newspaper/Die")
                        || ((ImageView) node).getImage().getUrl().contains("zombie_head_fly")
                        || ((ImageView) node).getImage().getUrl().contains("newspaper/HeadWalk0")));
                cellPane.getChildren().add(zombieView);
            }
        });
    }

    public void updateZombieState(Zombie zombie, ZombieGifManager.ZombieState state) {
        if (zombie == null || zombie.isDead()) {
            if (zombie != null && zombie.isDead()) {
                showDeathGifs(zombie);
            }
            return;
        }

        Platform.runLater(() -> {
            int row = zombie.getY();
            int col = zombie.getX();

            if (!gameBoard.isValidPosition(row, col)) {
                LOGGER.warning("Invalid position for zombie " + zombie.getName() + " at (" + row + "," + col + ")");
                return;
            }

            ImageView zombieView = zombieViews.get(zombie);
            String imagePath = ZombieGifManager.getGifPath(zombie.getZombieType(), state);

            if (imagePath == null || getClass().getResource(imagePath) == null) {
                LOGGER.severe("Cannot load GIF for zombie " + zombie.getName() + " at path: " + imagePath);
                return;
            }

            if (zombieView == null) {
                zombieView = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
                zombieView.setFitWidth(ZOMBIE_WIDTH);
                zombieView.setFitHeight(ZOMBIE_HEIGHT);
                zombieViews.put(zombie, zombieView);
                LOGGER.info("Placed zombie " + zombie.getName() + " at (" + row + "," + col + ")");
            } else {
                zombieView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
                LOGGER.info("Updated zombie state to " + state + " at (" + row + "," + col + ")");
            }

            AnchorPane cellPane = getCellAt(row, col);
            if (cellPane != null && !cellPane.getChildren().contains(zombieView)) {
                cellPane.getChildren().removeIf(node -> node instanceof ImageView
                        && (((ImageView) node).getImage().getUrl().contains("zombie_die")
                        || ((ImageView) node).getImage().getUrl().contains("newspaper/Die")
                        || ((ImageView) node).getImage().getUrl().contains("zombie_head_fly")
                        || ((ImageView) node).getImage().getUrl().contains("newspaper/HeadWalk0")));
                cellPane.getChildren().add(zombieView);
            }
        });
    }

    public void moveZombie(Zombie zombie, int newCol) {
        if (zombie == null || zombie.isDead()) {
            if (zombie != null && zombie.isDead()) {
                showDeathGifs(zombie);
            }
            return;
        }

        Platform.runLater(() -> {
            int row = zombie.getY();
            if (!gameBoard.isValidPosition(row, newCol)) {
                LOGGER.warning("Invalid position for zombie " + zombie.getName() + " at (" + row + "," + newCol + ")");
                return;
            }

            ImageView zombieView = zombieViews.get(zombie);
            if (zombieView != null) {
                AnchorPane oldCellPane = getCellAt(row, zombie.getX());
                AnchorPane newCellPane = getCellAt(row, newCol);
                if (oldCellPane != null && oldCellPane.getChildren().contains(zombieView)) {
                    oldCellPane.getChildren().remove(zombieView);
                }
                if (newCellPane != null && !newCellPane.getChildren().contains(zombieView)) {
                    newCellPane.getChildren().removeIf(node -> node instanceof ImageView
                            && (((ImageView) node).getImage().getUrl().contains("zombie_die")
                            || ((ImageView) node).getImage().getUrl().contains("newspaper/Die")
                            || ((ImageView) node).getImage().getUrl().contains("zombie_head_fly")
                            || ((ImageView) node).getImage().getUrl().contains("newspaper/HeadWalk0")));
                    newCellPane.getChildren().add(zombieView);
                    LOGGER.info("Moved zombie " + zombie.getName() + " to (" + row + "," + newCol + ")");
                }
            }
        });
    }

    public void showDeathGifs(Zombie zombie) {
        Platform.runLater(() -> {
            int row = zombie.getY();
            int col = zombie.getX();

            if (!gameBoard.isValidPosition(row, col)) {
                LOGGER.warning("Invalid position for death GIFs for zombie " + zombie.getName() + " at (" + row + "," + col + ")");
                return;
            }

            AnchorPane cellPane = getCellAt(row, col);
            if (cellPane != null) {
                ImageView zombieView = zombieViews.remove(zombie);
                if (zombieView != null) {
                    cellPane.getChildren().remove(zombieView);
                }

                cellPane.getChildren().removeIf(node -> node instanceof ImageView
                        && (((ImageView) node).getImage().getUrl().contains("zombie_die")
                        || ((ImageView) node).getImage().getUrl().contains("newspaper/Die")
                        || ((ImageView) node).getImage().getUrl().contains("zombie_head_fly")
                        || ((ImageView) node).getImage().getUrl().contains("newspaper/HeadWalk0")));

                String deathGifPath = ZombieGifManager.getGifPath(zombie.getZombieType(), ZombieGifManager.ZombieState.DYING);
                if (deathGifPath == null || getClass().getResource(deathGifPath) == null) {
                    LOGGER.severe("Cannot load death GIF for zombie " + zombie.getName() + " at path: " + deathGifPath);
                    return;
                }

                ImageView deathView = new ImageView(new Image(getClass().getResource(deathGifPath).toExternalForm()));
                deathView.setFitWidth(ZOMBIE_WIDTH);
                deathView.setFitHeight(ZOMBIE_HEIGHT);
                cellPane.getChildren().add(deathView);

                String headGifPath = ZombieGifManager.getGifPath(zombie.getZombieType(), ZombieGifManager.ZombieState.HEADLESS);
                if (headGifPath != null && getClass().getResource(headGifPath) != null) {
                    ImageView headView = new ImageView(new Image(getClass().getResource(headGifPath).toExternalForm()));
                    headView.setFitWidth(HEAD_WIDTH);
                    headView.setFitHeight(HEAD_HEIGHT);
                    cellPane.getChildren().add(headView);
                }

                new Timeline(
                        new KeyFrame(Duration.seconds(2), e -> {
                            cellPane.getChildren().remove(deathView);
                            if (headGifPath != null && getClass().getResource(headGifPath) != null) {
                                cellPane.getChildren().removeIf(node -> node instanceof ImageView
                                        && ((ImageView) node).getImage().getUrl().contains("zombie_head_fly"));
                            }
                            LOGGER.info("Displayed death GIFs for zombie " + zombie.getName() + " at (" + row + "," + col + ")");
                        })
                ).play();
            }
        });
    }

    public void removeZombie(Zombie zombie) {
        Platform.runLater(() -> {
            ImageView zombieView = zombieViews.remove(zombie);
            if (zombieView != null) {
                AnchorPane cellPane = getCellAt(zombie.getY(), zombie.getX());
                if (cellPane != null) {
                    cellPane.getChildren().remove(zombieView);
                    LOGGER.info("Removed zombie " + zombie.getName() + " from (" + zombie.getY() + "," + zombie.getX() + ")");
                }
            }
        });
    }

    private AnchorPane getCellAt(int row, int col) {
        return (AnchorPane) gameGrid.getChildren().stream()
                .filter(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row
                        && GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == col)
                .findFirst().orElse(null);
    }
}