package org.example.plants_vs_zombies1.model.plant;

import org.example.plants_vs_zombies1.model.board.GameBoard;
import org.example.plants_vs_zombies1.model.enums.PlantType;

public abstract class Plant {
    protected String name;
    protected int cost;
    protected int health;
    protected int x, y;
    protected PlantType plantType;
    protected GameBoard board;

    public Plant(String name, int cost, int health, int x, int y, PlantType plantType) {
        this.name = name;
        this.cost = cost;
        this.health = health;
        this.x = x;
        this.y = y;
        this.plantType = plantType;
    }

    public abstract void performAction();

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getHealth() {
        return health;
    }

    public PlantType getPlantType() {
        return plantType;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPlantType(PlantType plantType) {
        this.plantType = plantType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public GameBoard getBoard() {
        return board;
    }

    public void setBoard(GameBoard board) {
        this.board = board;
    }

    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
        System.out.println("Plant " + name + " took " + damage + " damage, Health: " + health);
    }

    public boolean isDead() {
        return health <= 0;
    }
}