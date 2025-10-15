package org.example.plants_vs_zombies1.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.plants_vs_zombies1.model.Level;
import org.example.plants_vs_zombies1.model.enums.PlantType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class CardLoaderService {

    public static List<ImageView> loadCardsForLevel(Level level, Consumer<PlantType> onSelect) {
        List<ImageView> cards = new ArrayList<>();
        int lvl = level.getLevelNumber();

        if (lvl >= 1) {
            cards.add(createCard("/media/plants/PEASHOOTER.jpg", PlantType.PEASHOOTER, onSelect));
            cards.add(createCard("/media/plants/SUNFLOWER.jpg", PlantType.SUNFLOWER, onSelect));
        }

        if (lvl >= 2) {
            cards.add(createCard("/media/plants/PEASHOOTER.jpg", PlantType.PEASHOOTER, onSelect));
            cards.add(createCard("/media/plants/SUNFLOWER.jpg", PlantType.SUNFLOWER, onSelect));
            cards.add(createCard("/media/plants/WALL_NUT.jpg", PlantType.WALL_NUT, onSelect));
            cards.add(createCard("/media/plants/SNOW_PEA.jpg", PlantType.SNOW_PEA, onSelect));
        }

        if (lvl >= 3) {
            cards.add(createCard("/media/plants/PEASHOOTER.jpg", PlantType.PEASHOOTER, onSelect));
            cards.add(createCard("/media/plants/SUNFLOWER.jpg", PlantType.SUNFLOWER, onSelect));
            cards.add(createCard("/media/plants/WALL_NUT.jpg", PlantType.WALL_NUT, onSelect));
            cards.add(createCard("/media/plants/SNOW_PEA.jpg", PlantType.SNOW_PEA, onSelect));
            cards.add(createCard("/media/plants/CHERRY_BOMB.jpg", PlantType.CHERRY_BOMB, onSelect));
            cards.add(createCard("/media/plants/REPEATER.jpg", PlantType.REPEATER, onSelect));
        }


        if (lvl >= 4) {
            cards.add(createCard("/media/plants/WALL_NUT.jpg", PlantType.WALL_NUT, onSelect));
            cards.add(createCard("/media/plants/WALL_NUT.jpg", PlantType.SUN_SHROOM, onSelect));
            cards.add(createCard("/media/plants/WALL_NUT.jpg", PlantType.PUFF_SHROOM, onSelect));
            cards.add(createCard("/media/plants/CHERRY_BOMB.jpg", PlantType.CHERRY_BOMB, onSelect));
            cards.add(createCard("/media/plants/WALL_NUT.jpg", PlantType.WALL_NUT, onSelect));

        }
        //TODO: REST OF LEVELS

        return cards;
    }

    private static ImageView createCard(String path, PlantType type, Consumer<PlantType> onSelect) {
        ImageView iv = new ImageView(new Image(CardLoaderService.class.getResource(path).toExternalForm()));
        iv.setFitWidth(60);
        iv.setFitHeight(60);
        iv.setOnMouseClicked(e -> onSelect.accept(type));
        return iv;
    }
}
