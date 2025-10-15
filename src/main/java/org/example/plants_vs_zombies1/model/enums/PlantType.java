package org.example.plants_vs_zombies1.model.enums;

public enum PlantType {
    PEASHOOTER("Peashooter", true),
    SUNFLOWER("Sunflower", true),
    CHERRY_BOMB("CherryBomb", true),
    WALL_NUT("WallNut", true),
    SNOW_PEA("SnowPea", true),
    REPEATER("Repeater", true),
    PUFF_SHROOM("PuffShroom", false),
    SUN_SHROOM("SunShroom", false),
    FUME_SHROOM("FumeShroom", false),
    ICE_SHROOM("IceShroom", false),
    DOOM_SHROOM("DoomShroom", false),
    SCAREDY_SHROOM("ScaredyShroom", false);

    private final String name;
    private final boolean isDayMode;

    PlantType(String name, boolean isDayMode) {
        this.name = name;
        this.isDayMode = isDayMode;
    }

    public String getName() {
        return name;
    }

    public boolean isDayMode() {
        return isDayMode;
    }
}