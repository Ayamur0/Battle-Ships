package com.battleships.logic.AI;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.logic.Grid;
import com.battleships.logic.LogicManager;
import com.battleships.logic.Ship;
import org.joml.Vector2i;

public class AIMedium extends AI{

    private static final int UNKNOWN = -1, HORIZONTAL = 0, VERTICAL = 1;
    private static final int NA = 0, SHIP = 1, WATER = 2;

    private Pattern pattern;
    private boolean lastHit;
    private Vector2i lastShot;
    private int foundShipDir;
    private boolean leftEnd, downEnd, upEnd, rightEnd;
    private Grid opponentGrid;

    /**
     * Creates a new AI with medium difficulty.
     * @param team Team this ai should play for (0 or 1 as in {@link GridManager})
     * @param gridSize Size of the grid this ai should play on.
     * @param manager LogicManager this ai should use to shoot and place ships.
     */
    public AIMedium(int team, int gridSize, LogicManager manager) {
        super(team, gridSize, manager);
        pattern = AI.choosePattern(gridSize);
        opponentGrid = team == GridManager.OWNFIELD ? manager.getOpponentGrid() : manager.getPlayerGrid();
    }

    /**
     * AI makes their next turn.
     */
    @Override
    public void makeTurn() {
        if(lastHit && foundShipDir == UNKNOWN){
            findFoundShipDir();
        }
    }

    private int shootCell(Vector2i cell){
        if(opponentGrid.getCell(cell.x, cell.y).state == Grid.SHIP){
            return GameManager.shoot(team, cell) ? SHIP : NA;
        }
        else
            return GameManager.shoot(team, cell) ? WATER : NA;
    }

    @SuppressWarnings("Duplicates")
    private boolean findFoundShipDir(){
        Vector2i toShoot = new Vector2i(lastShot);
        toShoot.x -= 1;
        switch (shootCell(toShoot)){
            case SHIP: foundShipDir = HORIZONTAL; return true;
            case WATER: return false;
            case NA: leftEnd = true; break;
        }
        toShoot.x += 2;
        switch (shootCell(toShoot)){
            case SHIP: foundShipDir = HORIZONTAL; return true;
            case WATER: return false;
            case NA: rightEnd = true; break;
        }
        toShoot.x -= 1;
        toShoot.y += 1;
        switch (shootCell(toShoot)){
            case SHIP: foundShipDir = VERTICAL; return true;
            case WATER: return false;
            case NA: upEnd = true; break;
        }
        toShoot.y -= 2;
        switch (shootCell(toShoot)){
            case SHIP: foundShipDir = VERTICAL; return true;
            case WATER: return false;
            case NA: downEnd = true; break;
        }
        return false;
    }



}
