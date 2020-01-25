package com.battleships.logic;

import com.battleships.gui.entities.Entity;

import java.util.List;

/**
 * A ship that is located in the cells of the logic grid.
 *
 * @author Tim Staudemnaier
 */
public class Ship {

    /**
     * Size of this ship (2-5)
     */
    private int size;
    /**
     * Hits this ship has already taken.
     */
    private int hitsTaken;
    /**
     * Direction this ship is facing (see constants in {@link com.battleships.gui.gameAssets.grids.ShipManager}).
     */
    private int direction;
    /**
     * Entity of this ship in the gui.
     * Is {@code null} if this ship isn't represented in the gui.
     */
    private Entity guiShip;
    /**
     * List containing all cells this ships parts are on.
     */
    private List<Cell> occupiedCells;

    /**
     * Create a new logic ship.
     *
     * @param size          Size of the ship (2-5).
     * @param direction     Direction the ship is facing (see constants in {@link com.battleships.gui.gameAssets.grids.ShipManager})
     * @param occupiedCells List containing all cells this ships parts are on.
     * @param guiShip       Entity of this ship in the gui. Or {@code null} if this ship isn't represented in the gui.
     */
    public Ship(int size, int direction, List<Cell> occupiedCells, Entity guiShip) {
        this.size = size;
        this.direction = direction;
        this.guiShip = guiShip;
        this.occupiedCells = occupiedCells;
    }

    /**
     * Damage this ship by increasing its hitsTaken.
     */
    public void damage() {
        hitsTaken++;
    }

    /**
     * @return {@code true} if this ship has been sunk, {@code false} if this ship is still alive.
     */
    public boolean isSunk() {
        return hitsTaken == size;
    }

    /**
     * @return List containing all cells this ships parts are on.
     */
    public List<Cell> getOccupiedCells() {
        return occupiedCells;
    }

    /**
     * @return Entity of this ship in the gui. Or {@code null} if this ship isn't represented in the gui.
     */
    public Entity getGuiShip() {
        return guiShip;
    }

    /**
     * @return Size of this ship.
     */
    public int getSize() {
        return size;
    }

    /**
     * @return Direction this ship is facing (see constants in {@link com.battleships.gui.gameAssets.grids.ShipManager}).
     */
    public int getDirection() {
        return direction;
    }
}
