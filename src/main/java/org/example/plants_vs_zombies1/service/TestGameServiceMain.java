/* package org.example.plants_vs_zombies1.service;

import org.example.plants_vs_zombies1.model.*;
import org.example.plants_vs_zombies1.model.board.GameBoard;
import org.example.plants_vs_zombies1.model.collision.CollisionDetector;
import org.example.plants_vs_zombies1.model.collision.CollisionHandler;
import org.example.plants_vs_zombies1.model.enums.GameMode;
import org.example.plants_vs_zombies1.model.plant.Sunflower;

public class TestGameServiceMain {
    public static void main(String[] args) {
        // Initialize game components
        GameBoard gameBoard = new GameBoard();
        GameState gameState = new GameState(1, true); // Day mode
        HouseProtection houseProtection = new HouseProtection();
        Level level = new Level(1, GameMode.DAY);
        CollisionDetector detector = new CollisionDetector(gameBoard);
        CollisionHandler handler = new CollisionHandler(gameBoard);

        GameService gameService = new GameService(gameBoard, detector, handler, gameState, houseProtection, level);
        gameService.setGameStatusListener(new GameService.GameStatusListener() {
            @Override
            public void onGameOver(boolean isWin) {
                System.out.println("Game Over: " + (isWin ? "You Win!" : "You Lose!"));
            }

            @Override
            public void onWaveStarted(int waveNumber) {
                System.out.println("Wave " + waveNumber + " started!");
            }
        });

        // Place a plant
        gameBoard.placePlant(new Sunflower(1, 1), 1, 1);

        // Start game loop
        gameService.startGameLoop();

        // Save/load service
        SaveLoadService saveLoadService = new SaveLoadService();

        // Save player
        String username = "testUser";
        String password = "testPass";
        if (saveLoadService.savePlayer(username, password)) {
            System.out.println("Player saved successfully");
        }

        // Load player
        Player player = saveLoadService.loadPlayer(username, password);
        if (player != null) {
            System.out.println("Player loaded: " + player.getUsername());
            SaveLoadService.setCurrentPlayer(player); // ست کردن بازیکن فعال
        }

        // Save game state
        Player currentPlayer = SaveLoadService.getCurrentPlayer();
        if (currentPlayer != null && saveLoadService.saveGameState(currentPlayer.getId(), gameState, gameBoard, houseProtection)) {
            System.out.println("Game state saved successfully");
        }

        // Load game state
        if (currentPlayer != null) {
            SaveLoadService.GameStateData stateData = saveLoadService.loadGameState(currentPlayer.getId(), new GameBoard());
            if (stateData != null) {
                System.out.println("Game state loaded: Level=" + stateData.getGameState().getLevelNumber() +
                        ", SunPoints=" + stateData.getGameState().getSunPoints());
            }
        }

        // Simulate game for a few seconds
        try {
            Thread.sleep(20000); // Run for 20 seconds
        } catch (InterruptedException e) {
            System.err.println("Test interrupted: " + e.getMessage());
        }

        // Stop game
        gameService.stopGame();
    }
}
*/