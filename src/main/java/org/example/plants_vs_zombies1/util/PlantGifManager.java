package org.example.plants_vs_zombies1.util;

import org.example.plants_vs_zombies1.model.enums.PlantType;

public class PlantGifManager {
    public static String getGifPath(PlantType type) {
        return switch (type) {
            case SUNFLOWER -> "/media/plants/SunFlower1.gif";
            case PEASHOOTER -> "/media/plants/Peashooter.gif";
            case REPEATER -> "/media/plants/Repeater.gif";
            case SNOW_PEA -> "/media/plants/SnowPea.gif";
            case WALL_NUT -> "/media/plants/WallNut.gif";
            case PUFF_SHROOM -> "/media/plants/PuffShroom.gif";
            case SUN_SHROOM -> "/media/plants/SunShroom.gif";
            case FUME_SHROOM -> "/media/plants/FumeShroom.gif";
            case ICE_SHROOM -> "/media/plants/IceShroom.gif";
            case DOOM_SHROOM -> "/media/plants/DoomShroom.gif";
            case SCAREDY_SHROOM -> "/media/plants/ScaredyShroom.gif";
            case CHERRY_BOMB -> "/media/plants/CherryBomb.gif";
        };
    }
}
