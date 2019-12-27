package com.battleships.gui.gameAssets.grids;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.models.ModelTexture;
import com.battleships.gui.models.TexturedModel;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector3f;

/**
 * Specific {@link Entity} used for the grids the game is played on.
 *
 * @author Tim Staudenmaier
 */
public class GuiGrid extends Entity {

    /**
     * Vertices for creating a rectangle vao on which the grid texture can be rendered.
     */
    private static final float[] VERTICES = {-0.5f, 0.5f, 0, -0.5f, -0.5f, 0, 0.5f, -0.5f, 0, 0.5f, 0.5f, 0};
    /**
     * Indices for creating a rectangle vao on which the grid texture can be rendered.
     */
    private static final int[] INDICES = {0, 1, 3, 3, 1, 2};
    /**
     * TextureCoords for creating a rectangle vao on which the grid texture can be rendered.
     */
    private static final float[] TEXTURECOORDS = {0, 0, 0, 1, 1, 1, 1, 0};
    /**
     * Normals for creating a rectangle vao on which the grid texture can be rendered.
     */
    private static final float[] NORMALS = {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0};

    /**
     * Path of the texture for the grid.
     */
    private static final String playingfieldTexturePath = "PlayingField.png";

    /**
     * Create a new Grid Entity.
     * @param loader Loader to load the model.
     * @param position World coordinates of the center of this grid.
     * @param rotation Rotation of this grid.
     * @param scale Scale of this grid (300 for grid with size 30)
     * @param textureOffset Offset the texture needs depending on size of this grid.
     *                      Needed to map the right part of the texture to the grid.
     */
    public GuiGrid(Loader loader, Vector3f position, Vector3f rotation, float scale, float textureOffset) {
        super(new TexturedModel(loader.loadToVAO(VERTICES, TEXTURECOORDS, NORMALS, INDICES), new ModelTexture(loader.loadTexture(playingfieldTexturePath))), position, rotation, scale);
        super.getModel().getTexture().setNumberOfRows(textureOffset);
    }


}
