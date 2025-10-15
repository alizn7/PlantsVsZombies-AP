/*


ackage org.example.plants_vs_zombies1.service;

import org.example.plants_vs_zombies1.model.*;
import org.example.plants_vs_zombies1.model.board.Cell;
import org.example.plants_vs_zombies1.model.board.GameBoard;
import org.example.plants_vs_zombies1.model.collision.CollisionDetector;
import org.example.plants_vs_zombies1.model.collision.CollisionHandler;
import org.example.plants_vs_zombies1.model.enums.GameMode;
import org.example.plants_vs_zombies1.model.enums.PlantType;
import org.example.plants_vs_zombies1.model.zombie.Zombie;
import org.example.plants_vs_zombies1.model.zombie.ZombieFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FullCharacterTestMain {
    public static void main(String[] args) {
        // Initialize game components
        GameBoard board = new GameBoard();
        GameState gameState = new GameState(1, true); // Day mode
        HouseProtection houseProtection = new HouseProtection();
        Level level = new Level(1, GameMode.DAY);
        CollisionDetector detector = new CollisionDetector(board);
        CollisionHandler handler = new CollisionHandler(board);
        GameService gameService = new GameService(board, detector, handler, gameState, houseProtection, level);
        TestGameService test = new TestGameService(board, gameState);

        // Set up game status listener
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

        gameService.startGameLoop();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Add initial sun points
        scheduler.schedule(() -> {
            System.out.println("☀ Initial sun points added");
            test.addSunPoints(1000);
        }, 1, TimeUnit.SECONDS);

        // Add plants statically and dynamically
        scheduler.schedule(() -> tryPlant(test, board, PlantType.SCAREDY_SHROOM, 1, 8), 2, TimeUnit.SECONDS);
        scheduler.schedule(() -> tryPlant(test, board, PlantType.SUN_SHROOM, 2, 2), 2, TimeUnit.SECONDS);
        scheduler.schedule(() -> tryPlant(test, board, PlantType.FUME_SHROOM, 3, 3), 2, TimeUnit.SECONDS);

        // Add more plants dynamically
        List<PlantType> plantsToTest = Arrays.asList(
                PlantType.PEASHOOTER, PlantType.SUNFLOWER, PlantType.REPEATER, PlantType.SNOW_PEA,
                PlantType.WALL_NUT, PlantType.PUFF_SHROOM, PlantType.ICE_SHROOM,
                PlantType.DOOM_SHROOM, PlantType.CHERRY_BOMB
        );

        int delay = 3;
        for (PlantType type : plantsToTest) {
            int[] position = findEmptyCell(board);
            if (position != null) {
                int row = position[0];
                int col = position[1];
                scheduler.schedule(() -> tryPlant(test, board, type, row, col), delay, TimeUnit.SECONDS);
                delay += 1;
            } else {
                System.out.println("❌ No empty cell found for " + type);
            }
        }

        // Add zombies
        scheduler.schedule(() -> test.addZombieBasic(0, 9), 15, TimeUnit.SECONDS);
        scheduler.schedule(() -> test.addZombieConehead(1, 9), 16, TimeUnit.SECONDS);
        scheduler.schedule(() -> test.addZombieFlag(2, 9), 17, TimeUnit.SECONDS);
        scheduler.schedule(() -> test.addZombieScreenDoor(3, 9), 18, TimeUnit.SECONDS);
        scheduler.schedule(() -> test.addZombieNewspaper(4, 9), 19, TimeUnit.SECONDS);

        // Add zombie randomly at intervals
        scheduler.scheduleAtFixedRate(() -> test.addZombieRandomly(), 10, 5, TimeUnit.SECONDS);

        // Periodic sun operations
        scheduler.scheduleAtFixedRate(test::generateAndCollectSun, 5, 8, TimeUnit.SECONDS);

        // Stop everything after the final planting
        scheduler.schedule(() -> {
            System.out.println("✅ Test completed!");
            gameService.stopGame();
            scheduler.shutdownNow();
        }, Math.max(25, delay + 5), TimeUnit.SECONDS);
    }

    // Helper method to plant only if cell is still empty at time of execution
    private static void tryPlant(TestGameService test, GameBoard board, PlantType type, int row, int col) {
        Cell cell = board.getCell(row, col);
        if (cell != null && cell.getPlant() == null) {
            System.out.println("🌱 Planting " + type + " at (" + row + "," + col + ")");
            test.plant(type, row, col);
        } else {
            System.out.println("❌ Cell (" + row + "," + col + ") is not empty for " + type);
        }
    }

    private static int[] findEmptyCell(GameBoard board) {
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                Cell cell = board.getCell(row, col);
                if (cell != null && cell.getPlant() == null && cell.getZombie() == null) {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }
}
*/