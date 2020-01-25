package com.battleships.gui.gameAssets.grids;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.models.ModelTexture;
import com.battleships.gui.models.TexturedModel;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

/**
 * Specific {@link Entity} used to indicate which cell of the grid is currently being hovered.
 *
 * @author Tim Staudenmaier
 */
public class Highlighter extends Entity {

    /**
     * Vertices for creating a rectangle vao on which the highlighter texture can be rendered.
     */
    private static final float[] VERTICES = {-0.5f, 0.5f, 0, -0.5f, -0.5f, 0, 0.5f, -0.5f, 0, 0.5f, 0.5f, 0};
    /**
     * Indices for creating a rectangle vao on which the highlighter texture can be rendered.
     */
    private static final int[] INDICES = {0, 1, 3, 3, 1, 2};
    /**
     * TextureCoords for creating a rectangle vao on which the highlighter texture can be rendered.
     */
    private static final float[] TEXTURECOORDS = {0, 0, 0, 1, 1, 1, 1, 0};
    /**
     * Normals for creating a rectangle vao on which the highlighter texture can be rendered.
     */
    private static final float[] NORMALS = {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0};

    /**
     * Texture of the highlighter that indicates which cell the mouse cursor is hovering over.
     */
    private static final String highlightTexturePath = "transparentBlack.png";

    /**
     * Vector containing the cell the mouse is currently pointing at.
     * x and y values are the indices of the cell.
     * z value is the constant for the grid on which the pointed cell is.
     */
    private Vector3i currentPointedCell;

    /**
     * GridManager this highlighter is on.
     */
    private GridManager gridManager;

    /**
     * Create a highlighter.
     *
     * @param loader      Loader to load texture.
     * @param scale       Scale of the highlighter.
     * @param gridManager GridManager this highlighter is on.
     */
    public Highlighter(Loader loader, float scale, GridManager gridManager) {
        super(new TexturedModel(loader.loadToVAO(VERTICES, TEXTURECOORDS, NORMALS, INDICES), new ModelTexture(loader.loadTexture(highlightTexturePath))),
                new Vector3f(), new Vector3f(-90, 0, 0), scale);
        this.gridManager = gridManager;
        remove();
    }

    /**
     * Removes the highlighter by moving it under the terrain.
     * Used when the mouse isn't pointing at a cell.
     */
    private void remove() {
        super.getPosition().y = -1000;
    }

    /**
     * Highlights the cell at the intersection Point between the given Vector and the grid.
     * Used with mouse vector to highlight the cell the mouse is pointing at.
     *
     * @param intersectionPoint Vector that should intersect with grid to determine pointed cell.
     */
    public void highligtCell(Vector3f intersectionPoint) {
        currentPointedCell = GridMaths.convertCoordsToIndex(intersectionPoint);
        if (currentPointedCell == null) {
            remove();
            return;
        }
        place(new Vector2i(currentPointedCell.x, currentPointedCell.y), currentPointedCell.z);
    }

    /**
     * Highlight the cell at the index, by setting the highlighter to the cells position.
     *
     * @param index  index of the cell that should be highlighted.
     * @param gridID the ID of the grid the cell that should be highlighted is on.
     */
    private void place(Vector2i index, int gridID) {
        GuiGrid grid = gridManager.getGridByID(gridID);
        if (grid == null)
            return;
        Vector2f coords = GridMaths.convertIndextoCoords(new Vector2f(index), grid);
        Vector3f position = new Vector3f(coords.x, -2.51f, coords.y);
        super.setPosition(position);
    }

    /**
     * @return Vector containing the cell the mouse is currently pointing at.
     * x and y values are the indices of the cell.
     * z value is the constant for the grid on which the pointed cell is.
     */
    public Vector3i getCurrentPointedCell() {
        return currentPointedCell;
    }
}
