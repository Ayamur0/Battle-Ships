package com.battleships.logic.AI;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.logic.Grid;
import com.battleships.logic.LogicManager;
import com.battleships.logic.Ship;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of an AI with medium difficulty.
 * This AI will first choose either a {@link PatternX}, {@link PatternChess} or {@link PatternLines} to shoot.
 * After that pattern is done it will shoot randomly.
 *
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

    protected Pattern pattern;
    private Vector2i lastShot;
    private int foundShipDir = -1;
    private Grid opponentGrid;
    private List<Vector2i> hitCells = new ArrayList<>();
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
            if(result == ERROR) {
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

    protected int shootCell(Vector2i cell) {
        if (cell.x < 1 || cell.y < 1 || cell.x > gridSize || cell.y > gridSize) {
            return ERROR;
        }
        if (GameManager.getSettings().isOnline()){
            return GameManager.shoot(team, cell) ? WATER : NA;
        }
        if (opponentGrid.getCell(cell.x, cell.y).state == Grid.SHIP) {
            return GameManager.shoot(team, cell) ? SHIP : NA;
        } else
            return GameManager.shoot(team, cell) ? WATER : NA;
    }

    @SuppressWarnings("Duplicates")
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
                if(GameManager.getSettings().isOnline())
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
                if(GameManager.getSettings().isOnline())
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
                if(GameManager.getSettings().isOnline())
                    lastTried = HORIZONTAL;
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
                if(GameManager.getSettings().isOnline())
                    lastTried = HORIZONTAL;
                return false;
            case NA:
                break;
        }
        return false;
    }

    protected void updatePattern() {
        pattern = new PatternRandom(gridSize, team);
    }

    public void processAnswer(Vector2i shot){
        hitCells.add(shot);
        if(lastTried != UNKNOWN)
            foundShipDir = lastTried;
        lastShot = shot;
    }
}
