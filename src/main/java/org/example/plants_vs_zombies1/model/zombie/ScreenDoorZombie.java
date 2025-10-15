package org.example.plants_vs_zombies1.model.zombie;

import org.example.plants_vs_zombies1.model.enums.BulletType;
import org.example.plants_vs_zombies1.model.enums.ZombieType;

public class ScreenDoorZombie extends ResistantZombie {
    private boolean isShieldActive;

    public ScreenDoorZombie(int y, int x, double v) {
        super("ScreenDoorZombie", 300, 0.5, 25, x, y, ZombieType.SCREEN_DOOR_ZOMBIE, 300);
        this.isShieldActive = true;
    }

    @Override
    public void performAction() {
        System.out.println("🛡 ScreenDoorZombie moves forward with shield at (" + y + "," + x + ")!");
    }

    public void disableShield() {
        this.isShieldActive = false;
        this.extraResistance = 0;
        System.out.println("🛡 ScreenDoorZombie shield destroyed at (" + y + "," + x + ")!");
    }

    public boolean isShieldActive() {
        return isShieldActive;
    }

    @Override
    public void takeDamage(int damage) {
        if (isShieldActive) {
            extraResistance = Math.max(0, extraResistance - damage);
            System.out.println("🛡 ScreenDoorZombie shield took " + damage + " damage at (" + y + "," + x + "), Shield: " + extraResistance);
            if (extraResistance <= 0) {
                disableShield();
            }
        } else {
            super.takeDamage(damage);
        }
    }

    public void takeDamage(int damage, BulletType bulletType) {
        if (isShieldActive && bulletType != BulletType.SMOKE) {
            System.out.println("🛡 ScreenDoorZombie shield blocks non-Smoke bullet at (" + y + "," + x + ")!");
            return;
        }
        takeDamage(damage);
    }
}