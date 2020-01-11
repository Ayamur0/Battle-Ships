package com.battleships.logic;

import java.util.List;

/**
 * One Cell on the Grids.
 * Contains information of what is in that cell.
 *
 * @author Tim Staudenmaier
 */
public class Cell {

    /**
     * Index of this cell on the grid (0-size)
     */
    public int x,y;
    /**
     * State of this cell (one of constants on {@link Grid}.
     */
    public int state;
    /**
     * Ship this cell contains.
     * If this cell doesn't contain a ship this value is {@code null}.
     */
    public Ship ship;

    /**
     * Create a new cell with an index.
     * @param x x value of the cell in the grid array.
     * @param y y value of the cell in the grid array.
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        state = Grid.WATER;
    }
}
