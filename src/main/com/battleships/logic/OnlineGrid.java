package com.battleships.logic;

import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class OnlineGrid extends Grid {

    private static final int SHIPPROCESSED = 5;

    public OnlineGrid(int size, int owner) {
        super(size, owner);
    }

    public void processHit(int x, int y){
        getCell(x,y).state = SHIP;
    }

    public void processHitSunk(int x, int y){
        List<Vector2i> shipParts = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();

        List<Vector2i> current = findShipParts(x,y);
        if(current.size() != 0){
            for(Vector2i v : current){
                shipParts.add(v);
                findShipParts(v.x,v.y);
            }
        }
        for(Vector2i v : shipParts){
            cells.add(new Cell(v.x-1,v.y-1));
        }
        getCell(x,y).ship = new Ship(shipParts.size(), 0, cells, null);
        sinkShip(x,y);
    }

    public List<Vector2i> findShipParts(int x, int y) {
        List<Vector2i> toReturn = new ArrayList<>();
        x -= 1;
        y -= 1;
        int[] cellsAroundShip = {x - 1, y - 1, x, y - 1, x + 1, y - 1, x - 1, y, x + 1, y, x - 1, y + 1, x, y + 1, x + 1, y + 1};
        for (int i = 0; i < cellsAroundShip.length; i += 2) {
            if (cellsAroundShip[i] >= 0 && cellsAroundShip[i] < grid.length &&
                    cellsAroundShip[i + 1] >= 0 && cellsAroundShip[i + 1] < grid.length
                    && grid[cellsAroundShip[i + 1]][cellsAroundShip[i]].state == SHIP) {
                grid[cellsAroundShip[i + 1]][cellsAroundShip[i]].state = SHIPPROCESSED;
                toReturn.add(new Vector2i(x + 1, y + 1));
            }
        }
        return toReturn;
    }
}
