package com.battleships.logic;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.gui.gameAssets.grids.GridMaths;
import org.joml.Vector2i;

import java.util.Random;

public class LogicManager {

    private TurnHandler turnHandler = new TurnHandler();
    private Grid playerGrid;
    private Grid opponentGrid;
    private int gameState = GameManager.MENU;

    public void init(int size) {
        playerGrid = new Grid(size, GridManager.OWNFIELD);
        opponentGrid = new Grid(size, GridManager.OPPONENTFIELD);
        turnHandler.setOpponentAI(new AIEasy(1, size, this));
    }

    public boolean shoot(int x, int y, int grid){
        if(grid == GridManager.OWNFIELD)
            return playerGrid.shoot(x,y);
        if(grid == GridManager.OPPONENTFIELD)
            return opponentGrid.shoot(x,y);
        return false;
    }

    public void testEndOfGame(){
        int[] shipsAlive = playerGrid.getShipsAlive();
        if(shipsAlive[0]+shipsAlive[1]+shipsAlive[2]+shipsAlive[3]==0)
            GameManager.finishGame(false);
        shipsAlive = opponentGrid.getShipsAlive();
        if(shipsAlive[0]+shipsAlive[1]+shipsAlive[2]+shipsAlive[3]==0)
            GameManager.finishGame(true);
    }

    public boolean placeShip(int x, int y, int size, int direction, Entity entity, int grid){
        if(grid == GridManager.OWNFIELD)
            return playerGrid.placeShip(x,y,size,direction,entity);
        if(grid == GridManager.OPPONENTFIELD)
            return opponentGrid.placeShip(x,y,size,direction,entity);
        return false;
    }

    public void placeRandomShips(int gridNum){
        Random random = new Random();

        Grid grid = gridNum == GridManager.OWNFIELD ? playerGrid : opponentGrid;
        int[] shipsToPlace = grid.getShipsAlive();
        int size = grid.getSize();

        for(int i = 2; i <= 5; i++){
            for(int j = 0; j < shipsToPlace[i-2]; j++){
                int x = random.nextInt(size)+1;
                int y = random.nextInt(size)+1;
                int dir = random.nextInt(4);
                out: while(!grid.canShipBePlaced(x,y,i,dir)){
                        for(int k = 0; k < 4; k++){
                            dir %= 3;
                            dir += 1;
                            if(grid.canShipBePlaced(x,y,i,dir))
                                break out;
                    }
                        y += x / size;
                        y %= size+1;
                        if(y==0)
                            y+=1;
                        x += 1;
                        x %= size+1;
                        if(x==0)
                            x+=1;
                }
                if(gridNum == GridManager.OWNFIELD)
                    GameManager.placeShip(new Vector2i(x,y), i, dir, gridNum);
                else
                    placeShip(x,y,i,dir,null,gridNum);
            }
        }
    }

    public boolean canShipBePlaced(int x, int y, int size, int direction, int grid){
        if(grid == GridManager.OWNFIELD)
            return playerGrid.canShipBePlaced(x,y,size,direction);
        if(grid == GridManager.OPPONENTFIELD)
            return opponentGrid.canShipBePlaced(x,y,size,direction);
        return false;
    }

    public int[] getEnemyShipsLeft(){
        return opponentGrid.getShipsAlive();
    }

    public void advanceGamePhase(){
        switch (gameState){
            case GameManager.MENU: init(GameManager.getSettings().getSize()); GameManager.startShipPlacementPhase(); break;
            case GameManager.SHIPLACING: turnHandler.placeAiShips(); GameManager.startPlayPhase();  break;
            case GameManager.SHOOTING:
        }
        gameState++;
        gameState %= 3;
    }

    public void removeAllShips(){
        int size = playerGrid.getSize();
        playerGrid = new Grid(size, GridManager.OWNFIELD);
    }

    public void removeShip(Ship ship){
        playerGrid.removeShip(ship);
    }

    public boolean hasBeenShot(int x, int y, int grid){
        if(grid == GridManager.OWNFIELD)
            return playerGrid.getCell(x,y).state == Grid.SHOT;
        else
            return opponentGrid.getCell(x,y).state == Grid.SHOT;
    }

    public Ship getPlayerShipAtIndex(int x, int y){
        return playerGrid.getCell(x,y).ship;
    }

    public boolean isPlayerTurn(){
        return turnHandler.isPlayerTurn();
    }

    public void advanceTurn(){
        testEndOfGame();
        GameManager.updateAliveShip();
        turnHandler.advanceTurnOrder();
    }

    public int getGameState() {
        return gameState;
    }

    public int getGridID(Grid grid){
        return grid == playerGrid ? 0 : 1;
    }
}
