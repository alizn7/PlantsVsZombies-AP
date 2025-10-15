package org.example.plants_vs_zombies1.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.plants_vs_zombies1.model.Player;
import org.example.plants_vs_zombies1.util.SoundManager;

import java.io.IOException;

public class AuthMenuController {

    @FXML private Label loginStatusLabel;
    @FXML private Button adventureButton;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Button optionsButton;
    @FXML private StackPane rootPane;

    private static Player loggedInPlayer = null;

    @FXML
    public void initialize() {
        adventureButton.setDisable(true);
        updateLoginStatus();
        SoundManager.playBackgroundMusic();
    }

    private void updateLoginStatus() {
        if (loggedInPlayer == null) {
            loginStatusLabel.setText("Welcome! Please login.");
            adventureButton.setDisable(true);
        } else {
            loginStatusLabel.setText("Welcome back, " + loggedInPlayer.getUsername() + "!");
            adventureButton.setDisable(false);
        }
    }

    @FXML
    private void onLoginClicked() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/plants_vs_zombies1/view/login.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onRegisterClicked() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/plants_vs_zombies1/view/register_video.fxml"));
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Register");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onOptionsClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/plants_vs_zombies1/view/settings.fxml"));
            Parent overlay = loader.load();
            rootPane.getChildren().add(overlay);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAdventureClicked() {
        if (loggedInPlayer == null) return;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/plants_vs_zombies1/view/main_menu.fxml"));
            Stage stage = (Stage) adventureButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Game Menu");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setLoggedInPlayer(Player player) {
        loggedInPlayer = player;
    }
}
