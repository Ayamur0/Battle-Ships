package com.battleships.logic;

/**
 * Class containing all currently set settings.
 */
public class Settings {

    /**
     * Constants representing ai levels.
     */
    private static final int EASY = 0, MEDIUM = 1, HARD = 2;

    /**
     * Level the ai's in the current game use.
     */
    private int aiLevel;
    /**
     * Size the grid of the next game should have.
     */
    private int size = 30;
    /**
     * {@code true} if the game is played online, {@code false} if the game is played offline.
     */
    private boolean online;

    /**
     * @return {@code true} if the game is played online, {@code false} if the game is played offline.
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Set whether the game is played online or offline.
     * @param online {@code true} if the game is played online, {@code false} if the game is played offline.
     */
    public void setOnline(boolean online) {
        this.online = online;
    }

    /**
     * @return Level the AI's in the current game use.
     */
    public int getAiLevel() {
        return aiLevel;
    }

    /**
     * Sets the level the AI's should use during the next game.
     * @param aiLevel Level the AI's in the next game should use.
     */
    public void setAiLevel(int aiLevel) {
        this.aiLevel = aiLevel;
    }

    /**
     * @return Size of the grids in the current game.
     */
    public int getSize() {
        return size;
    }

    /**
     * Set the size for the grids in the next game.
     * @param size Size the grids should have in the next game.
     */
    public void setSize(int size) {
        this.size = size;
    }
}
