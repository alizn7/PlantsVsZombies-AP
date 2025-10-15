package org.example.plants_vs_zombies1.model;

import java.util.stream.Collectors;

public class PlayerRow {
    private int rank;
    private final String username;
    private final int level;
    private final int wins;
    private final int losses;
    private final int score;
    private final String lastPlayed;
    private final String unlockedPlants;

    public PlayerRow(Player player) {
        this.username = player.getUsername();
        this.level = player.getCurrentLevel();
        this.wins = player.getWins();
        this.losses = player.getLosses();
        this.score = player.getScore();
        this.lastPlayed = player.getLastPlayedFormatted();
        this.unlockedPlants = player.getUnlockedPlants()
                .stream()
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public int getLevel() {
        return level;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getScore() {
        return score;
    }

    public String getLastPlayed() {
        return lastPlayed;
    }

    public String getUnlockedPlants() {
        return unlockedPlants;
    }
}
