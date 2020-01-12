package com.battleships.logic;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.gui.gameAssets.grids.ShipManager;
import com.battleships.logic.AI.AIEasy;
import com.battleships.logic.AI.AIHard;
import com.battleships.logic.AI.AIMedium;
import org.joml.Vector2i;

import java.util.Random;

/**
 * Main class for communication with the logic.
 * Contains all functions needed form the logic.
 *
 * @author Tim Staudenmaier
 */
public class LogicManager {

    /**
     * Handler that handles turn sequence and executes ai turns if necessary.
     */
    private TurnHandler turnHandler = new TurnHandler();
    /**
     * Grid of the player.
     */
    private Grid playerGrid;
    /**
     * Grid of the opponent.
     */
    private Grid opponentGrid;
    /**
     * State of the game (one of constants in {@link GameManager}.
     */
    private int gameState = GameManager.MENU;
    /**
     * Class containing stats for this logic/game.
     */
    private Stats stats;

    /**
     * Initialize this logic using the information from the given settings.
     */
    public void init(Settings settings) {
        playerGrid = new Grid(settings.getSize(), GridManager.OWNFIELD);
        opponentGrid = new Grid(settings.getSize(), GridManager.OPPONENTFIELD);
        turnHandler.setOpponentAI(new AIHard(1, settings.getSize(), this));
        stats = new Stats();
        stats.init();
    }

    /**
     * Shoots a specific cell on a grid.
     * (only in logic not in gui use method in {@link GameManager#shoot(int, Vector2i)}  for that}).
     * @param x X index of the cell that should be shot.
     * @param y Y index of the cell that should be shot.
     * @param grid ID of the grid the cell is on (one of constants in {@link GridManager}.
     * @return {@code true} if a ship was hit, {@code false} if no ship was hit or shot couldn't be made.
     */
    public boolean shoot(int x, int y, int grid){
        if(x < 1 || y < 1 || x > playerGrid.getSize() ||y > playerGrid.getSize())
            return false;
        if(GameManager.getSettings().isOnline() && grid == GridManager.OWNFIELD)
            return false;
        if(grid == GridManager.OWNFIELD)
            return playerGrid.shoot(x,y);
        if(grid == GridManager.OPPONENTFIELD)
            return opponentGrid.shoot(x,y);
        return false;
    }

    /**
     * Tests if the game is over.
     * If game is over executes finishGame method to end the game.
     * @return {@code true} if the game is finished {@code false} else.
     */
    public boolean testEndOfGame(){
        int[] shipsAlive = playerGrid.getShipsAlive();
        if(shipsAlive[0]+shipsAlive[1]+shipsAlive[2]+shipsAlive[3]==0) {
            GameManager.finishGame(false);
            return true;
        }
        shipsAlive = opponentGrid.getShipsAlive();
        if(shipsAlive[0]+shipsAlive[1]+shipsAlive[2]+shipsAlive[3]==0) {
            GameManager.finishGame(true);
            return true;
        }
        return false;
    }

    /**
     * Place a ship at a index on a grid.
     * (only in logic not in gui use method in {@link GameManager#placeShip(Vector2i, int, int, int)}  for that}).
     * @param x x index of the stern of the ship (1-size)
     * @param y y index of the stern of the ship (1-size)
     * @param size size of the ship (2-5)
     * @param direction direction the ship is facing (constants in {@link ShipManager}.
     * @param entity Entity of this ship in the GUI, if ship is on enemy grid and is not represented
     *               visually this value should be {@code null}.
     * @param grid ID of the grid the ship should be on (one of constants in {@link GridManager}.
     * @return
     */
    public boolean placeShip(int x, int y, int size, int direction, Entity entity, int grid){
        if(grid == GridManager.OWNFIELD)
            return playerGrid.placeShip(x,y,size,direction,entity);
        if(grid == GridManager.OPPONENTFIELD)
            return opponentGrid.placeShip(x,y,size,direction,entity);
        return false;
    }

    /**
     * Places ships at random spots on a grid.
     * Clears grid before placing ships.
     * Places ships also in gui if they are placed on players grid.
     * @param gridNum  ID of the grid the ships should be placed on (one of constants in {@link GridManager}.
     */
    public void placeRandomShips(int gridNum){
        Random random = new Random();

        Grid grid = gridNum == GridManager.OWNFIELD ? playerGrid : opponentGrid;
        int[] shipsToPlace = grid.getShipsAlive();
        int size = grid.getSize();

        for(int i = 5; i >= 2; i--){
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

    /**
     * Can be used to test if a ship can be placed in it's current spot on a specific grid
     * without altering the grid.
     * @param x x index of the stern of the ship (1-size)
     * @param y y index of the stern of the ship (1-size)
     * @param size size of the ship (2-5)
     * @param direction direction the ship is facing (constants in {@link ShipManager}.
     * @param grid ID of the grid the ships should be placed on (one of constants in {@link GridManager}.
     * @return {@code true} if the ship can be placed at it's current spot, {@code false} else.
     */
    public boolean canShipBePlaced(int x, int y, int size, int direction, int grid){
        if(grid == GridManager.OWNFIELD)
            return playerGrid.canShipBePlaced(x,y,size,direction);
        if(grid == GridManager.OPPONENTFIELD)
            return opponentGrid.canShipBePlaced(x,y,size,direction);
        return false;
    }

    /**
     * @return Array containing amount of ships still alive on enemy grid, ordered by size from small to large.
     */
    public int[] getEnemyShipsLeft(){
        return opponentGrid.getShipsAlive();
    }
    /**
     * @return Array containing amount of ships still alive on player grid, ordered by size from small to large.
     */
    public int[] getPlayerShipsLeft(){
        return playerGrid.getShipsAlive();
    }

    /**
     * Advances the game phase by one.
     * Menu -> Ship placing
     * Ship placing -> Shooting
     * Shooting -> Menu
     */
    public void advanceGamePhase(){
        switch (gameState){
            case GameManager.MENU: init(GameManager.getSettings()); GameManager.startShipPlacementPhase(); break;
            case GameManager.SHIPLACING: turnHandler.placeAiShips(); GameManager.startPlayPhase();  break;
            case GameManager.SHOOTING:
        }
        gameState++;
        gameState %= 3;
    }

    /**
     * Removes all ships from the grid of the player.
     * (only in logic not in gui use method in {@link GameManager#removeAllShips()}  for that}).
     */
    public void removeAllShips(){
        int size = playerGrid.getSize();
        playerGrid = new Grid(size, GridManager.OWNFIELD);
    }

    /**
     * Removes on ship from the players grid.
     * (only in logic not in gui).
     * @param ship Ship to remove
     */
    public void removeShip(Ship ship){
        playerGrid.removeShip(ship);
    }

    /**
     * Determines whether a specific cell on the grid can't be shot anymore.
     * @param x x index of cell that should be tested (1-size)
     * @param y y index of cell that should be tested (1-size)
     * @return {@code true} if the cell can't be shot, {@code false} if the cell can still be shot.
     */
    public boolean hasBeenShot(int x, int y, int grid){
        if(grid == GridManager.OWNFIELD)
            return !playerGrid.canBeShot(x,y);
        else
            return !opponentGrid.canBeShot(x,y);
    }

    /**
     * @param x X index of the cell the ship should be returned for (1-size).
     * @param y Y index of the cell the ship should be returned for (1-size).
     * @return The ship at that index or {@code null} if there is no ship at that index.
     */
    public Ship getPlayerShipAtIndex(int x, int y){
        return playerGrid.getCell(x,y).ship;
    }

    /**
     * @return {@code true} if it's currently the players turn, {@code false} if it's the opponents turn.
     */
    public boolean isPlayerTurn(){
        return turnHandler.isPlayerTurn();
    }

    /**
     * Advance turn order.
     * Adds rounds to stats, tests if game is over and updates ships left alive.
     * If next turn is turn of an AI the turn gets executed.
     */
    public void advanceTurn(){
        if(!isPlayerTurn())
            stats.addRound();
        GameManager.updateAliveShip();
        if(testEndOfGame())
            return;
        turnHandler.advanceTurnOrder();
    }

    /**
     * Same player is allowed to shoot again, call if player hit a ship.
     */
    public void repeatTurn(){
        GameManager.updateAliveShip();
        if(testEndOfGame())
            return;
        turnHandler.makeAiTurns();
    }

    /**
     * @return Current state of the game (one of constants in {@link GameManager}).
     */
    public int getGameState() {
        return gameState;
    }

    /**
     * @param grid Grid the ID is needed for.
     * @return The ID of the player the grid belongs to (one of constants in {@link GridManager}).
     */
    public int getGridID(Grid grid){
        return grid == playerGrid ? 0 : 1;
    }

    /**
     * @return Stats of this logic/game.
     */
    public Stats getStats() {
        return stats;
    }

    /**
     * @return TurnHandler for this logic.
     */
    public TurnHandler getTurnHandler() {
        return turnHandler;
    }

    /**
     * @return Grid of the player.
     */
    public Grid getPlayerGrid() {
        return playerGrid;
    }

    /**
     * @return Grid of the opponent.
     */
    public Grid getOpponentGrid() {
        return opponentGrid;
    }

    /**
     * Set the turnHandler to a new one (for loading games).
     * @param turnHandler New TurnHandler this logic should use.
     */
    public void setTurnHandler(TurnHandler turnHandler) {
        this.turnHandler = turnHandler;
    }

    /**
     * Set the players grid to a new one (for loading games).
     * @param playerGrid New grid this logic should use for the player.
     */
    public void setPlayerGrid(Grid playerGrid) {
        this.playerGrid = playerGrid;
    }

    /**
     * Set the opponents grid to a new one (for loading games).
     * @param opponentGrid New grid this logic should use for the opponent.
     */
    public void setOpponentGrid(Grid opponentGrid) {
        this.opponentGrid = opponentGrid;
    }

    /**
     * Sets gameState to a specific state and updates GUI correspondingly.
     * @param gameState State this game should be set to (constants on {@link GameManager}).
     */
    public void setGameState(int gameState) {
        this.gameState = gameState;
        switch (gameState){
            case GameManager.MENU: break;
            case GameManager.SHIPLACING: GameManager.startShipPlacementPhase(); break;
            case GameManager.SHOOTING: GameManager.startPlayPhase();
        }
    }

    /**
     * Set the stats to specific ones (for loading games).
     * @param stats New stats this logic should use.
     */
    public void setStats(Stats stats) {
        this.stats = stats;
    }
}
