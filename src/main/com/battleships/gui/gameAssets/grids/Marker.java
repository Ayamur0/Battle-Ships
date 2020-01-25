package com.battleships.gui.gameAssets.grids;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.models.ModelTexture;
import com.battleships.gui.models.TexturedModel;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.renderingEngine.OBJLoader;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Specific {@link Entity} used to mark hit cells to indicate what they contained.
 *
 * @author Tim Staudenmaier
 */
public class Marker extends Entity {

    /**
     * Constant indicating a marker for water.
     */
    public static final int WATERMARKER = 0;
    /**
     * Constant indicating a marker for a hit ship.
     */
    public static final int SHIPMARKER = 1;
    /**
     * Path for the model of the markers.
     */
    private static final String buoyModelOBJ = "buoy";
    /**
     * The texturedModel for water markers.
     */
    private static TexturedModel buoyModelWhite;
    /**
     * The texturedModel for ship markers (used on opponents grid where ships aren't visible).
     */
    private static TexturedModel buoyModelRed;

    /**
     * Create a new marker.
     *
     * @param type  Type of the water ({@value WATERMARKER} or {@value SHIPMARKER})
     * @param index Index at which the marker should be placed.
     * @param grid  Grid on which the marker should be placed.
     */
    public Marker(int type, Vector2f index, GuiGrid grid) {
        super(null, null, new Vector3f(), 1);
        if (type == WATERMARKER)
            super.setModel(buoyModelWhite);
        else
            super.setModel(buoyModelRed);
        super.setPosition(calculatePosition(index, grid));
    }

    /**
     * Create the TexturedModel for the two marker types.
     *
     * @param loader Loader to load textures and models.
     */
    public static void createModels(Loader loader) {
        ModelTexture white = new ModelTexture(loader.loadTexture("white.png"));
        ModelTexture red = new ModelTexture(loader.loadTexture("red.png"));
        buoyModelWhite = new TexturedModel(OBJLoader.loadObjModel(buoyModelOBJ), white);
        buoyModelRed = new TexturedModel(OBJLoader.loadObjModel(buoyModelOBJ), red);
    }

    /**
     * Calculate the world position for this marker from the index.
     *
     * @param index Index this marker should be placed at.
     * @param grid  Grid this marker should be placed on.
     * @return World coordinates this marker needs to be placed at.
     */
    private Vector3f calculatePosition(Vector2f index, GuiGrid grid) {
        Vector2f coordsXZ = GridMaths.convertIndextoCoords(index, grid);
        return new Vector3f(coordsXZ.x, GridManager.getGRIDHEIGHT(), coordsXZ.y);
    }
}
