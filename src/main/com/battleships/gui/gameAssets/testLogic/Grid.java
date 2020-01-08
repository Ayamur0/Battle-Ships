package com.battleships.gui.gameAssets.testLogic;

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

    public Grid (int size, int owner){
        this.owner = owner;
        grid = new Cell[size][size];
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Cell(i,j);
            }
        }
    }

    public boolean canShipBePlaced(int x, int y, int size, int direction){
        x -= 1;
        y -= 1;
        int directionFactor = getDirectionFactor(direction);
        try {
            if (direction == ShipManager.NORTH || direction == ShipManager.SOUTH) {
                for (int i = 0; i < size; i++) {
                    if (grid[y + directionFactor * i][x].state != WATER) {
                        return false;
                    }
                }
            }
            if (direction == ShipManager.WEST || direction == ShipManager.EAST) {
                for (int i = 0; i < size; i++) {
                    if (grid[y][x + directionFactor * i].state != WATER) {
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

    public void placeShip(int x, int y, int size, int direction){
        if(!canShipBePlaced(x, y, size, direction))
            return;
        x -= 1;
        y -= 1;
        List<Cell> shipParts = new ArrayList<>();
        int directionFactor = getDirectionFactor(direction);
        if (direction == ShipManager.NORTH || direction == ShipManager.SOUTH) {
            for (int i = 0; i < size; i++) {
                shipParts.add(grid[y + directionFactor * i][x]);
                grid[y + directionFactor * i][x].state = SHIP;
                grid[y + directionFactor * i][x].lifes = size;
                blockFieldsAroundIndex(x + 1, y + 1 + directionFactor * i, BLOCKED);
            }
        }
        if (direction == ShipManager.WEST || direction == ShipManager.EAST) {
            for (int i = 0; i < size; i++) {
                shipParts.add(grid[y][x + directionFactor * i]);
                grid[y][x + directionFactor * i].state = SHIP;
                grid[y][x + directionFactor * i].lifes = size;
                blockFieldsAroundIndex(x + 1 + directionFactor * i, y + 1, BLOCKED);
            }
        }
        for(Cell c : shipParts){
            c.ShipParts = shipParts;
        }
    }

    public boolean canBeShot(int x, int y){
        return grid[x-1][y-1].state != SHOT;
    }

    public boolean shoot(int x, int y){
        if(!canBeShot(x, y))
            return false;
        boolean shipHit = shipHit(x, y);
        grid[x-1][y-1].state = SHOT;
        for(Cell c : grid[x-1][y-1].ShipParts)
            c.lifes -= 1;
        if(isShipSunk(x, y))
            sinkShip(x, y);
        return shipHit;
    }

    public boolean shipHit(int x, int y){
        return grid[x-1][y-1].state == SHIP;
    }

    private void blockFieldsAroundIndex(int x, int y, int blockType){
        x-=1;
        y-=1;
        int[] toBlock = {x-1, y-1, x, y-1, x+1, y-1, x-1, y, x+1, y, x-1, y+1, x, y+1, x+1, y+1};
        for(int i = 0; i < toBlock.length; i += 2){
            if(toBlock[i] >= 0 && toBlock[i] < grid.length && toBlock[i+1] >= 0 && toBlock[i+1] < grid.length && grid[toBlock[i+1]][toBlock[i]].state != SHIP)
                grid[toBlock[i+1]][toBlock[i]].state = blockType;
        }
    }

    private void sinkShip(int x, int y){
        for(Cell c : grid[x-1][y-1].ShipParts){
            blockFieldsAroundIndex(c.x+1, c.y+1, WATER);
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
        if(grid[x-1][y-1].lifes == 0)
            return true;
        return false;
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
}
