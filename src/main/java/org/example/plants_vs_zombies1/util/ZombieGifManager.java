package org.example.plants_vs_zombies1.util;

import org.example.plants_vs_zombies1.model.enums.ZombieType;
import java.util.logging.Logger;

public class ZombieGifManager {
    private static final Logger LOGGER = Logger.getLogger(ZombieGifManager.class.getName());

    public enum ZombieState {
        WALKING,
        ATTACKING,
        DYING,
        HEADLESS,
        LOST_NEWSPAPER
    }

    public static String getGifPath(ZombieType type, ZombieState state) {
        LOGGER.fine(() -> "Getting GIF path for ZombieType: " + type + ", State: " + state);
        String basePath = "/media/zombies/";
        String path = switch (type) {
            case BASIC_ZOMBIE -> getBasicZombieGif(state);
            case CONEHEAD_ZOMBIE -> getConeheadZombieGif(state);
            case FLAG_ZOMBIE -> getFlagZombieGif(state);
            case NEWSPAPER_ZOMBIE -> getNewspaperZombieGif(state);
            case SCREEN_DOOR_ZOMBIE -> getScreenDoorZombieGif(state);
            default -> null;
        };
        if (path == null || ZombieGifManager.class.getResource(path) == null) {
            LOGGER.severe("GIF file not found for ZombieType: " + type + ", State: " + state + ", Path: " + path);
            return basePath + "zombie_walk.gif";
        }
        return path;
    }

    private static String getBasicZombieGif(ZombieState state) {
        String basePath = "/media/zombies/";
        return switch (state) {
            case WALKING -> basePath + "zombie_walk.gif";
            case ATTACKING -> basePath + "zombie_attack.gif";
            case DYING -> basePath + "zombie_die.gif";
            case HEADLESS -> basePath + "zombie_head_fly.gif";
            default -> basePath + "zombie_walk.gif";
        };
    }

    private static String getConeheadZombieGif(ZombieState state) {
        String basePath = "/media/zombies/";
        return switch (state) {
            case WALKING -> basePath + "cone_zombie_walk.gif";
            case ATTACKING -> basePath + "cone_zombie_attack.gif";
            case DYING -> basePath + "zombie_die.gif";
            case HEADLESS -> basePath + "zombie_head_fly.gif";
            default -> basePath + "cone_zombie_walk.gif";
        };
    }

    private static String getFlagZombieGif(ZombieState state) {
        String basePath = "/media/zombies/";
        return switch (state) {
            case WALKING -> basePath + "flag_zombie_walk.gif";
            case ATTACKING -> basePath + "flag_zombie_attack.gif";
            case DYING -> basePath + "zombie_die.gif";
            case HEADLESS -> basePath + "zombie_head_fly.gif";
            default -> basePath + "flag_zombie_walk.gif";
        };
    }

    private static String getNewspaperZombieGif(ZombieState state) {
        String basePath = "/media/zombies/newspaper/";
        return switch (state) {
            case WALKING -> basePath + "HeadWalk1.gif";
            case ATTACKING -> basePath + "HeadAttack1.gif";
            case HEADLESS -> basePath + "HeadWalk0.gif";
            case LOST_NEWSPAPER -> basePath + "LostNewspaper.gif";
            case DYING -> basePath + "Die.gif";
            default -> basePath + "HeadWalk1.gif";
        };
    }

    private static String getScreenDoorZombieGif(ZombieState state) {
        String basePath = "/media/zombies/";
        return switch (state) {
            case WALKING -> basePath + "screendoor_zombie_walk.gif";
            case ATTACKING -> basePath + "screendoor_zombie_attack.gif";
            case DYING -> basePath + "zombie_die.gif";
            case HEADLESS -> basePath + "zombie_head_fly.gif";
            default -> basePath + "screendoor_zombie_walk.gif";
        };
    }
}