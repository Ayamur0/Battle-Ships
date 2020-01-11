package com.battleships.logic.AI;

import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.logic.LogicManager;

import java.util.Random;

/**
 * Interface for different AI's.
 *
 * @author Tim Staudenmaier
 */
public abstract class AI {

    /**
     * Random number generator for determining cell that gets shot next.
     */
    Random random = new Random();
    /**
     * ID of the team this AI plays for.
     * One of the constants in hte {@link GridManager} (0 or 1).
     */
    protected int team;
    /**
     * Size of the grid this ai plays on.
     */
    protected int gridSize;
    /**
     * LogicManager this ai uses to shoot and place ships.
     */
    protected LogicManager manager;

    /**
     * Creates a new AI.
     * @param team Team this ai should play for (0 or 1 as in {@link GridManager})
     * @param gridSize Size of the grid this ai should play on.
     * @param manager LogicManager this ai should use to shoot and place ships.
     */
    public AI(int team, int gridSize, LogicManager manager) {
        this.team = team;
        this.gridSize = gridSize;
        this.manager = manager;
    }

    /**
     * AI makes their next turn.
     */
    abstract void makeTurn();

    /**
     * This AI places it's ships.
     */
    public void placeShips(){
        manager.placeRandomShips(team);
    }

    /**
     * Chooses a random pattern for an AI to use.
     * @param size Size of the grid the pattern is used for.
     * @return A pattern the AI can use.
     */
    static Pattern choosePattern(int size){
        Random r = new Random();
        switch (r.nextInt(3)){
            case 0: return new PatternX(size);
            case 1: return new PatternLines(size);
            case 2: return new PatternChess(size);
            default: return new PatternChess(size);
        }
    }
}
