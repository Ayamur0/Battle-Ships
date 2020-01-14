package com.battleships.logic;

import com.battleships.gui.gameAssets.GameManager;

/**
 * Class containing all currently set settings.
 */
public class Settings {

    /**
     * Constants representing ai levels.
     */
    public static final int EASY = 0, MEDIUM = 1, HARD = 2;

    /**
     * Level the ai for the player in the current game use.
     */
    private int aiLevelP = -1;

    /**
     * Level the ai for the opponent in the current game use.
     */
    private int aiLevelO = -1;
    /**
     * Size the grid of the next game should have.
     */
    private int size = 12;
    /**
     * {@code true} if the game is played online, {@code false} if the game is played offline.
     */
    private boolean online;

    /**
     * {@code true} if sound is enabled, {@code false} else.
     */
    private boolean sound;

    /**
     * {@code true} if animations are enabled, {@code false} else.
     */
    private boolean animation;

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
        GameManager.getLogic().onlineMode(online);
        this.online = online;
    }

    /**
     * @return Level the AI of the player in the current game uses.
     */
    public int getAiLevelP() {
        return aiLevelP;
    }

    /**
     * Sets the level the AI for the player should use during the next game.
     * @param aiLevel Level the AI for the player in the next game should use.
     */
    public void setAiLevelP(int aiLevel) {
        this.aiLevelP = aiLevel;
    }

    /**
     * @return Level the AI of the opponent in the current game uses.
     */
    public int getAiLevelO() {
        return aiLevelO;
    }

    /**
     * Sets the level the AI for the opponent should use during the next game.
     * @param aiLevelO Level the AI for the opponent in the next game should use.
     */
    public void setAiLevelO(int aiLevelO) {
        this.aiLevelO = aiLevelO;
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

    /**
     * @return {@code true} if sound is enabled, {@code false} else.
     */
    public boolean isSound() {
        return sound;
    }

    /**
     * This value needs to be set, if sound is being enabled or disabled.
     * @param sound {@code true} if sound gets enabled, {@code false} if it gets disabled.
     */
    public void setSound(boolean sound) {
        this.sound = sound;
    }

    /**
     * @return {@code true} if animations are enabled, {@code false} else.
     */
    public boolean isAnimation() {
        return animation;
    }

    /**
     * This value needs to be set, if animations are being enabled or disabled.
     * @param animation {@code true} if animations get enabled, {@code false} if they get disabled.
     */
    public void setAnimation(boolean animation) {
        this.animation = animation;
    }
}
