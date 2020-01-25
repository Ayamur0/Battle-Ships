package com.battleships.gui.particles;

/**
 * Texture for a {@link Particle}.
 *
 * @author Tim Staudenmaier
 */
public class ParticleTexture {

    /**
     * OpenGL Id of this texture.
     */
    private int textureID;
    /**
     * Number of rows of this TextureAtlas.
     */
    private int numberOfRows;
    /**
     * Determines what happens of multiple particles are overlapping each other.
     * If this is {@code true} then the colors of the particles get added, else they get rendered separately on top of
     * each other.
     */
    private boolean additive;

    /**
     * Create a new ParticleTexture.
     *
     * @param textureID    Texture ID for the texture this ParticleTexture should use.
     * @param numberOfRows Number of rows in the texture.
     * @param additive     {@code true} if the texture is additive.
     */
    public ParticleTexture(int textureID, int numberOfRows, boolean additive) {
        this.textureID = textureID;
        this.numberOfRows = numberOfRows;
        this.additive = additive;
    }

    /**
     * @return TextureId of this ParticleTexture
     */
    public int getTextureID() {
        return textureID;
    }

    /**
     * @return Amount of rows in this textureAtlas.
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * @return {@code true} if this texture is additive.
     */
    public boolean isAdditive() {
        return additive;
    }
}
