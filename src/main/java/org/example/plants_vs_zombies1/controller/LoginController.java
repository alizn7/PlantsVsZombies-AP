package org.example.plants_vs_zombies1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import org.example.plants_vs_zombies1.model.Player;
import org.example.plants_vs_zombies1.service.SaveLoadService;
import org.example.plants_vs_zombies1.util.SoundManager;

import java.io.IOException;
import java.net.URL;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private MediaView mediaView;

    private MediaPlayer backgroundVideoPlayer;

    @FXML
    public void initialize() {
        URL videoUrl = getClass().getResource("/media/login_background_video.mp4");
        if (videoUrl != null) {
            Media media = new Media(videoUrl.toExternalForm());
            backgroundVideoPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(backgroundVideoPlayer);
            backgroundVideoPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundVideoPlayer.setAutoPlay(true);
        }

        SoundManager.playBackgroundMusic();
    }

    @FXML
    private void onLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        SaveLoadService saveLoadService = new SaveLoadService();
        try {
            Player player = saveLoadService.loadPlayer(username, password);
            if (player != null) {
                StartController.setLoggedInPlayer(player);
                Parent root = FXMLLoader.load(getClass().getResource("/org/example/plants_vs_zombies1/view/start.fxml"));
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Plants vs Zombies");
                stage.setResizable(true);
                stage.sizeToScene();
                stage.show();
            } else {
                errorLabel.setText("Username or password is incorrect.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Database error.");
        }
    }

    @FXML
    private void goToSignup(ActionEvent event) {
        stopVideo();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/plants_vs_zombies1/view/register_video.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sign Up");
            stage.setResizable(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Unable to load signup page.");
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        stopVideo();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/plants_vs_zombies1/view/start.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Plants vs Zombies");
            stage.setResizable(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Unable to return to start page.");
        }
    }

    private void stopVideo() {
        if (backgroundVideoPlayer != null) backgroundVideoPlayer.stop();
    }
}
