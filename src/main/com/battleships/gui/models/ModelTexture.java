package com.battleships.gui.models;

/**
 * Texture for a {@link TexturedModel}.
 *
 * @author Tim Staudenmaier
 */
public class ModelTexture {

    /**
     * OpenGl Id of the texture.
     */
    private int textureID;

    /**
     * How much the light, reflected by entities with this texture, spreads.
     */
    private float shineDamper = 1;
    /**
     * How much light gets reflected by entities with this texture.
     */
    private float reflectivity = 0;

    /**
     * {@code true} if this texture has transparent parts, {@code false} else.
     */
    private boolean hasTransparency = false;
    /**
     * {@code true} if this texture needs to use fake lighting.
     * Needs to be used if texture is used on entities that are only one face thick and are visible from both sides.
     */
    private boolean useFakeLighting = false;

    /**
     * How many rows this textureAtlas has. 1 if it isn't a textureAtlas.
     */
    private float numberOfRows = 1;

    /**
     * Create a new ModelTexture.
     * @param textureID Texture ID of the texture this ModelTexture should use.
     */
    public ModelTexture(int textureID) {
        this.textureID = textureID;
    }


    /**
     *
     * @return Number of rows this textureAtlas has. 1 if it isn't a textureAtlas.
     */
    public float getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * Set the number of rows in this texture.
     * @param numberOfRows numberOfRows this texture has.
     */
    public void setNumberOfRows(float numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    /**
     *
     * @return {@code true} if this texture uses transparency, {@code false} else.
     */
    public boolean isHasTransparency() {
        return hasTransparency;
    }

    /**
     * Set if this texture uses transparency.
     * @param hasTransparency {@code true} if this texture should use transparency.
     */
    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    /**
     *
     * @return {@code true} if this texture needs to use fake lighting.
     */
    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    /**
     * Set if this texture needs to use fake lighting.
     * @param useFakeLighting {@code true} if this texture should use fake lighting.
     */
    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }

    /**
     *
     * @return The ID of the texture.
     */
    public int getID() {
        return textureID;
    }

    /**
     *
     * @return The value of how much the light, reflected by entities with this texture, spreads.
     */
    public float getShineDamper() {
        return shineDamper;
    }

    /**
     * Set how much the light, reflected by entities with this texture, should spread.
     * @param shineDamper How much the light should spread.
     */
    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    /**
     * @return How much light gets reflected by entities with this texture.
     */
    public float getReflectivity() {
        return reflectivity;
    }

    /**
     * Set how much light should be reflected by entities with this texture.
     * @param reflectivity how much light should be reflected.
     */
    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
