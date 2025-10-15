package org.example.plants_vs_zombies1.model;

import org.example.plants_vs_zombies1.model.enums.PlantType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String id;
    private String username;
    private String password;
    private List<PlantType> unlockedPlants;
    private int currentLevel;
    private int wins;
    private int losses;
    private int score;
    private LocalDateTime lastPlayed;

    public Player(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.unlockedPlants = new ArrayList<>();
        this.currentLevel = 1;
        this.wins = 0;
        this.losses = 0;
        this.score = 0;
        this.lastPlayed = LocalDateTime.now();
    }

    public String getLastPlayedFormatted() {
        return lastPlayed != null ? lastPlayed.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "-";
    }

    public LocalDateTime getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(LocalDateTime lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<PlantType> getUnlockedPlants() {
        return unlockedPlants;
    }

    public int getCurrentLevel() {
        return currentLevel;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUnlockedPlants(List<PlantType> unlockedPlants) {
        this.unlockedPlants = unlockedPlants;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void setWins(int wins) {
        this.wins = wins;
        updateScore();
    }

    public void setLosses(int losses) {
        this.losses = losses;
        updateScore();
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addUnlockedPlant(PlantType plantType) {
        if (!unlockedPlants.contains(plantType)) {
            unlockedPlants.add(plantType);
        }
    }

    public void incrementWins() {
        this.wins++;
        updateScore();
    }

    public void incrementLosses() {
        this.losses++;
        updateScore();
    }

    private void updateScore() {
        this.score = (wins * 10) - (losses * 5) + (unlockedPlants.size() * 20);
    }
}
