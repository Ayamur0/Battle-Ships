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

/**
 * Class that handles placing ships with the mouse.
 *
 * @author Tim Staudenmaier
 */
public class ShipManager {

    /**
     * Colors the ships get overlapped with to indicate whether the ship can be placed (GREEN) or not (RED).
     */
    private static final Vector3f GREEN = new Vector3f(0,1,0);
    private static final Vector3f RED = new Vector3f(1,0,0);
    private static final float MIXPERCENTAGE = 0.5f;

    /**
     * Constants for the four directions a ship can face.
     */
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;

    /**
     * Array containing all the models for the different ship sizes.
     */
    private TexturedModel[] ships;

    /**
     * PlayingField this ShipManager should place the ships on.
     */
    private PlayingField ownPlayingField;
    /**
     * ShipSelector GUI needed to select the ships.
     */
    private ShipSelector shipSelector;
    /**
     * Size of the grid the ships are placed on.
     */
    private int gridSize;

    /**
     * Cursor Ship related:
     *          cursorShip          - Entity of the ship, that is currently following the mouse cursor, so it can be placed.
     *          cursorShipSize      - Size of the ship following the cursor.
     *          cursorShipDirection - Direction the ship following the cursor has.
     *          cursorShipAttached  - {@code true} if a ship is currently following the cursor, {@code false} else.
     *          cursorShipOnGrid    - {@code true} if the ship following the cursor is currently on the grid of the player, {@code false} else.
     */
    private Entity cursorShip;
    private int cursorShipSize;
    private int cursorShipDirection;
    private boolean cursorShipAttached;
    private boolean cursorShipOnGrid;

    /**
     * Create a new shipManager for a PlayingField.
     * @param loader - Loader to load models.
     * @param ownPlayingField - PlayingField this ShipManager should be created for.
     */
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

    /**
     * Create a entity from the corresponding model to place a ship. Gets added to a list (intended to be the ships list from
     * the PlayingField).
     * @param entities - List to add this new ship entity to.
     * @param size - Size of the ship that should be placed.
     * @param position - Position (world coordinates) at which the ship should be placed.
     * @param rotation - Rotation of the ship (as rotation around x,y and z axis in the world).
     * @param scale - Scale of the ship (should for normal grids always be 1).
     */
    public void placeShip(List<Entity> entities, int size, Vector3f position, Vector3f rotation, float scale){
        entities.add(new Entity(ships[size - 2], position, rotation, scale));
    }

    /**
     * Make a ship following the cursor so it can be placed by clicking.
     * @param shipSize - Size of the ship that should follow the cursor.
     */
    public void stickShipToCursor(int shipSize) {
        cursorShipSize = shipSize;
        cursorShipDirection = NORTH;
        cursorShip = new Entity(ships[shipSize - 2], new Vector3f(), new Vector3f(), 1f);
        cursorShipAttached = true;
    }

    /**
     * Stick a already existing ship to the cursor (to edit already placed ships).
     * @param ship
     */
    public void stickShipToCursor(Ship ship){
        //TODO get shipsize, entity and direction from logic
    }

    /**
     * Render the ship that is following the cursor. Render method in the passed renderer
     * needs to be called separately, this only adds the entity to the renderer)
     * @param renderer - Renderer that should render the ship.
     */
    public void renderCursorShip(MasterRenderer renderer){
        if(cursorShipAttached && cursorShipOnGrid)
            renderer.processEntity(cursorShip);
    }

    /**
     * Place the ship that is following the cursor on the grid.
     */
    public void placeCursorShip(){
        if(!cursorShipAttached)
            return;
        cursorShip.setAdditionalColorPercentage(0);
        shipSelector.decrementCount(cursorShipSize);
        GameManager.placeShip(new Vector2i(ownPlayingField.getCurrentPointedCell().x, ownPlayingField.getCurrentPointedCell().y), cursorShipSize, cursorShipDirection, PlayingField.OWNFIELD);
        //ownPlayingField.placeShip(new Vector2i(ownPlayingField.getCurrentPointedCell().x, ownPlayingField.getCurrentPointedCell().y), cursorShipSize, cursorShipDirection);
        removeCursorShip();
    }

    /**
     * Removes the ship following the cursor without placing it.
     */
    public void removeCursorShip(){
        cursorShipAttached = false;
        cursorShip = null;
    }

    /**
     * Move the ship following the cursor to the new cursor position, if the cursor has moved.
     * Needs to be called every frame so the ship can correctly follow the cursor.
     */
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

    /**
     * Rotates the ship following the cursor clockwise by 90 degrees.
     */
    public void rotateShip(){
        if(!cursorShipAttached)
            return;
        cursorShipDirection++;
        cursorShipDirection %= 4;
        cursorShip.getRotation().y = ownPlayingField.calculateShipRotation(cursorShipDirection);
    }

    /**
     * Set the GUI for selecting the ships.
     * @param shipSelector - The shipSelector that is currently1 used for selecting the ships.
     */
    public void setShipSelector(ShipSelector shipSelector) {
        this.shipSelector = shipSelector;
    }

    /**
     * @return - size of the playingField this ShipManager belongs to.
     */
    public int getGridSize() {
        return gridSize;
    }
}
