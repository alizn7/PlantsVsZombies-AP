package org.example.plants_vs_zombies1.service;


public interface GameStatusListener {

    void onGameOver(boolean isWin);
    void onWaveStarted(int waveNumber);
    void onSunGenerated(int value);
}
