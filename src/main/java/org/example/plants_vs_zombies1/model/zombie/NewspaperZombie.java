package org.example.plants_vs_zombies1.model.zombie;

import org.example.plants_vs_zombies1.model.enums.ZombieType;

public class NewspaperZombie extends SpecialZombie {
    private boolean isEnraged;

    public NewspaperZombie(int y, int x, double v) {
        super("NewspaperZombie", 250, 0.5, 25, x, y, ZombieType.NEWSPAPER_ZOMBIE, "Enrage");
        this.isEnraged = false;
    }


    public void enrage() {
        this.isEnraged = true;
        this.speed = 1.0;
        System.out.println(" NewspaperZombie at (" + y + "," + x + ") is enraged! Speed increased to " + speed + "!");
    }

    @Override
    public void performAction() {
        if (isEnraged) {
            System.out.println(" NewspaperZombie moves faster and attacks aggressively at (" + y + "," + x + ") with speed " + speed + "!");
        } else {
            System.out.println(" NewspaperZombie moves forward with newspaper at (" + y + "," + x + ")!");
        }
    }

    public boolean isEnraged() {
        return isEnraged;
    }

    public void setEnraged(boolean enraged) {
        this.isEnraged = enraged;
    }


    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if (!isEnraged && getHealth() <= 100) {
            enrage();
        }
    }
}