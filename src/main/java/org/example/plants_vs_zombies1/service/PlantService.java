package org.example.plants_vs_zombies1.service;

import org.example.plants_vs_zombies1.model.Sun;
import org.example.plants_vs_zombies1.model.Bullet;
import org.example.plants_vs_zombies1.model.plant.Plant;
import org.example.plants_vs_zombies1.model.plant.SunProducerPlant;
import org.example.plants_vs_zombies1.model.plant.WarriorPlant;
import org.example.plants_vs_zombies1.model.plant.ExplosivePlant;
import org.example.plants_vs_zombies1.model.board.GameBoard;
import org.example.plants_vs_zombies1.model.collision.CollisionDetector;
import org.example.plants_vs_zombies1.model.collision.CollisionHandler;
import org.example.plants_vs_zombies1.model.zombie.Zombie;

import java.util.ArrayList;
import java.util.List;


public class PlantService {
    private final GameBoard gameBoard;
    private final CollisionDetector detector;
    private final CollisionHandler handler;

    public PlantService(GameBoard gameBoard,
                        CollisionDetector detector,
                        CollisionHandler handler) {
        this.gameBoard = gameBoard;
        this.detector = detector;
        this.handler = handler;
    }

    public boolean plant(Plant plant, int row, int col) {
        return gameBoard.placePlant(plant, row, col);
    }


    public void removePlant(int row, int col) {
        gameBoard.removePlant(row, col);
    }


    public void triggerPlants() {
        for (int r = 0; r < gameBoard.getRows(); r++) {
            for (int c = 0; c < gameBoard.getCols(); c++) {
                Plant plant = gameBoard.getCell(r, c).getPlant();
                if (plant == null || plant.isDead()) continue;

                plant.performAction();

                if (plant instanceof SunProducerPlant) {
                    SunProducerPlant sp = (SunProducerPlant) plant;
                    Sun sun = sp.produceSun();
                    if (sun != null) {
                        gameBoard.placeSun(sun, r, c);
                    }
                } else if (plant instanceof WarriorPlant) {
                    WarriorPlant wp = (WarriorPlant) plant;
                    List<Bullet> shots = wp.shoot();
                    for (Bullet bullet : shots) {
                        gameBoard.addBullet(bullet);
                    }
                } else if (plant instanceof ExplosivePlant) {
                    ExplosivePlant ep = (ExplosivePlant) plant;
                    if (ep.isReadyToExplode()) {
                        List<Zombie> targets = detector.detectExplosionCollisions(r, c, ep.getBlastRange());
                        handler.handleExplosionCollision(ep, targets);
                    }
                }
            }
        }
    }
}
