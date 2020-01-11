package com.battleships.logic.AI;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.logic.LogicManager;
import org.joml.Vector2i;

import java.util.Random;

/**
 * Implementation of an AI with easy difficulty.
 * This AI will always shoot randomly even if it has hit a ship in the previous round.
 *
 * @author Tim Staudenmaier
 */
public class AIEasy extends AI{
    /**
     * Creates a new AI with easy difficulty.
     * @param team Team this ai should play for (0 or 1 as in {@link GridManager})
     * @param gridSize Size of the grid this ai should play on.
     * @param manager LogicManager this ai should use to shoot and place ships.
     */
    public AIEasy(int team, int gridSize, LogicManager manager) {
        super(team, gridSize, manager);
    }

    /**
     * This AI makes it's next turn.
     * Chooses a random cell an tries to shoot it.
     * If that cell can't be shoot the cell to the right is tried next until one gets found that can be shot.
     */
    public void makeTurn(){
        int x = random.nextInt(gridSize) + 1;
        int y = random.nextInt(gridSize) + 1;
        while(!GameManager.shoot(team, new Vector2i(x,y))){
            y += x / gridSize;
            y %= gridSize+1;
            if(y==0)
                y+=1;
            x += 1;
            x %= gridSize+1;
            if(x==0)
                x+=1;
        }
    }
}
