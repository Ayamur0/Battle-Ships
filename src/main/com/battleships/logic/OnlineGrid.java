package com.battleships.logic;

import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class OnlineGrid extends Grid {

    public static final int SHIPPROCESSED = 5, ONLINESHIP = 6;

    public OnlineGrid(int size, int owner) {
        super(size, owner);
    }

    public void processShot(int x, int y, int answer) {
        getCell(x, y).state = SHOT;
        switch (answer) {
            case 0:
                break;
            case 1:
                processHit(x, y);
                break;
            case 2:
                processHit(x, y);
                processHitSunk(x, y);
        }
    }

    public void processHit(int x, int y) {
        getCell(x, y).state = ONLINESHIP;
    }

    public void processHitSunk(int x, int y) {
        List<Vector2i> shipParts = new ArrayList<>();
        List<Vector2i> needsSearching = new ArrayList<>();
        List<Cell> cells = new ArrayList<>();

        List<Vector2i> current = findShipParts(new Vector2i(x, y));
        needsSearching.addAll(current);
        while (needsSearching.size() != 0) {
            shipParts.addAll(current);
            current = findShipParts(needsSearching.get(0));
            needsSearching.remove(0);
            needsSearching.addAll(current);
        }
        for (Vector2i v : shipParts) {
            cells.add(new Cell(v.y - 1, v.x - 1));
        }
        getCell(x, y).ship = new Ship(shipParts.size(), 0, cells, null);
        sinkShip(x, y);
    }

    public List<Vector2i> findShipParts(Vector2i index) {
        List<Vector2i> toReturn = new ArrayList<>();
        int x = index.x - 1;
        int y = index.y - 1;
        int[] cellsAroundShip = {x - 1, y - 1, x, y - 1, x + 1, y - 1, x - 1, y, x + 1, y, x - 1, y + 1, x, y + 1, x + 1, y + 1};
        for (int i = 0; i < cellsAroundShip.length; i += 2) {
            if (cellsAroundShip[i] >= 0 && cellsAroundShip[i] < grid.length &&
                    cellsAroundShip[i + 1] >= 0 && cellsAroundShip[i + 1] < grid.length
                    && grid[cellsAroundShip[i + 1]][cellsAroundShip[i]].state == ONLINESHIP) {
                grid[cellsAroundShip[i + 1]][cellsAroundShip[i]].state = SHIPPROCESSED;
                toReturn.add(new Vector2i(cellsAroundShip[i] + 1, cellsAroundShip[i + 1] + 1));
            }
        }
        return toReturn;
    }

    @Override
    public boolean canBeShot(int x, int y) {
        return getCell(x, y).state != SHOT && getCell(x, y).state != ONLINESHIP && getCell(x, y).state != SHIPPROCESSED;
    }
}
