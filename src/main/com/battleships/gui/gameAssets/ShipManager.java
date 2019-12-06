package com.battleships.gui.gameAssets;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.models.TexturedModel;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.renderingEngine.MasterRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class ShipManager {

    protected static final int NORTH = 0;
    protected static final int EAST = 1;
    protected static final int SOUTH = 2;
    protected static final int WEST = 3;

    private Entity[] ships;
    private PlayingField ownPlayingField;

    private Entity cursorShip;
    private int cursorShipSize;
    private int cursorShipDirection;
    private boolean cursorShipAttached;

    public ShipManager(Loader loader, PlayingField ownPlayingField){
        this.ownPlayingField = ownPlayingField;
        ships = new Entity[4];
        ships[0] = (loader.loadEntityfromOBJ("ship2", "ship2.tga", 10, 1));
        ships[1] = (loader.loadEntityfromOBJ("ship3", "ship3.jpg", 10, 1));
        ships[2] = (loader.loadEntityfromOBJ("ship4", "ship4.tga", 10, 1));
        ships[3] = (loader.loadEntityfromOBJ("ship5new", "ship5.jpg", 10, 1));
    }

    public int placeShip(List<Entity> entities, int size, Vector3f position, Vector3f rotation, float scale){
        Entity toAdd = ships[size - 2];
        toAdd.setPosition(position);
        toAdd.setRotation(rotation);
        toAdd.setScale(scale);
        entities.add(toAdd);
        return entities.size() - 1;
    }

    public void stickShipToCursor(int shipSize) {
        cursorShipAttached = true;
        cursorShipSize = shipSize;
        cursorShipDirection = NORTH;
        cursorShip = ships[shipSize - 2];
    }

    private void renderCursorShip(MasterRenderer renderer){
        if(cursorShip != null)
            renderer.processEntity(cursorShip);
    }

    public void removeCursorShip(){
        cursorShip = null;
    }

    public void moveCursorShip(MasterRenderer renderer){
        if(!cursorShipAttached)
            return;
        Vector3f currentCell = ownPlayingField.getCurrentPointedCell();
        if(currentCell == null)
            return;
        cursorShip.setPosition(ownPlayingField.calculateShipPosition(PlayingField.OWNFIELD, new Vector2f(currentCell.x, currentCell.y), cursorShipSize, cursorShipDirection));
        renderCursorShip(renderer);
    }

    public void rotateShip(){
        if(!cursorShipAttached)
            return;
        cursorShipDirection++;
        cursorShipDirection %= 4;
        cursorShip.getRotation().y = ownPlayingField.calculateShipRotation(cursorShipDirection);
    }
}
