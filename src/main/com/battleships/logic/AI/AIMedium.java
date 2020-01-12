package com.battleships.logic.AI;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.logic.Grid;
import com.battleships.logic.LogicManager;
import com.battleships.logic.Ship;
import org.joml.Vector2i;

public class AIMedium extends AI{

    /**
     * Constants for directions a hit ship can have.
     */
    private static final int UNKNOWN = -1, HORIZONTAL = 0, VERTICAL = 1;
    /**
     * Constants for what was hit during last shot.
     */
    private static final int ERROR = -1, NA = 0, SHIP = 1, WATER = 2;

    protected Pattern pattern;
    private boolean lastHit;
    private Vector2i lastShot;
    private Vector2i firstShipHit;
    private int foundShipDir = -1;
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
        pattern = new PatternChess(gridSize);
        opponentGrid = team == GridManager.OWNFIELD ? manager.getOpponentGrid() : manager.getPlayerGrid();
    }

    /**
     * AI makes their next turn.
     */
    @Override
    public void makeTurn() {
        opponentGrid = team == GridManager.OWNFIELD ? manager.getOpponentGrid() : manager.getPlayerGrid();
        if(lastHit && foundShipDir == UNKNOWN){
            findFoundShipDir();
            return;
        }
        if(lastHit && foundShipDir != UNKNOWN){
            while(!shootFoundShip());
            if(lastHit)
                return;
        }
        if(lastShot == null){
            lastShot = pattern.firstIndex();
            if(shootCell(lastShot) == SHIP) {
                lastHit = true;
                firstShipHit = new Vector2i(lastShot);
            }
            return;
        }
        lastShot = pattern.nextIndex();
        if(lastShot == null){
            updatePattern();
            lastShot = pattern.firstIndex();
        }
        int result;
        while((result = shootCell(lastShot)) == NA) {
            if(manager.hasBeenShot(lastShot.x, lastShot.y, team == GridManager.OWNFIELD ? GridManager.OPPONENTFIELD : GridManager.OWNFIELD))
                lastShot = pattern.nextIndex();
            if(lastShot == null){
                updatePattern();
                lastShot = pattern.firstIndex();
            }
        }
        if(result == SHIP) {
            lastHit = true;
            firstShipHit = new Vector2i(lastShot);
        }
    }

    protected boolean shootFoundShip(){
        if(foundShipDir == HORIZONTAL){
            if(!leftEnd){
                lastShot.x -= 1;
                switch (shootCell(lastShot)){
                    case SHIP: return true;
                    case WATER: lastShot = firstShipHit; leftEnd = true; return true;
                    case NA:  lastShot = firstShipHit; leftEnd = true; return false;
                }
            }
            if(!rightEnd){
                lastShot.x += 1;
                switch (shootCell(lastShot)){
                    case SHIP: return true;
                    case WATER: lastShot = firstShipHit; rightEnd = true; return true;
                    case NA:  lastShot = firstShipHit; rightEnd = true; return false;
                }
            }
        }

        if(foundShipDir == VERTICAL){
            if(!downEnd){
                lastShot.y += 1;
                switch (shootCell(lastShot)){
                    case SHIP: return true;
                    case WATER: lastShot = firstShipHit; downEnd = true; return true;
                    case NA:  lastShot = firstShipHit; downEnd = true; return false;
                }
            }
            if(!upEnd){
                lastShot.y -= 1;
                switch (shootCell(lastShot)){
                    case SHIP: return true;
                    case WATER: lastShot = firstShipHit; upEnd = true; return true;
                    case NA:  lastShot = firstShipHit; upEnd = true; return false;
                }
            }
        }
        foundShipDir = UNKNOWN;
        lastHit = false;
        leftEnd = false;
        rightEnd = false;
        upEnd = false;
        downEnd = false;
        return true;
    }

    protected int shootCell(Vector2i cell){
        if(cell.x < 1 || cell.y < 1 || cell.x > gridSize || cell.y > gridSize) {
            return ERROR;
        }
        if(opponentGrid.getCell(cell.x, cell.y).state == Grid.SHIP){
            return GameManager.shoot(team, cell) ? SHIP : NA;
        }
        else
            return GameManager.shoot(team, cell) ? WATER : NA;
    }

    @SuppressWarnings("Duplicates")
    protected boolean findFoundShipDir(){
        Vector2i toShoot = new Vector2i(firstShipHit);
        toShoot.x -= 1;
        switch (shootCell(toShoot)){
            case SHIP: foundShipDir = HORIZONTAL; lastShot = toShoot; return true;
            case WATER: leftEnd = true; return false;
            case NA: leftEnd = true; break;
        }
        toShoot.x += 2;
        switch (shootCell(toShoot)){
            case SHIP: foundShipDir = HORIZONTAL; lastShot = toShoot; return true;
            case WATER: rightEnd = true; return false;
            case NA: rightEnd = true; break;
        }
        toShoot.x -= 1;
        toShoot.y += 1;
        switch (shootCell(toShoot)){
            case SHIP: foundShipDir = VERTICAL; lastShot = toShoot; return true;
            case WATER: downEnd = true; return false;
            case NA: downEnd = true; break;
        }
        toShoot.y -= 2;
        switch (shootCell(toShoot)){
            case SHIP: foundShipDir = VERTICAL; lastShot = toShoot; return true;
            case WATER: upEnd = true; return false;
            case NA: upEnd = true; break;
        }
        return false;
    }

    protected void updatePattern(){
        pattern = new PatternRandom(gridSize, team);
    }
}
