package com.battleships.logic.AI;

import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.logic.LogicManager;

/**
 * Implementation of an AI with hard difficulty.
 * This AI will first choose either a {@link PatternX} or {@link PatternLines} to shoot.
 * After that pattern is done it will shoot the remaining cells using a {@link PatternChess} to find
 * all ships.
 *
 * If it hits a ship it will try to sink that ship before moving on.
 *
 * @author Tim Staudenmaier
 */
public class AIHard extends AIMedium {
    /**
     * Creates a new hard AI.
     * @param team Team this ai should play for (0 or 1 as in {@link GridManager})
     * @param gridSize Size of the grid this ai should play on.
     * @param manager LogicManager this ai should use to shoot and place ships.
     */
    public AIHard(int team, int gridSize, LogicManager manager) {
        super(team, gridSize, manager);
        int r = random.nextInt(2);
        pattern = r == 1 ? new PatternLines(gridSize) : new PatternX(gridSize);
    }

    /**
     * Updates the pattern this AI uses after the first one is finished.
     */
    @Override
    protected void updatePattern() {
        if(!(pattern instanceof PatternChess))
            pattern = new PatternChess(gridSize);
        else
            pattern = new PatternRandom(gridSize, team);
    }
}
