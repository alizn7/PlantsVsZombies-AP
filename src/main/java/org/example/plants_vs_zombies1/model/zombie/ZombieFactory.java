package org.example.plants_vs_zombies1.model.zombie;

import org.example.plants_vs_zombies1.model.enums.ZombieType;

public class ZombieFactory {
    public static Zombie createZombie(ZombieType zombieType, int y, int x) {
        switch (zombieType) {
            case BASIC_ZOMBIE:
                return new BasicZombie(y, x, 125.0);
            case CONEHEAD_ZOMBIE:
                return new ConeheadZombie(y, x, 125.0);
            case FLAG_ZOMBIE:
                return new FlagZombie(y, x, 250.0);
            case NEWSPAPER_ZOMBIE:
                return new NewspaperZombie(y, x, 250.0);
            case SCREEN_DOOR_ZOMBIE:
                return new ScreenDoorZombie(y, x, 250.0);
            default:
                throw new IllegalArgumentException("Unknown zombie type: " + zombieType);
        }
    }
}
