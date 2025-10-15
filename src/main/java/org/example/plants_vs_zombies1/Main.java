package org.example.plants_vs_zombies1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import org.example.plants_vs_zombies1.controller.StartController;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        URL videoUrl = getClass().getResource("/media/intro_video.mp4");
        if (videoUrl != null) {
            Media media = new Media(videoUrl.toExternalForm());
            MediaPlayer player = new MediaPlayer(media);
            MediaView view = new MediaView(player);

            player.setOnReady(() -> {
                double width = media.getWidth();
                double height = media.getHeight();
                Scene scene = new Scene(new javafx.scene.Group(view), width, height);
                stage.setScene(scene);
                view.fitWidthProperty().bind(stage.widthProperty());
                view.fitHeightProperty().bind(stage.heightProperty());
                stage.setResizable(true);
                stage.setMinWidth(800);
                stage.setMinHeight(600);
                stage.show();
                player.play();
            });

            player.setOnEndOfMedia(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/plants_vs_zombies1/view/start.fxml"));
                    Parent root = loader.load();
                    StartController controller = loader.getController();
                    Scene startScene = new Scene(root);
                    stage.setScene(startScene);
                    stage.setTitle("Plants vs Zombies");
                    stage.setResizable(true);
                    stage.sizeToScene();
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/plants_vs_zombies1/view/start.fxml"));
            Parent root = loader.load();
            StartController controller = loader.getController();
            Scene startScene = new Scene(root);
            stage.setScene(startScene);
            stage.setTitle("Plants vs Zombies");
            stage.setResizable(true);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
