package com.battleships.gui.gameAssets.testLogic;

import java.util.List;

public class Cell {

    public int x,y;
    public int state;
    public int lifes;
    public List<Cell> ShipParts;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        state = Grid.WATER;
    }
}
