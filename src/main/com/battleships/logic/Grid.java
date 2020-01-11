package com.battleships.logic;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.ShipManager;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    public static final int WATER = 0;
    public static final int SHIP = 1;
    public static final int BLOCKED = 2;
    public static final int SHOT = 3;

    private Cell[][] grid;
    private int owner;
    private int[] shipsAlive;

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

    public void removeShip(Ship ship){
        for(Cell c : ship.getOccupiedCells()){
            c.state = WATER;
            blockFieldsAroundIndex(c.y +1, c.x+1, WATER, false);
        }
    }

    public boolean canBeShot(int x, int y){
        return getCell(x,y).state != SHOT;
    }

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

    public boolean shipHit(int x, int y){
        return getCell(x,y).state == SHIP;
    }

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

    private void sinkShip(int x, int y){
        shipsAlive[getCell(x,y).ship.getSize() - 2]--;
        for(Cell c : getCell(x,y).ship.getOccupiedCells()){
            blockFieldsAroundIndex(c.y+1, c.x+1, WATER, true);
            GameManager.placeMarker(false, new Vector2i(x,y), owner);
        }
    }

    private int getDirectionFactor(int direction){
        switch (direction){
            case ShipManager.NORTH: return -1;
            case ShipManager.EAST: return 1;
            case ShipManager.SOUTH: return 1;
            case ShipManager.WEST: return -1;
            default: return  0;
        }
    }

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

    public Cell getCell(int x, int y){
        return grid[y-1][x-1];
    }

    public int[] getShipsAlive() {
        return shipsAlive;
    }

    public int getSize(){
        return grid.length;
    }
}
