package org.example.plants_vs_zombies1.model;

import org.example.plants_vs_zombies1.model.enums.ZombieType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wave {
    private final int waveNumber;
    private final List<ZombieType> zombies;
    private final boolean isFinalWave;
    private final int levelNumber;

    public Wave(int waveNumber, int levelNumber) {
        this.waveNumber = waveNumber;
        this.levelNumber = levelNumber;
        this.isFinalWave = isFinalWave(waveNumber, levelNumber);
        this.zombies = generateZombiesForWave(waveNumber, levelNumber);
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public List<ZombieType> getZombies() {
        return zombies;
    }

    public boolean isFinalWave() {
        return isFinalWave;
    }

    private boolean isFinalWave(int wave, int level) {
        return (level == 1 && wave == 2) ||
                (level == 2 && wave == 4) ||
                (level == 3 && wave == 4) ||
                (level >= 4 && wave == 4);
    }

    private List<ZombieType> generateZombiesForWave(int wave, int level) {
        List<ZombieType> list = new ArrayList<>();
        Random random = new Random();
        int randomZombieCount;

        switch (level) {
            case 1 -> {
                if (wave == 1) {
                    randomZombieCount = 5 + random.nextInt(3);
                    for (int i = 0; i < randomZombieCount; i++) {
                        list.add(random.nextDouble() < 0.75 ? ZombieType.BASIC_ZOMBIE : ZombieType.CONEHEAD_ZOMBIE);
                    }
                } else if (wave == 2) {
                    for (int i = 0; i < 8; i++) {
                        list.add(random.nextDouble() < 0.55 ? ZombieType.BASIC_ZOMBIE : ZombieType.CONEHEAD_ZOMBIE);
                    }
                }
            }
            case 2 -> {
                if (wave == 1 || wave == 3) {
                    randomZombieCount = wave == 1 ? 5 + random.nextInt(3) : 4 + random.nextInt(2);
                    for (int i = 0; i < randomZombieCount; i++) {
                        double rand = random.nextDouble();
                        if (rand < 0.75) list.add(ZombieType.BASIC_ZOMBIE);
                        else if (rand < 0.95) list.add(ZombieType.CONEHEAD_ZOMBIE);
                        else list.add(ZombieType.FLAG_ZOMBIE);
                    }
                } else if (wave == 2) {
                    for (int i = 0; i < 5; i++) {
                        list.add(random.nextDouble() < 0.75 ? ZombieType.BASIC_ZOMBIE : ZombieType.CONEHEAD_ZOMBIE);
                    }
                } else if (wave == 4) {
                    for (int i = 0; i < 8; i++) {
                        double rand = random.nextDouble();
                        if (rand < 0.75) list.add(ZombieType.BASIC_ZOMBIE);
                        else if (rand < 0.95) list.add(ZombieType.CONEHEAD_ZOMBIE);
                        else list.add(ZombieType.FLAG_ZOMBIE);
                    }
                }
            }
            case 3 -> {
                if (wave == 1 || wave == 3) {
                    randomZombieCount = wave == 1 ? 5 + random.nextInt(3) : 4 + random.nextInt(2);
                    for (int i = 0; i < randomZombieCount; i++) {
                        double rand = random.nextDouble();
                        if (rand < 0.75) list.add(ZombieType.BASIC_ZOMBIE);
                        else if (rand < 0.9) list.add(ZombieType.CONEHEAD_ZOMBIE);
                        else list.add(ZombieType.NEWSPAPER_ZOMBIE);
                    }
                } else if (wave == 2) {
                    for (int i = 0; i < 5; i++) {
                        list.add(random.nextDouble() < 0.75 ? ZombieType.BASIC_ZOMBIE : ZombieType.CONEHEAD_ZOMBIE);
                    }
                } else if (wave == 4) {
                    for (int i = 0; i < 10; i++) {
                        double rand = random.nextDouble();
                        if (rand < 0.75) list.add(ZombieType.BASIC_ZOMBIE);
                        else if (rand < 0.9) list.add(ZombieType.CONEHEAD_ZOMBIE);
                        else list.add(ZombieType.NEWSPAPER_ZOMBIE);
                    }
                }
            }
            case 4, 5, 6 -> {
                if (wave == 1 || wave == 3) {
                    randomZombieCount = wave == 1 ? 5 + random.nextInt(3) : 4 + random.nextInt(2);
                    for (int i = 0; i < randomZombieCount; i++) {
                        double rand = random.nextDouble();
                        if (rand < 0.75) list.add(ZombieType.BASIC_ZOMBIE);
                        else if (rand < 0.85) list.add(ZombieType.CONEHEAD_ZOMBIE);
                        else if (rand < 0.95) list.add(ZombieType.NEWSPAPER_ZOMBIE);
                        else list.add(ZombieType.SCREEN_DOOR_ZOMBIE);
                    }
                } else if (wave == 2) {
                    for (int i = 0; i < 5; i++) {
                        double rand = random.nextDouble();
                        if (rand < 0.75) list.add(ZombieType.BASIC_ZOMBIE);
                        else if (rand < 0.95) list.add(ZombieType.CONEHEAD_ZOMBIE);
                        else list.add(ZombieType.NEWSPAPER_ZOMBIE);
                    }
                } else if (wave == 4) {
                    int count = level == 6 ? 10 : 8;
                    for (int i = 0; i < count; i++) {
                        double rand = random.nextDouble();
                        if (rand < 0.75) list.add(ZombieType.BASIC_ZOMBIE);
                        else if (rand < 0.85) list.add(ZombieType.CONEHEAD_ZOMBIE);
                        else if (rand < 0.95) list.add(ZombieType.NEWSPAPER_ZOMBIE);
                        else list.add(ZombieType.SCREEN_DOOR_ZOMBIE);
                    }
                }
            }
        }
        return list;
    }
}