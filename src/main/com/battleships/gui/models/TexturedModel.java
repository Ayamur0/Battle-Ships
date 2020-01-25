package com.battleships.gui.models;

/**
 * TexturedModel needed for creating an {@link com.battleships.gui.entities.Entity}.
 *
 * @author Tim Staudenmaier
 */
public class TexturedModel {

    /**
     * Model of this texturedModel.
     */
    private RawModel rawModel;
    /**
     * Texture of this texturedModel.
     */
    private ModelTexture texture;

    /**
     * Create a new TexturedModel.
     *
     * @param rawModel Model
     * @param texture  Texture
     */
    public TexturedModel(RawModel rawModel, ModelTexture texture) {
        this.rawModel = rawModel;
        this.texture = texture;
    }

    /**
     * @return The model of this texturedModel.
     */
    public RawModel getRawModel() {
        return rawModel;
    }

    /**
     * @return The textures of this texturedModel.
     */
    public ModelTexture getTexture() {
        return texture;
    }
}
