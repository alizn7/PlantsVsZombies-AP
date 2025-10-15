package org.example.plants_vs_zombies1.model.enums;

public enum ZombieType {
    BASIC_ZOMBIE("BasicZombie", true),
    CONEHEAD_ZOMBIE("ConeheadZombie", true),
    FLAG_ZOMBIE("FlagZombie", true),
    NEWSPAPER_ZOMBIE("NewspaperZombie", false),
    SCREEN_DOOR_ZOMBIE("ScreenDoorZombie", false);

    private final String name;
    private final boolean isDayMode;

    ZombieType(String name, boolean isDayMode) {
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
