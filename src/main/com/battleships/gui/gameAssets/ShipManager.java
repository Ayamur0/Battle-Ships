package com.battleships.gui.gameAssets;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.gameAssets.ingameGui.ShipSelector;
import com.battleships.gui.models.TexturedModel;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.logic.Ship;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class ShipManager {

    private static final Vector3f GREEN = new Vector3f(0,1,0);
    private static final Vector3f RED = new Vector3f(1,0,0);
    private static final float MIXPERCENTAGE = 0.5f;

    protected static final int NORTH = 0;
    protected static final int EAST = 1;
    protected static final int SOUTH = 2;
    protected static final int WEST = 3;

    private TexturedModel[] ships;
    private PlayingField ownPlayingField;
    private ShipSelector shipSelector;

    private Entity cursorShip;
    private int cursorShipSize;
    private int cursorShipDirection;
    private boolean cursorShipAttached;
    private boolean cursorShipOnGrid;

    public ShipManager(Loader loader, PlayingField ownPlayingField){
        cursorShipAttached = false;
        this.ownPlayingField = ownPlayingField;
        ships = new TexturedModel[4];
        ships[0] = (loader.loadModelFromOBJ("ship2", "ship2.tga", 10, 1));
        ships[1] = (loader.loadModelFromOBJ("ship3", "ship3.jpg", 10, 1));
        ships[2] = (loader.loadModelFromOBJ("ship4", "ship4.tga", 10, 1));
        ships[3] = (loader.loadModelFromOBJ("ship5new", "ship5.jpg", 10, 1));
    }

    public int placeShip(List<Entity> entities, int size, Vector3f position, Vector3f rotation, float scale){
        Entity toAdd = new Entity(ships[size - 2], position, rotation, scale);
        entities.add(toAdd);
        return entities.size() - 1;
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

    public void placeCursorShip(List<Entity> entities){
        if(!cursorShipAttached)
            return;
        cursorShip.setAdditionalColorPercentage(0);
        shipSelector.decrementCount(cursorShipSize);
        entities.add(new Entity(cursorShip.getModel(), new Vector3f(cursorShip.getPosition()), new Vector3f(cursorShip.getRotation()), cursorShip.getScale()));
        removeCursorShip();
    }

    public void removeCursorShip(){
        cursorShipAttached = false;
        cursorShip = null;
    }

    public void moveCursorShip(){
        if(!cursorShipAttached)
            return;
        Vector3f currentCell = ownPlayingField.getCurrentPointedCell();
        if(true) { //TODO get if ship is allowed to be placed at currentCell)
            cursorShip.setAdditionalColor(GREEN);
            cursorShip.setAdditionalColorPercentage(MIXPERCENTAGE);
        }
        else{
            cursorShip.setAdditionalColor(RED);
            cursorShip.setAdditionalColorPercentage(MIXPERCENTAGE);
        }
        if(currentCell == null || (int) currentCell.z == PlayingField.OPPONENTFIELD) {
            cursorShipOnGrid = false;
            return;
        }
        cursorShipOnGrid = true;
        cursorShip.setPosition(ownPlayingField.calculateShipPosition(PlayingField.OWNFIELD, new Vector2f(currentCell.x, currentCell.y), cursorShipSize, cursorShipDirection));
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
}
