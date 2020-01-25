package com.battleships.logic;

import com.battleships.gui.audio.Source;
import com.battleships.gui.entities.Entity;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

/**
 * Class containing all information of a game that needs to be saved
 * so the game can be continued at a later point in time.
 *
 * @author Tim Staudenmaier
 */
public class SaveFile {

    /**
     * Logic grid of the player.
     */
    private Grid playerGrid;
    /**
     * Logic grid of the opponent.
     */
    private Grid opponentGrid;
    /**
     * Stats of this game.
     */
    private Stats stats;
    /**
     * TurnHandler this game used.
     */
    private TurnHandler turnHandler;
    /**
     * GameState this game was in. (see constants in {@link GameManager}.
     */
    private int gameState;
    /**
     * All ship entities on the players grid.
     */
    private List<Entity> ships;
    /**
     * All marker entities that were on the grids.
     */
    private List<Entity> markers;
    /**
     * List containing all positions for fire effects on the players grid grouped by the ships on which the fires are burning.
     */
    private Map<Entity, List<Vector3f>> burningFires;
    /**
     * List containing all sources for fire sounds on the players grid grouped by the ships on which the fires are burning.
     */
    private Map<Entity, List<Source>> burningFireSounds;

    /**
     * Settings of the saved game.
     */
    private Settings settings;

    /**
     * Array containing the ships that still need to be placed ordered by size.
     */
    private int[] shipsLeftPlacing;

    /**
     * Create a new SaveFile and save information about current game into it.
     */
    public SaveFile() {
        LogicManager logic = GameManager.getLogic();
        playerGrid = logic.getPlayerGrid();
        opponentGrid = logic.getOpponentGrid();
        stats = logic.getStats();
        turnHandler = logic.getTurnHandler();
        gameState = logic.getGameState();
        stats.saveTime();
        GridManager gridManager = GameManager.getGridManager();
        ships = gridManager.getShips();
        markers = gridManager.getMarkers();
        burningFires = gridManager.getBurningFires();
        burningFireSounds = gridManager.getBurningFireSounds();
        settings = GameManager.getSettings();
        shipsLeftPlacing = GameManager.getShipSelector().getShipCounts();
    }

    /**
     * @return Saved grid of the player.
     */
    public Grid getPlayerGrid() {
        return playerGrid;
    }

    /**
     * @return Saved grid of the opponent.
     */
    public Grid getOpponentGrid() {
        return opponentGrid;
    }

    /**
     * @return Saved stats of the game.
     */
    public Stats getStats() {
        return stats;
    }

    /**
     * @return Saved TurnHandler of the game.
     */
    public TurnHandler getTurnHandler() {
        return turnHandler;
    }

    /**
     * @return Saved state the game was in.
     */
    public int getGameState() {
        return gameState;
    }

    /**
     * @return Saved entities of all ships on the players grid.
     */
    public List<Entity> getShips() {
        return ships;
    }

    /**
     * @return Saved entities of all markers on the grids.
     */
    public List<Entity> getMarkers() {
        return markers;
    }

    /**
     * @return Saved positions for all fires on the players grid.
     */
    public Map<Entity, List<Vector3f>> getBurningFires() {
        return burningFires;
    }

    /**
     * @return Saved Sources for all fire sounds on the players grid.
     */
    public Map<Entity, List<Source>> getBurningFireSounds() {
        return burningFireSounds;
    }

    /**
     * @return Settings of this game.
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * @return Array containing the ships that still need to be placed ordered by size.
     */
    public int[] getShipsLeftPlacing() {
        return shipsLeftPlacing;
    }
}
