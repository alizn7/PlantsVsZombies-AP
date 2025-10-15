package org.example.plants_vs_zombies1.service;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.example.plants_vs_zombies1.model.plant.Plant;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PlantRenderer {
    private final Map<Plant, ImageView> plantViews = new HashMap<>();
    private final Map<Plant, Timeline> plantTimelines = new HashMap<>();
    private final Random random = new Random();

    public void renderPlant(Plant plant, int row, int col) {
        String gifPath = "/media/plants/" + plant.getName().toLowerCase() + ".gif";
        Image image = new Image(getClass().getResource(gifPath).toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);
        imageView.setX(col * 80);
        imageView.setY(row * 80);
        plantViews.put(plant, imageView);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setDelay(Duration.millis(random.nextInt(500)));
        timeline.play();
        plantTimelines.put(plant, timeline);

    }

    public void removePlant(Plant plant) {
        ImageView imageView = plantViews.remove(plant);
        Timeline timeline = plantTimelines.remove(plant);
        if (imageView != null) {
        }
        if (timeline != null) {
            timeline.stop();
        }
    }
}