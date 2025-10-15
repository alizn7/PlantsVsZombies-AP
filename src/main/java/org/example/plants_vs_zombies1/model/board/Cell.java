package org.example.plants_vs_zombies1.model.board;

import org.example.plants_vs_zombies1.model.Sun;
import org.example.plants_vs_zombies1.model.enums.CellType;
import org.example.plants_vs_zombies1.model.plant.Plant;
import org.example.plants_vs_zombies1.model.zombie.Zombie;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private final int x;
    private final int y;
    private Plant plant;
    private Zombie zombie;
    private final List<Sun> suns;
    private CellType type;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.suns = new ArrayList<>();
        this.type = CellType.EMPTY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Zombie getZombie() {
        return zombie;
    }

    public void setZombie(Zombie zombie) {
        this.zombie = zombie;
    }

    public List<Sun> getSuns() {
        return suns;
    }

    public void addSun(Sun sun) {
        if (sun != null) {
            suns.add(sun);
        }
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Cell[x=" + x + ", y=" + y + ", type=" + type + ", plant=" + (plant != null ? plant.getName() : "none") +
                ", zombie=" + (zombie != null ? zombie.getName() : "none") + ", suns=" + suns.size() + "]";
    }
}
