package org.example.plants_vs_zombies1.service;

import org.example.plants_vs_zombies1.model.Sun;
import org.example.plants_vs_zombies1.model.board.GameBoard;

public class SunService {
    private final GameBoard board;

    public SunService(GameBoard board) {
        this.board = board;
    }

    public void addSunToCell(int row, int col) {
        Sun sun = new Sun(25, col, row);
        board.placeSun(sun, row, col);
    }

    public int collectSunsAt(int row, int col) {
        return board.collectSun(row, col);
    }
}
