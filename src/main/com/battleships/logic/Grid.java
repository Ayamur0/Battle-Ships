package com.battleships.logic;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.ShipManager;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

/**
 * Grids the game is played on.
 * Contain all necessary logic operations needed for the game.
 *
 * @author Tim Staudenmaier
 */
public class Grid {

    /**
     * Constants for states a cell on this grid can be in.
     */
    public static final int WATER = 0;
    /**
     * Constants for states a cell on this grid can be in.
     */
    public static final int SHIP = 1;
    /**
     * Constants for states a cell on this grid can be in.
     */
    public static final int BLOCKED = 2;
    /**
     * Constants for states a cell on this grid can be in.
     */
    public static final int SHOT = 3;

    /**
     * Array containing all cells on this grid.
     */
    private Cell[][] grid;
    /**
     * ID of the owner of this grid (contsants in {@link com.battleships.gui.gameAssets.grids.GridManager})
     */
    private int owner;
    /**
     * Array containing amount of ships still alive on this grid, ordered by size from small to large.
     */
    private int[] shipsAlive;

    /**
     * Creates a new grid.
     * @param size Size this grid should have.
     * @param owner ID of the owner this grid is created for.
     */
    public Grid (int size, int owner){
        this.owner = owner;
        grid = new Cell[size][size];
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Cell(i,j);
            }
        }
        shipsAlive = ShipAmountLoader.getShipAmounts(size);
    }

    /**
     * Can be used to test if a ship can be placed in it's current spot
     * without altering the grid.
     * @param x x index of the stern of the ship (1-size)
     * @param y y index of the stern of the ship (1-size)
     * @param size size of the ship (2-5)
     * @param direction direction the ship is facing (constants in {@link ShipManager}.
     * @return {@code true} if the ship can be placed at it's current spot, {@code false} else.
     */
    public boolean canShipBePlaced(int x, int y, int size, int direction){
        int directionFactor = getDirectionFactor(direction);
        try {
            if (direction == ShipManager.NORTH || direction == ShipManager.SOUTH) {
                for (int i = 0; i < size; i++) {
                    if (getCell(x, y + directionFactor * i).state != WATER) {
                        return false;
                    }
                }
            }
            if (direction == ShipManager.WEST || direction == ShipManager.EAST) {
                for (int i = 0; i < size; i++) {
                    if (getCell(x + directionFactor * i, y).state != WATER) {
                        return false;
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    /**
     * Places a ship at a specified spot.
     * @param x x index of the stern of the ship (1-size)
     * @param y y index of the stern of the ship (1-size)
     * @param size size of the ship (2-5)
     * @param direction direction the ship is facing (constants in {@link ShipManager}.
     * @param entity Entity of this ship in the GUI, if ship is on enemy grid and is not represented
     *               visually this value should be {@code null}
     * @return {@code true} if the ship was placed at it's current spot, {@code false} if it couldn't be placed.
     */
    public boolean placeShip(int x, int y, int size, int direction, Entity entity){
        if(!canShipBePlaced(x, y, size, direction))
            return false;
        List<Cell> shipParts = new ArrayList<>();
        int directionFactor = getDirectionFactor(direction);
        if (direction == ShipManager.NORTH || direction == ShipManager.SOUTH) {
            for (int i = 0; i < size; i++) {
                shipParts.add(getCell(x, y + directionFactor * i));
                getCell(x, y + directionFactor * i).state = SHIP;
                blockFieldsAroundIndex(x, y + directionFactor * i, BLOCKED, false);
            }
        }
        if (direction == ShipManager.WEST || direction == ShipManager.EAST) {
            for (int i = 0; i < size; i++) {
                shipParts.add(getCell(x + directionFactor * i, y));
                getCell(x + directionFactor * i, y).state = SHIP;
                blockFieldsAroundIndex(x + directionFactor * i, y, BLOCKED, false);
            }
        }
        Ship ship = new Ship(size, direction, shipParts, entity);
        for(Cell c : shipParts){
            c.ship = ship;
        }
        return true;
    }

    /**
     * Removes a ship form the grid.
     * @param ship Ship to remove from the grid.
     */
    public void removeShip(Ship ship){
        for(Cell c : ship.getOccupiedCells()){
            c.state = WATER;
            blockFieldsAroundIndex(c.y +1, c.x+1, WATER, false);
        }
    }

    /**
     * Determines whether a specific cell on the grid can be shot.
     * @param x x index of cell that should be tested (1-size)
     * @param y y index of cell that should be tested (1-size)
     * @return {@code true} if the cell can still be shot, {@code false} if the cell was already shot or marked with water.
     */
    public boolean canBeShot(int x, int y){
        return getCell(x,y).state != SHOT;
    }

    /**
     * Shoot a specific cell and place markers depending on what was hit.
     * @param x x index of cell that should be shot (1-size)
     * @param y y index of cell that should be shot (1-size)
     * @return {@code true} if a ship was hit, {@code false} if the cell didn't contain a ship or was already shot or marked with water and thus couldn't be shot.
     */
    public boolean shoot(int x, int y){
        if(!canBeShot(x, y))
            return false;
        boolean shipHit = shipHit(x, y);
        getCell(x,y).state = SHOT;
        if(shipHit)
            getCell(x,y).ship.damage();
        if(shipHit && isShipSunk(x, y))
            sinkShip(x, y);
        return shipHit;
    }

    /**
     * Tests if there is a ship in a specific cell that would be hit by a shot.
     * @param x x index of cell that should be tested (1-size)
     * @param y y index of cell that should be tested (1-size)
     * @return {@code true} if there is a ship on that cell, {@code false} else.
     */
    private boolean shipHit(int x, int y){
        return getCell(x,y).state == SHIP;
    }

    /**
     * Blocks all empty cell around the specified cell. Can be used to mark cell around sunk ship with water
     * or to block cell around a placed ship, so no other ship can be placed there.
     * @param x x index of cell around which all cells should be blocked (1-size)
     * @param y y index of cell around which all cells should be blocked (1-size)
     * @param blockType State to which the blocked cells should be set.
     * @param visible {@code true} if the markers should be visible on the gui (will be water markers), {@code false} if they should
     *                            be invisible on the gui.
     */
    private void blockFieldsAroundIndex(int x, int y, int blockType, boolean visible){
        x-=1;
        y-=1;
        int[] toBlock = {x-1, y-1, x, y-1, x+1, y-1, x-1, y, x+1, y, x-1, y+1, x, y+1, x+1, y+1};
        for(int i = 0; i < toBlock.length; i += 2){
            if(toBlock[i] >= 0 && toBlock[i] < grid.length && toBlock[i+1] >= 0 && toBlock[i+1] < grid.length && grid[toBlock[i+1]][toBlock[i]].state != SHIP) {
                grid[toBlock[i + 1]][toBlock[i]].state = blockType;
                if(visible && grid[toBlock[i+1]][toBlock[i]].state != SHOT)
                    GameManager.placeMarker(false, new Vector2i(toBlock[i]+1, toBlock[i+1]+1), GameManager.getLogic().getGridID(this));
            }
        }
    }

    /**
     * Sinks the ship that is on the specified cell.
     * Places water markers around the while ship.
     * @param x x index of one of the cell the ship is on (1-size)
     * @param y y index of one of the cell the ship is on (1-size)
     */
    private void sinkShip(int x, int y){
        shipsAlive[getCell(x,y).ship.getSize() - 2]--;
        for(Cell c : getCell(x,y).ship.getOccupiedCells()){
            blockFieldsAroundIndex(c.y+1, c.x+1, WATER, true);
            GameManager.placeMarker(false, new Vector2i(x,y), owner);
        }
    }

    /**
     * Converts a direction constans (from {@link ShipManager}) to a direction factor, to calculate the
     * sign the index on the grid array needs to be altered with.
     * @param direction Direction the ship is facing (from {@link ShipManager})
     * @return -1 if the rest of the ship is to the left or above this part in the grid array, 1 for right or below.
     */
    private int getDirectionFactor(int direction){
        switch (direction){
            case ShipManager.NORTH: return -1;
            case ShipManager.EAST: return 1;
            case ShipManager.SOUTH: return 1;
            case ShipManager.WEST: return -1;
            default: return  0;
        }
    }

    /**
     * Tests if the ship at the specified cell has been sunk.
     * @param x x index of one of the cell the ship is on (1-size)
     * @param y y index of one of the cell the ship is on (1-size)
     * @return {@code true} if this ship has been sunk, {@code false} else.
     */
    private boolean isShipSunk(int x, int y){
        return getCell(x,y).ship.isSunk();
    }


    @Override
    public String toString() {
        String[] rows = new String[grid.length];
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid.length; j++){
                builder.append(grid[i][j].state);
                builder.append(" | ");
            }
            rows[i] = builder.toString();
            builder = new StringBuilder();
        }
        for(int i = 0; i < rows.length; i++) {
            builder.append(rows[i]);
            builder.append("\n");
            builder.append("____________________________________________________________________________\n");
        }
        builder.append("\n\n\n");
        return builder.toString();
    }

    /**
     * Converts the standard x and y index into the y-1,x-1 index in the 2D-Array and returns the corresponding cell.
     * @param x x index of the cell that is needed.
     * @param y y index of the cell that is needed.
     * @return The cell at that index.
     */
    public Cell getCell(int x, int y){
        return grid[y-1][x-1];
    }

    /**
     * @return Array containing amount of ships still alive on this grid, ordered by size from small to large.
     */
    public int[] getShipsAlive() {
        return shipsAlive;
    }

    /**
     * @return Size of this grid.
     */
    public int getSize(){
        return grid.length;
    }
}
