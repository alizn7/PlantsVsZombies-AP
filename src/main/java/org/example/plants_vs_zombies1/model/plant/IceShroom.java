package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.Sun;
import org.example.plants_vs_zombies1.model.enums.PlantType;
import org.example.plants_vs_zombies1.model.zombie.Zombie;

import java.util.List;

public class IceShroom extends ExplosivePlant {
    public IceShroom(int x, int y) {
        super("IceShroom", 75, 100, x, y, PlantType.ICE_SHROOM, 50, 9);
    }

    @Override
    public void performAction() {
        if (isReadyToExplode()) {
            List<Zombie> zombies = board.getZombies();
            for (Zombie zombie : zombies) {
                if (!zombie.isDead()) {
                    zombie.setSpeed(zombie.getSpeed() / 2);
                    System.out.println("IceShroom freezes " + zombie.getName() + " at (" + zombie.getY() + "," + zombie.getX() + ")");
                }
            }
            setHasExploded(true);
            System.out.println("IceShroom freezes all zombies on the board from (" + x + "," + y + ")!");
        }
    }
}
