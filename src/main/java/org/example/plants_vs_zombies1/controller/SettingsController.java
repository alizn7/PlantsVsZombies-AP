package org.example.plants_vs_zombies1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.example.plants_vs_zombies1.model.Player;
import org.example.plants_vs_zombies1.service.SaveLoadService;
import org.example.plants_vs_zombies1.util.SoundManager;

public class SettingsController {

    @FXML private CheckBox musicCheckBox;
    @FXML private CheckBox sfxCheckBox;
    @FXML private PasswordField passwordField;
    @FXML private Button togglePasswordButton;
    @FXML private Button okButton;
    @FXML private AnchorPane settingsRoot;

    private final SaveLoadService saveLoadService = new SaveLoadService();

    @FXML
    public void initialize() {
        musicCheckBox.setSelected(SoundManager.isMusicEnabled());
        sfxCheckBox.setSelected(SoundManager.isSfxEnabled());
        passwordField.setVisible(false);
    }

    @FXML
    private void onTogglePassword() {
        passwordField.setVisible(!passwordField.isVisible());
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

        String newPass = passwordField.getText().trim();
        Player currentPlayer = SaveLoadService.getCurrentPlayer();
        if (!newPass.isEmpty() && currentPlayer != null) {
            currentPlayer.setPassword(newPass);
            saveLoadService.updatePlayer(currentPlayer);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("تنظیمات ذخیره شد");
        alert.setHeaderText(null);
        alert.setContentText("تنظیمات شما با موفقیت ذخیره شد.");
        alert.showAndWait();

        StackPane root = (StackPane) settingsRoot.getScene().getRoot();
        root.getChildren().remove(settingsRoot);
    }
}
