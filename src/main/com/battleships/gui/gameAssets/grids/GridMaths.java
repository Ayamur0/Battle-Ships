package com.battleships.gui.gameAssets.grids;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

/**
 * Math functions needed for the grids.
 * Mainly to convert between indices of the cells and world coordinates.
 *
 * @author Tim Staudenmaier
 */
public class GridMaths {

    private static GridManager gridManager;

    /**
     * Converts the rotation of a ship from the direction to degrees.
     *
     * @param rotation Direction the ship should be facing.
     * @return Degrees the entity of the ship needs to be rotated upon the y axis, so it faces the given direction.
     */
    protected static int calculateShipRotation(int rotation) {
        int degrees = 0;
        switch (rotation) {
            case ShipManager.NORTH:
                degrees = 0;
                break;
            case ShipManager.SOUTH:
                degrees = 180;
                break;
            case ShipManager.EAST:
                degrees = 270;
                break;
            case ShipManager.WEST:
                degrees = 90;
                break;
        }
        return degrees;
    }

    /**
     * Calculates the Coordinates the ship entity needs to have, to be placed on a given cell.
     *
     * @param grid     The grid the ship should be placed on.
     * @param indexInt The index of the cell the stern of the ship is on.
     * @param shipSize The size of the ship (2-5)
     * @param rotation The direction the ship should be facing (one of the constant values in {@link ShipManager} for the directions)
     * @return The OpenGl Coordinates the entity of the ship needs to be placed at.
     */
    protected static Vector3f calculateShipPosition(GuiGrid grid, Vector2i indexInt, int shipSize, int rotation) {
        Vector3f position = new Vector3f();
        Vector2f indexFloat = new Vector2f(indexInt);
        if (rotation == ShipManager.NORTH || rotation == ShipManager.SOUTH) {
            //move index so it's at center of ship instead of the cell the back part of the ship is on
            //upwards if ship is facing north, downwards if ship is facing south
            indexFloat.y += rotation == ShipManager.NORTH ? -(shipSize - 1) / 2f : (shipSize - 1) / 2f;
            Vector2f coords = convertIndextoCoords(indexFloat, grid);

            position.x = coords.x;
            position.y -= 3;
            position.z = coords.y;
        }
        if (rotation == ShipManager.EAST || rotation == ShipManager.WEST) {
            //move index so it's at center of ship instead of back
            //right if ship is facing east, left is ship is facing west
            indexFloat.x += rotation == ShipManager.EAST ? (shipSize - 1) / 2f : -(shipSize - 1) / 2f;
            Vector2f coords = convertIndextoCoords(indexFloat, grid);

            position.x = coords.x - 0.5f;
            position.y -= 3;
            position.z = coords.y - 0.5f;
        }
        return position;
    }

    /**
     * Convert the index of a cell to world coordinates pointing to the center of that cell.
     *
     * @param i     The index of the cell.
     * @param field The field the cell is on.
     * @return The world coordinates pointing at the center of the cell.
     */
    public static Vector2f convertIndextoCoords(Vector2f i, GuiGrid field) {
        int size = gridManager.getSize();
        Vector2f index = new Vector2f(i.x, i.y);
        index.x -= size / 2f;
        index.y -= size / 2f;
        index.x = field.getPosition().x + (index.x) * field.getScale() / (size + 1);
        index.y = field.getPosition().z + (index.y) * field.getScale() / (size + 1);
        return index;
    }

    /**
     * Calculates which cell is at the given coordinates.
     *
     * @param coords The coordinates at which it should be calculated what cell is at those coordinates. The
     *               y-value of the coordinates does not matter.
     * @return The index of the cell that is at the given coordinates, or {@code null} if there is no cell at the coordinates.
     */
    public static Vector3i convertCoordsToIndex(Vector3f coords) {
        GuiGrid ownGrid = gridManager.getOwnGrid();
        GuiGrid opponentGrid = gridManager.getOpponentGrid();
        int size = gridManager.getSize();
        float scale = gridManager.getScale();
        Vector3f result = new Vector3f();
        int field;

        if (coords.x > ownGrid.getPosition().x - ownGrid.getScale() / 2 + ownGrid.getScale() / (size + 1) &&
                coords.x < ownGrid.getPosition().x + ownGrid.getScale() / 2 &&
                coords.z > ownGrid.getPosition().z - ownGrid.getScale() / 2 + ownGrid.getScale() / (size + 1) &&
                coords.z < ownGrid.getPosition().z + ownGrid.getScale() / 2) {
            field = GridManager.OWNFIELD;
            result.x = coords.x - ownGrid.getPosition().x + ownGrid.getScale() / 2f;
            result.y = coords.z - ownGrid.getPosition().z + ownGrid.getScale() / 2f;
        } else if (coords.x > opponentGrid.getPosition().x - opponentGrid.getScale() / 2 + opponentGrid.getScale() / (size + 1) &&
                coords.x < opponentGrid.getPosition().x + opponentGrid.getScale() / 2 &&
                coords.z > opponentGrid.getPosition().z - opponentGrid.getScale() / 2 + opponentGrid.getScale() / (size + 1) &&
                coords.z < opponentGrid.getPosition().z + opponentGrid.getScale() / 2) {
            field = GridManager.OPPONENTFIELD;
            result.x = coords.x - opponentGrid.getPosition().x + opponentGrid.getScale() / 2f;
            result.y = coords.z - opponentGrid.getPosition().z + opponentGrid.getScale() / 2f;
        } else
            return null;

        int indexX = (int) (result.x / (scale / (size + 1)));
        int indexY = (int) (result.y / (scale / (size + 1)));

        return new Vector3i(indexX, indexY, field);
    }

    /**
     * Set the GridManager these calculations are made for.
     *
     * @param gridManager The GridManager that uses these calculations.
     */
    public static void setGridManager(GridManager gridManager) {
        GridMaths.gridManager = gridManager;
    }
}
