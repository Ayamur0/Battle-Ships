package com.battleships.gui.terrains;

/**
 * A single texture for a {@link Terrain}.
 *
 * @author Tim Staudemaier
 */
public class TerrainTexture {

    /**
     * ID of this texture.
     */
    private int textureID;

    /**
     * Create a new TerrainTexture for a texture ID.
     *
     * @param textureID Id of the texture this TerrainTexture should use.
     */
    public TerrainTexture(int textureID) {
        this.textureID = textureID;
    }

    /**
     * @return The ID of the texture this terrain texture uses.
     */
    public int getTextureID() {
        return textureID;
    }
}
