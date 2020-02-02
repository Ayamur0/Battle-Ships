package com.battleships.logic.AI;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.logic.Grid;
import com.battleships.logic.LogicManager;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of an AI with medium difficulty.
 * This AI will first choose either a {@link PatternX}, {@link PatternChess} or {@link PatternLines} to shoot.
 * After that pattern is done it will shoot randomly.
 * <p>
 * If it hits a ship it will try to sink that ship before moving on.
 *
 * @author Tim Staudenmaier
 */
public class AIMedium extends AI {

    /**
     * Constants for directions a hit ship can have.
     */
    private static final int UNKNOWN = -1, HORIZONTAL = 0, VERTICAL = 1;
    /**
     * Constants for what was hit during last shot.
     */
    private static final int ERROR = -1, NA = 0, SHIP = 1, WATER = 2;

    /**
     * Pattern in which the AI will shoot.
     */
    protected Pattern pattern;
    /**
     * Index at which the last shot was made.
     */
    private Vector2i lastShot;
    /**
     * Direction of the ship that was found and the AI is now trying to sink.
     * Is {@value UNKNOWN} while the AI isn't trying to sink a ship or while it hasn't found out
     * which direction the ship has.
     */
    private int foundShipDir = UNKNOWN;
    /**
     * Grid of the opponent of this AI.
     */
    private Grid opponentGrid;
    /**
     * List containing all the cells in which a ship part was hit.
     * Needed to fully sink that ship.
     * Is empty again after the ship has been sunk.
     */
    private List<Vector2i> hitCells = new ArrayList<>();
    /**
     * Saves the last direction the AI tested for a ship.
     * Needed if the game is online and the AI doesn't immediately get an answer.
     */
    private int lastTried;

    /**
     * Creates a new AI with medium difficulty.
     *
     * @param team     Team this ai should play for (0 or 1 as in {@link GridManager})
     * @param gridSize Size of the grid this ai should play on.
     * @param manager  LogicManager this ai should use to shoot and place ships.
     */
    public AIMedium(int team, int gridSize, LogicManager manager) {
        super(team, gridSize, manager);
        pattern = AI.choosePattern(gridSize);
        opponentGrid = team == GridManager.OWNFIELD ? manager.getOpponentGrid() : manager.getPlayerGrid();
    }

    /**
     * AI makes their next turn.
     */
    @Override
    public void makeTurn() {
        opponentGrid = team == GridManager.OWNFIELD ? manager.getOpponentGrid() : manager.getPlayerGrid();
        if (hitCells.size() != 0 && foundShipDir == UNKNOWN) {
            findFoundShipDir();
            return;
        }
        if (hitCells.size() != 0) {
            if (shootFoundShip(new Vector2i(hitCells.get(0))))
                return;
            hitCells.remove(0);
            makeTurn();
            return;
        }
        lastTried = foundShipDir = UNKNOWN;
        if (lastShot == null) {
            lastShot = pattern.firstIndex();
            if (shootCell(lastShot) == SHIP) {
                hitCells.add(lastShot);
            }
            return;
        }
        lastShot = pattern.nextIndex();
        if (lastShot == null) {
            updatePattern();
            lastShot = pattern.firstIndex();
        }
        int result;
        while ((result = shootCell(lastShot)) == NA || result == ERROR) {
            if (result == ERROR) {
                lastShot = pattern.nextIndex();
                if (lastShot == null) {
                    updatePattern();
                    lastShot = pattern.firstIndex();
                }
                continue;
            }
            if (manager.hasBeenShot(lastShot.x, lastShot.y, team == GridManager.OWNFIELD ? GridManager.OPPONENTFIELD : GridManager.OWNFIELD))
                lastShot = pattern.nextIndex();
            if (lastShot == null) {
                updatePattern();
                lastShot = pattern.firstIndex();
            }
        }
        if (result == SHIP) {
            hitCells.add(new Vector2i(lastShot));
        }
    }

    /**
     * AI tries to sink a ship. For this method the AI needs to already know the direction of the ship.
     * To sink a ship the AI tries to shoot the cells around the last shot in the direction the ship lies in.
     *
     * @param lastShot Last Shot the AI has made.
     * @return {@code true} if the AI has made a shot (regardless if it hit a ship or not), {@code false} if the AI couldn't make a shot for the passed cell.
     */
    protected boolean shootFoundShip(Vector2i lastShot) {
        if (foundShipDir == HORIZONTAL) {

            lastShot.x -= 1;
            switch (shootCell(lastShot)) {
                case SHIP:
                    hitCells.add(new Vector2i(lastShot));
                    return true;
                case WATER:
                    return true;
            }
            lastShot.x += 1;


            lastShot.x += 1;
            switch (shootCell(lastShot)) {
                case SHIP:
                    hitCells.add(new Vector2i(lastShot));
                    return true;
                case WATER:
                    return true;
            }
            lastShot.x -= 1;

        }

        if (foundShipDir == VERTICAL) {
            lastShot.y += 1;
            switch (shootCell(lastShot)) {
                case SHIP:
                    hitCells.add(new Vector2i(lastShot));
                    return true;
                case WATER:
                    return true;
            }
            lastShot.y -= 1;
            lastShot.y -= 1;
            switch (shootCell(lastShot)) {
                case SHIP:
                    hitCells.add(new Vector2i(lastShot));
                    return true;
                case WATER:
                    return true;

            }
            lastShot.y += 1;
        }

        return false;
    }

    /**
     * Executes shoot command and tells AI if it hit or couldn't make the shot.
     *
     * @param cell Index of the cell to shoot.
     * @return {@value ERROR} if the cell is outside of the grid, {@value NA} if the cell was already shot,
     * {@value WATER} if the cell contained water or {@value SHIP} if the cell contained a ship.
     */
    protected int shootCell(Vector2i cell) {
        if (cell.x < 1 || cell.y < 1 || cell.x > gridSize || cell.y > gridSize) {
            return ERROR;
        }
        if (GameManager.getSettings().isOnline()) {
            return GameManager.shoot(team, cell) ? WATER : NA;
        }
        if (opponentGrid.getCell(cell.x, cell.y).state == Grid.SHIP) {
            return GameManager.shoot(team, cell) ? SHIP : NA;
        } else
            return GameManager.shoot(team, cell) ? WATER : NA;
    }

    /**
     * AI tries to find out which direction the ship is facing by shooting around the last hit on the ship.
     * If a direction was found the direction attribute gets set to that direction.
     *
     * @return true if the direction was found, false else
     */
    protected boolean findFoundShipDir() {
        Vector2i toShoot = new Vector2i(hitCells.get(0));
        toShoot.x -= 1;
        switch (shootCell(toShoot)) {
            case SHIP:
                foundShipDir = HORIZONTAL;
                hitCells.add(new Vector2i(toShoot));
                lastShot = toShoot;
                return true;
            case WATER:
                if (GameManager.getSettings().isOnline())
                    lastTried = HORIZONTAL;
                return false;
            case NA:
                break;
        }
        toShoot.x += 2;
        switch (shootCell(toShoot)) {
            case SHIP:
                foundShipDir = HORIZONTAL;
                hitCells.add(new Vector2i(toShoot));
                lastShot = toShoot;
                return true;
            case WATER:
                if (GameManager.getSettings().isOnline())
                    lastTried = HORIZONTAL;
                return false;
            case NA:
                break;
        }
        toShoot.x -= 1;
        toShoot.y += 1;
        switch (shootCell(toShoot)) {
            case SHIP:
                foundShipDir = VERTICAL;
                hitCells.add(new Vector2i(toShoot));
                lastShot = toShoot;
                return true;
            case WATER:
                if (GameManager.getSettings().isOnline())
                    lastTried = VERTICAL;
                return false;
            case NA:
                break;
        }
        toShoot.y -= 2;
        switch (shootCell(toShoot)) {
            case SHIP:
                foundShipDir = VERTICAL;
                hitCells.add(new Vector2i(toShoot));
                lastShot = toShoot;
                return true;
            case WATER:
                if (GameManager.getSettings().isOnline())
                    lastTried = VERTICAL;
                return false;
            case NA:
                break;
        }
        return false;
    }

    /**
     * Updates the pattern this AI uses after the first one is finished.
     */
    protected void updatePattern() {
        pattern = new PatternRandom(gridSize, team);
    }

    /**
     * Processes the answer from the network after the AI made a shot.
     * Only needs to be called for answer = 1 or = 2.
     * Only used if game is online.
     *
     * @param shot Index of the shot the answer is for.
     */
    public void processAnswer(Vector2i shot) {
        System.out.println("Added by AI");
        hitCells.add(shot);
        if (lastTried != UNKNOWN) {
            foundShipDir = lastTried;
            System.out.println("AI found ship dir " + foundShipDir + " Hit cells " + hitCells.size());
        }
        lastShot = shot;
    }
}
