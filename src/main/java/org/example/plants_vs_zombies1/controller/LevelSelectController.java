package org.example.plants_vs_zombies1.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.plants_vs_zombies1.model.Level;
import org.example.plants_vs_zombies1.model.Player;
import org.example.plants_vs_zombies1.model.enums.GameMode;
import org.example.plants_vs_zombies1.service.SaveLoadService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LevelSelectController implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private ImageView backgroundImage;
    @FXML private HBox levelContainer;
    @FXML private Button dayButton;
    @FXML private Button nightButton;

    private Player currentPlayer;
    private GameMode currentMode = GameMode.DAY;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentPlayer = SaveLoadService.getCurrentPlayer();
        showDayLevels();
    }

    @FXML
    private void showDayLevels() {
        currentMode = GameMode.DAY;
        backgroundImage.setImage(new Image(getClass().getResource("/media/dayLevel.jpg").toExternalForm()));
        populateLevels(1, 3);
    }

    @FXML
    private void showNightLevels() {
        currentMode = GameMode.NIGHT;
        backgroundImage.setImage(new Image(getClass().getResource("/media/nightLevel.jpg").toExternalForm()));
        populateLevels(4, 6);
    }

    private void populateLevels(int start, int end) {
        levelContainer.getChildren().clear();
        int unlockedLevel = currentPlayer.getCurrentLevel();

        for (int i = start; i <= end; i++) {
            boolean isUnlocked = (i == 1) || (unlockedLevel >= i - 1);
            Level level = new Level(i, currentMode);
            VBox card = createLevelCard(level, isUnlocked);
            levelContainer.getChildren().add(card);
        }
    }

    private VBox createLevelCard(Level level, boolean isUnlocked) {
        VBox card = new VBox();
        card.setAlignment(Pos.TOP_CENTER);
        card.setSpacing(10);
        card.setPrefWidth(150);
        card.setTranslateY(20);

        Label label = new Label("Level " + level.getLevelNumber());
        label.setStyle("-fx-text-fill: transparent;");

        Button playBtn = new Button();
        playBtn.setPrefWidth(90);
        playBtn.setPrefHeight(30);
        playBtn.setTranslateY(40);
        playBtn.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: transparent;
        """);

        playBtn.setOnAction(e -> {
            if (!isUnlocked) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("مرحله قفل است");
                alert.setHeaderText("🚫 دسترسی غیرمجاز");
                alert.setContentText("ابتدا مرحله قبلی را به پایان برسانید.");

                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.setStyle("""
                    -fx-background-color: linear-gradient(to bottom right, #2b2d42, #1d3557);
                    -fx-font-family: 'Vazir', sans-serif;
                    -fx-font-size: 16px;
                    -fx-text-fill: white;
                """);
                dialogPane.lookup(".content.label").setStyle("-fx-text-fill: white;");
                dialogPane.lookupButton(ButtonType.OK).setStyle("""
                    -fx-background-color: #ef233c;
                    -fx-text-fill: white;
                    -fx-font-weight: bold;
                    -fx-background-radius: 8;
                """);

                alert.showAndWait();
            } else {
                startLevel(level);
            }
        });

        card.getChildren().addAll(label, playBtn);
        return card;
    }

    private void startLevel(Level level) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/plants_vs_zombies1/view/game.fxml"));
            Parent root = loader.load();

            GameController controller = loader.getController();
            controller.setLevelData(level);

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/plants_vs_zombies1/view/start.fxml"));
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
