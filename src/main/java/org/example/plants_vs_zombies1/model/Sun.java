package org.example.plants_vs_zombies1.model;

public class Sun {
    private int value;
    private int x;
    private int y;
    private boolean isCollected;

    public Sun(int value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.isCollected = false;
    }

    public int getValue() {
        return value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public void setValue(int value) {
        this.value = value;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void collect() {
        this.isCollected = true;
    }
}