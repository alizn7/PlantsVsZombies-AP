package org.example.plants_vs_zombies1.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.plants_vs_zombies1.model.Level;
import org.example.plants_vs_zombies1.util.SoundManager;

public class OptionsMenuController {

    @FXML private CheckBox musicCheckBox;
    @FXML private CheckBox sfxCheckBox;

    @FXML private Button backToGameButton;
    @FXML private Button restartLevelButton;
    @FXML private Button mainMenuButton;

    private Level currentLevel;
    private Stage gameStage;
    private GameController gameController;

    @FXML
    public void initialize() {
        musicCheckBox.setSelected(SoundManager.isMusicEnabled());
        sfxCheckBox.setSelected(SoundManager.isSfxEnabled());

        musicCheckBox.setOnAction(e -> onSaveClicked());
        sfxCheckBox.setOnAction(e -> onSaveClicked());
    }

    @FXML
    private void onBackToGame() {
        if (gameController != null) {
            gameController.resumeGameLoop();
        }
        Stage stage = (Stage) backToGameButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onRestartLevel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/plants_vs_zombies1/view/game.fxml"));
            Parent root = loader.load();

            GameController controller = loader.getController();
            if (currentLevel != null) {
                controller.setLevelData(currentLevel);
            }

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Restarted Level");
            newStage.show();

            Stage thisStage = (Stage) restartLevelButton.getScene().getWindow();
            thisStage.close();
            if (gameStage != null) gameStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/plants_vs_zombies1/view/start.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Plants vs Zombies - Main Menu");
            newStage.show();

            Stage current = (Stage) mainMenuButton.getScene().getWindow();
            current.close();
            if (gameStage != null) gameStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveClicked() {
        SoundManager.setMusicEnabled(musicCheckBox.isSelected());
        SoundManager.setSfxEnabled(sfxCheckBox.isSelected());

        if (musicCheckBox.isSelected()) {
            SoundManager.playBackgroundMusic();
        } else {
            SoundManager.stopBackgroundMusic();
        }
    }

    public void setCurrentLevel(Level level) {
        this.currentLevel = level;
    }

    public void setGameStage(Stage stage) {
        this.gameStage = stage;
    }

    public void setGameController(GameController controller) {
        this.gameController = controller;
    }
}