package org.example.plants_vs_zombies1.controller;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.plants_vs_zombies1.model.Player;
import org.example.plants_vs_zombies1.service.SaveLoadService;
import org.example.plants_vs_zombies1.util.SoundManager;

import java.io.IOException;
import java.net.URL;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private MediaView mediaView;

    private MediaPlayer backgroundVideoPlayer;

    @FXML
    public void initialize() {
        Platform.runLater(this::setupMedia);

        Tooltip passwordTip = new Tooltip("password must be at least 6 characters");
        passwordTip.setStyle("-fx-background-color: #ffd6d6; -fx-text-fill: #000;");
        passwordField.setTooltip(passwordTip);
    }

    private void setupMedia() {
        URL videoUrl = getClass().getResource("/media/register_background_video.mp4");
        if (videoUrl != null) {
            Media media = new Media(videoUrl.toExternalForm());
            backgroundVideoPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(backgroundVideoPlayer);
            backgroundVideoPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundVideoPlayer.setAutoPlay(true);

            Stage stage = (Stage) mediaView.getScene().getWindow();
            mediaView.fitWidthProperty().bind(stage.widthProperty());
            mediaView.fitHeightProperty().bind(stage.heightProperty());
        }

        SoundManager.playBackgroundMusic();
    }

    @FXML
    private void onRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("fill all fields");
            shakeNode(usernameField);
            shakeNode(passwordField);
            return;
        }

        SaveLoadService saveLoadService = new SaveLoadService();
        if (saveLoadService.loadPlayer(username, password) != null) {
            errorLabel.setText("this username is already in use");
            shakeNode(errorLabel);
            return;
        }

        boolean success = saveLoadService.savePlayer(username, password);
        if (success) {
            stopVideo();
            goToMainMenu();
        } else {
            errorLabel.setText("error in regestration, try again!");
            shakeNode(errorLabel);
        }
    }

    private void goToMainMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/plants_vs_zombies1/view/start.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("main menu");
            stage.setResizable(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("error in loading main menu");
            shakeNode(errorLabel);
        }
    }

    @FXML
    private void goBack() {
        stopVideo();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/plants_vs_zombies1/view/start.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("back to main menu");
            stage.setResizable(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("error in loading start menu");
            shakeNode(errorLabel);
        }
    }

    private void stopVideo() {
        if (backgroundVideoPlayer != null) backgroundVideoPlayer.stop();
    }

    private void shakeNode(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(70), node);
        tt.setFromX(0f);
        tt.setByX(10f);
        tt.setCycleCount(4);
        tt.setAutoReverse(true);
        tt.play();
    }
}
