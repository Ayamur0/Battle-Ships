package com.battleships.logic.AI;

import com.battleships.logic.LogicManager;

public class AIHard extends AIMedium {
    public AIHard(int team, int gridSize, LogicManager manager) {
        super(team, gridSize, manager);
        int r = random.nextInt(2);
        pattern = r == 1 ? new PatternLines(gridSize) : new PatternX(gridSize);
    }

    @Override
    protected void updatePattern() {
        if(!(pattern instanceof PatternChess))
            pattern = new PatternChess(gridSize);
        pattern = new PatternRandom(gridSize, team);
    }
}
