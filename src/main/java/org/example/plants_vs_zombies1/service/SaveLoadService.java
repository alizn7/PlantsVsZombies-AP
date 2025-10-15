package org.example.plants_vs_zombies1.service;

import org.example.plants_vs_zombies1.model.Player;
import org.example.plants_vs_zombies1.model.enums.PlantType;
import org.example.plants_vs_zombies1.util.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SaveLoadService {

    private static Player currentPlayer = null;

    public static void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean savePlayer(String username, String password) {
        String sql = "INSERT INTO Players (id, username, password, unlocked_plants, current_level, wins, losses, score, last_played) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String id = UUID.randomUUID().toString();
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            pstmt.setString(1, id);
            pstmt.setString(2, username);
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, "PEASHOOTER,SUNFLOWER");
            pstmt.setInt(5, 1);
            pstmt.setInt(6, 0);
            pstmt.setInt(7, 0);
            pstmt.setInt(8, 0);
            pstmt.setString(9, LocalDateTime.now().toString());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving player: " + e.getMessage());
            return false;
        }
    }

    public Player loadPlayer(String username, String password) {
        String sql = "SELECT * FROM Players WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    Player player = new Player(
                            rs.getString("id"),
                            username,
                            password
                    );
                    player.setCurrentLevel(rs.getInt("current_level"));
                    player.setWins(rs.getInt("wins"));
                    player.setLosses(rs.getInt("losses"));
                    player.setScore(rs.getInt("score"));

                    String unlockedStr = rs.getString("unlocked_plants");
                    if (unlockedStr != null) {
                        for (String p : unlockedStr.split(",")) {
                            try {
                                player.addUnlockedPlant(PlantType.valueOf(p));
                            } catch (IllegalArgumentException ignored) {}
                        }
                    }

                    String lastPlayedStr = rs.getString("last_played");
                    if (lastPlayedStr != null) {
                        player.setLastPlayed(LocalDateTime.parse(lastPlayedStr));
                    } else {
                        player.setLastPlayed(LocalDateTime.now());
                    }

                    setCurrentPlayer(player);
                    return player;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading player: " + e.getMessage());
        }
        return null;
    }

    public boolean updatePlayer(Player player) {
        String sql = "UPDATE Players SET unlocked_plants = ?, current_level = ?, wins = ?, losses = ?, score = ?, last_played = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String unlocked = player.getUnlockedPlants().stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(","));

            pstmt.setString(1, unlocked);
            pstmt.setInt(2, player.getCurrentLevel());
            pstmt.setInt(3, player.getWins());
            pstmt.setInt(4, player.getLosses());
            pstmt.setInt(5, player.getScore());
            pstmt.setString(6, player.getLastPlayed().toString());
            pstmt.setString(7, player.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating player: " + e.getMessage());
            return false;
        }
    }

    public List<Player> loadAllPlayers() {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM Players";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Player player = new Player(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                player.setCurrentLevel(rs.getInt("current_level"));
                player.setWins(rs.getInt("wins"));
                player.setLosses(rs.getInt("losses"));
                player.setScore(rs.getInt("score"));

                String unlockedStr = rs.getString("unlocked_plants");
                if (unlockedStr != null) {
                    for (String p : unlockedStr.split(",")) {
                        try {
                            player.addUnlockedPlant(PlantType.valueOf(p));
                        } catch (IllegalArgumentException ignored) {}
                    }
                }

                String lastPlayedStr = rs.getString("last_played");
                if (lastPlayedStr != null) {
                    player.setLastPlayed(LocalDateTime.parse(lastPlayedStr));
                } else {
                    player.setLastPlayed(LocalDateTime.now());
                }

                players.add(player);
            }
        } catch (SQLException e) {
            System.err.println("Error loading all players: " + e.getMessage());
        }
        return players;
    }
}
