package com.battleships.logic.AI;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import org.joml.Vector2i;

import java.util.Random;

public class PatternRandom implements Pattern{

    private int size;
    private int team;
    private Random random = new Random();

    public PatternRandom(int size, int team) {
        this.size = size;
        this.team = team;
    }

    @Override
    public Vector2i nextIndex() {
        int x = random.nextInt(size) + 1;
        int y = random.nextInt(size) + 1;
        while(GameManager.getLogic().hasBeenShot(x,y,team == GridManager.OWNFIELD ? GridManager.OPPONENTFIELD : GridManager.OWNFIELD)){
            y += x / size;
            y %= size+1;
            if(y==0)
                y+=1;
            x += 1;
            x %= size+1;
            if(x==0)
                x+=1;
        }
        return new Vector2i(x,y);
    }

    @Override
    public Vector2i firstIndex() {
        return nextIndex();
    }
}
