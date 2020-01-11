package com.battleships.logic;

import com.battleships.gui.entities.Entity;

import java.util.List;

public class Ship {

    private int size;
    private int hitsTaken;
    private int direction;
    private Entity guiShip;
    private List<Cell> occupiedCells;

    public Ship(int size, int direction, List<Cell> occupiedCells, Entity guiShip) {
        this.size = size;
        this.direction = direction;
        this.guiShip = guiShip;
        this.occupiedCells = occupiedCells;
    }

    public void damage(){
        hitsTaken++;
    }

    public boolean isSunk(){
        return hitsTaken == size;
    }

    public List<Cell> getOccupiedCells() {
        return occupiedCells;
    }

    public Entity getGuiShip() {
        return guiShip;
    }

    public int getSize() {
        return size;
    }

    public int getDirection() {
        return direction;
    }
}
