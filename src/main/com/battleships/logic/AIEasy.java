package com.battleships.logic;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import org.joml.Vector2i;

import java.util.Random;

public class AIEasy implements AI{

    Random random = new Random();
    private int team;
    private int opponent;
    private int gridSize;
    private LogicManager manager;

    public AIEasy(int team, int gridSize, LogicManager manager) {
        this.team = team;
        this.opponent = team == GridManager.OWNFIELD ? GridManager.OPPONENTFIELD : GridManager.OWNFIELD;
        this.gridSize = gridSize;
        this.manager = manager;
    }

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

    public void placeShips(){
        manager.placeRandomShips(team);
    }

}
