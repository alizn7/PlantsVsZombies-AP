package org.example.plants_vs_zombies1.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundManager {
    private static boolean musicEnabled = true;
    private static boolean sfxEnabled = true;
    private static MediaPlayer backgroundPlayer;

    public static void playBackgroundMusic(String fileName) {
        if (!musicEnabled) return;

        stopBackgroundMusic();

        try {
            URL musicUrl = SoundManager.class.getResource("/media/" + fileName);
            if (musicUrl != null) {
                Media media = new Media(musicUrl.toExternalForm());
                backgroundPlayer = new MediaPlayer(media);
                backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                backgroundPlayer.play();
            } else {
                System.err.println("Music file not found: /media/" + fileName);
            }
        } catch (Exception e) {
            System.err.println("Error playing music /media/" + fileName + ": " + e.getMessage());
        }
    }

    public static void playBackgroundMusic() {
        playBackgroundMusic("background_music.mp3");
    }

    public static void stopBackgroundMusic() {
        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
            backgroundPlayer.dispose();
            backgroundPlayer = null;
        }
    }

    public static boolean isMusicEnabled() {
        return musicEnabled;
    }

    public static void setMusicEnabled(boolean enabled) {
        musicEnabled = enabled;
        if (enabled) {
            playBackgroundMusic();
        } else {
            stopBackgroundMusic();
        }
    }

    public static boolean isSfxEnabled() {
        return sfxEnabled;
    }

    public static void setSfxEnabled(boolean enabled) {
        sfxEnabled = enabled;
    }

    public static void playSfx(String fileName) {
        if (!sfxEnabled) return;
        try {
            URL soundUrl = SoundManager.class.getResource("/media/" + fileName);
            if (soundUrl != null) {
                Media media = new Media(soundUrl.toExternalForm());
                MediaPlayer sfx = new MediaPlayer(media);
                sfx.setOnEndOfMedia(() -> sfx.dispose()); // Dispose after playing
                sfx.play();
            } else {
                System.err.println("Sound file not found: /media/" + fileName);
            }
        } catch (Exception e) {
            System.err.println("Error playing sound effect /media/" + fileName + ": " + e.getMessage());
        }
    }
}