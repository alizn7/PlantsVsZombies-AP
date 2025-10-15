package org.example.plants_vs_zombies1.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import org.example.plants_vs_zombies1.util.SoundManager;

import java.io.IOException;
import java.net.URL;

public class RegisterVideoController {

    @FXML private MediaView mediaView;

    @FXML
    public void initialize() {
        SoundManager.stopBackgroundMusic();

        URL videoUrl = getClass().getResource("/media/register_video.mp4");
        if (videoUrl != null) {
            Media media = new Media(videoUrl.toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            mediaPlayer.setOnReady(() -> {
                mediaView.setPreserveRatio(true);

                Stage stage = (Stage) mediaView.getScene().getWindow();

                Platform.runLater(() -> {
                    mediaView.fitWidthProperty().bind(stage.widthProperty());
                    mediaView.fitHeightProperty().bind(stage.heightProperty());
                });

                mediaPlayer.play();
            });

            mediaPlayer.setOnEndOfMedia(() -> {
                Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/plants_vs_zombies1/view/register.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) mediaView.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Register");
                        stage.setResizable(true);
                        stage.show();

                        if (SoundManager.isMusicEnabled()) {
                            SoundManager.playBackgroundMusic();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });
        } else {
            System.err.println("Register video not found.");
        }
    }
}
