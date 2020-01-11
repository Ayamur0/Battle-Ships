package com.battleships.logic;

import java.util.List;

public class Cell {

    public int x,y;
    public int state;
    public Ship ship;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        state = Grid.WATER;
    }
}
