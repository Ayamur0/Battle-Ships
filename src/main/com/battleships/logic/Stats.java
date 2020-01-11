package com.battleships.logic;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.window.WindowManager;
import org.lwjgl.glfw.GLFW;

import java.sql.Time;
import java.util.stream.IntStream;

/**
 * Class containing stats for the current game that get displayed at the end of the game.
 *
 * @author Tim Staudenmaier
 */
public class Stats{

    /**
     * Tiem played in seconds
     */
    private int playTime;
    /**
     * Time at which the game has started in seconds.
     */
    private int startTime;
    /**
     * Time at which the game has ended in seconds.
     */
    private int endTime;
    /**
     * Amount of rounds that have been played.
     */
    private int rounds;
    /**
     * Max amount of ships each player has.
     */
    private int maxShips;
    /**
     * Ships of the player that are still alive.
     */
    private int shipsAlive;
    /**
     * Amount of ships the player has sunk.
     */
    private int shipsDestroyed;
    /**
     * Accuracy with which the player has hit enemy ships.
     */
    private float accuracy;


    /**
     * Initialize the stats with values of the beginning of the game.
     */
    public void init(){
        startTime = (int)GLFW.glfwGetTime();
        rounds = 0;
        maxShips = IntStream.of(GameManager.getLogic().getEnemyShipsLeft()).sum();
        shipsAlive = maxShips;
        shipsDestroyed = 0;
        accuracy = 1;
    }

    /**
     * Restart the timer if these stats have been loaded from a {@link SaveFile}.
     */
    public void restartTime(){
        startTime = (int)GLFW.glfwGetTime();
    }

    /**
     * Saves the current played time.
     * Needed if these stats should be saved into a {@link SaveFile}.
     */
    public void saveTime(){
        playTime += endTime - startTime;
    }

    /**
     * Updates the stats to match current game state.
     */
    public void updateStats() {
        endTime = (int)GLFW.glfwGetTime();
        playTime += endTime - startTime;
        shipsAlive = IntStream.of(GameManager.getLogic().getPlayerShipsLeft()).sum();
        shipsDestroyed = maxShips - IntStream.of(GameManager.getLogic().getEnemyShipsLeft()).sum();
        int shipsHit  = 0;
        int[] enemyShips = GameManager.getLogic().getEnemyShipsLeft();
        for (int i = 0; i < enemyShips.length; i++){
            shipsHit += enemyShips[i] * (i + 2);
        }
        accuracy = (float) shipsHit / rounds;
    }

    /**
     * Increments the round counter.
     */
    public void addRound(){
        rounds++;
    }

    /**
     * @return Amount of time played in seconds.
     */
    public int getPlayTime() {
        return playTime;
    }

    /**
     * @return Amount of rounds played.
     */
    public int getRounds() {
        return rounds;
    }

    /**
     * @return Amount of ships a player had at the beginning.
     */
    public int getMaxShips() {
        return maxShips;
    }

    /**
     * @return Amount of ships still alive on the players grid.
     */
    public int getShipsAlive() {
        return shipsAlive;
    }

    /**
     * @return Amount of ships the player has destroyed.
     */
    public int getShipsDestroyed() {
        return shipsDestroyed;
    }

    /**
     * @return Accuracy with which the player has hit ships.
     */
    public float getAccuracy() {
        return accuracy;
    }
}
