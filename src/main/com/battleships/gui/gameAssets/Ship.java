package com.battleships.gui.gameAssets;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.models.TexturedModel;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

public abstract class Ship {

    protected static final int NORTH = 0;
    protected static final int EAST = 1;
    protected static final int SOUTH = 2;
    protected static final int WEST = 3;

    //xMultiplier, zMultiplier, yValue, standardSize
    private static final float[][] STANDARDSHIPVALUES = {{},
                                                        {},
                                                        {},
                                                        {0.615f, 1.266f, -2.7f, 1}};

    private TexturedModel model;

    protected int standardSize;
    protected float xMultiplier;
    protected float zMultiplier;
    protected float yValue;
    protected float getYRotation;

    /**
     * Adds ship on certain spot of a playingField.
     * The ship is always placed, so that the back of the ship is on the given index and
     * the rest of the ship is in the next cells in the specified direction.
     * ex: index 3 with ship size 4 and facing east: ship would be on cells 3, 4, 5, 6
     * @param entities - List of entities the ship should be added to
     * @param gridPosition - center x and z Position of the grid the ship should be placed on
     * @param gridSideLength - length of a side of the grid in world coordinates
     * @param gridSize - count of cells per grid side
     * @param gridIndex - index of the cell the ship should be placed on
     * @param facing - direction the ship should face
     * @return the index the ship was added at in the entities list
     */
    protected int addShip(List<Entity> entities, Vector2f gridPosition, int gridSideLength, int gridSize, int gridIndex, int facing){
        Entity dummy = new Entity(model, new Vector3f(), new Vector3f(), standardSize);
        dummy.getPosition().x = gridPosition.x * xMultiplier + (gridIndex % gridSize) * (float) gridSideLength / (gridSize + 1);
        dummy.getPosition().y = yValue;
        dummy.getPosition().z = gridPosition.y * zMultiplier + (int)(gridIndex / gridSize) * (float) gridSideLength / (gridSize + 1);
        switch (facing){
            case NORTH: break;
            case EAST:
                dummy.getRotation().y += 90;
                break;
            case SOUTH:
                dummy.getRotation().y += 180;
                break;
            case WEST:
                dummy.getRotation().y += 270;
                break;
            default: break;
        }
        entities.add(dummy);
        return entities.size() - 1;
    }

    protected void setAttributes(){

    }
}
