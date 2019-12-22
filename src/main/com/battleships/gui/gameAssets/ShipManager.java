package com.battleships.gui.gameAssets;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.gameAssets.ingameGui.ShipCounter;
import com.battleships.gui.gameAssets.ingameGui.ShipSelector;
import com.battleships.gui.gameAssets.testLogic.TestLogic;
import com.battleships.gui.models.TexturedModel;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.logic.Ship;
import org.joml.*;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class ShipManager {

    private static final Vector3f GREEN = new Vector3f(0,1,0);
    private static final Vector3f RED = new Vector3f(1,0,0);
    private static final float MIXPERCENTAGE = 0.5f;

    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;

    private TexturedModel[] ships;
    private PlayingField ownPlayingField;
    private ShipSelector shipSelector;
    private int gridSize;

    private Entity cursorShip;
    private int cursorShipSize;
    private int cursorShipDirection;
    private boolean cursorShipAttached;
    private boolean cursorShipOnGrid;

    public ShipManager(Loader loader, PlayingField ownPlayingField){
        cursorShipAttached = false;
        this.ownPlayingField = ownPlayingField;
        gridSize = ownPlayingField.getSize();
        ships = new TexturedModel[4];
        ships[0] = (loader.loadModelFromOBJ("ship2", "ship2.tga", 10, 1));
        ships[1] = (loader.loadModelFromOBJ("ship3", "ship3.jpg", 10, 1));
        ships[2] = (loader.loadModelFromOBJ("ship4", "ship4.tga", 10, 1));
        ships[3] = (loader.loadModelFromOBJ("ship5new", "ship5.jpg", 10, 1));
    }

    public void placeShip(List<Entity> entities, int size, Vector3f position, Vector3f rotation, float scale){
        entities.add(new Entity(ships[size - 2], position, rotation, scale));
    }

    public void stickShipToCursor(int shipSize) {
        cursorShipSize = shipSize;
        cursorShipDirection = NORTH;
        cursorShip = new Entity(ships[shipSize - 2], new Vector3f(), new Vector3f(), 1f);
        cursorShipAttached = true;
    }

    public void stickShipToCursor(Ship ship){
        //TODO get shipsize, entity and direction from logic
    }

    public void renderCursorShip(MasterRenderer renderer){
        if(cursorShipAttached && cursorShipOnGrid)
            renderer.processEntity(cursorShip);
    }

    public void placeCursorShip(){
        if(!cursorShipAttached)
            return;
        cursorShip.setAdditionalColorPercentage(0);
        shipSelector.decrementCount(cursorShipSize);
        GameManager.placeShip(new Vector2i(ownPlayingField.getCurrentPointedCell().x, ownPlayingField.getCurrentPointedCell().y), cursorShipSize, cursorShipDirection, PlayingField.OWNFIELD);
        //ownPlayingField.placeShip(new Vector2i(ownPlayingField.getCurrentPointedCell().x, ownPlayingField.getCurrentPointedCell().y), cursorShipSize, cursorShipDirection);
        removeCursorShip();
    }

    public void removeCursorShip(){
        cursorShipAttached = false;
        cursorShip = null;
    }

    public void moveCursorShip(){
        if(!cursorShipAttached)
            return;
        Vector3i currentCell = ownPlayingField.getCurrentPointedCell();
        if(currentCell == null || currentCell.z == PlayingField.OPPONENTFIELD) {
            cursorShipOnGrid = false;
            return;
        }
        //if(TestLogic.own.canShipBePlaced(currentCell.x, currentCell.y, cursorShipSize, cursorShipDirection)) { //TODO get if ship is allowed to be placed at currentCell)
        if(true){
            cursorShip.setAdditionalColor(GREEN);
            cursorShip.setAdditionalColorPercentage(MIXPERCENTAGE);
        }
        else{
            cursorShip.setAdditionalColor(RED);
            cursorShip.setAdditionalColorPercentage(MIXPERCENTAGE);
        }
        cursorShipOnGrid = true;
        cursorShip.setPosition(ownPlayingField.calculateShipPosition(PlayingField.OWNFIELD, new Vector2i(currentCell.x, currentCell.y), cursorShipSize, cursorShipDirection));
    }

    public void rotateShip(){
        if(!cursorShipAttached)
            return;
        cursorShipDirection++;
        cursorShipDirection %= 4;
        cursorShip.getRotation().y = ownPlayingField.calculateShipRotation(cursorShipDirection);
    }

    public void setShipSelector(ShipSelector shipSelector) {
        this.shipSelector = shipSelector;
    }

    /**
     *
     * @return - size of the playingField this ShipManager belongs to.
     */
    public int getGridSize() {
        return gridSize;
    }
}
