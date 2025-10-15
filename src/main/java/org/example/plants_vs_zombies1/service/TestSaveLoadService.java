        package org.example.plants_vs_zombies1.service;

import org.example.plants_vs_zombies1.model.Player;

import java.util.List;

        public class TestSaveLoadService {
    public static void main(String[] args) {
        SaveLoadService service = new SaveLoadService();

        boolean saved = service.savePlayer("newuser", "newpass");
        System.out.println("Player saved: " + saved);

        Player player = service.loadPlayer("testuser1", "password123");
        if (player != null) {
            System.out.println("Loaded player: " + player.getUsername() + ", Score: " + player.getScore());
        }

        List<Player> highScores = service.loadAllPlayers( );
        System.out.println("High Scores:");
        highScores.forEach(System.out::println);
    }
}
