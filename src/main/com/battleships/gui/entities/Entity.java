package com.battleships.gui.entities;

import com.battleships.gui.models.TexturedModel;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * All Objects (Entities) that should be placed in the scene need to be instances of this class.
 * The entities all need to be passed to the {@link com.battleships.gui.renderingEngine.EntityRenderer} every frame,
 * so they are visible in the scene.
 *
 * @author Tim Staudenmaier
 */
public class Entity {

    /**
     * Model of this entity.
     */
    private TexturedModel model;
    /**
     * Position and rotation of this entity (world coordinates)
     */
    private Vector3f position, rotation;
    /**
     * Scale of this entity (1 = scale of the original model)
     */
    private float scale;

    /**
     * These values can be used to overlay the normal texture of this model
     * with another color. The color of the model gets mixed with this color using a ratio of
     * additionalColorPercentage * additionalColor + 1 - additionalColorPercentage * originalColor.
     * If the additionalColorPercentage = 0, the model will have no additional color and if
     * additionalColorPercentage = 1, the model will completely have the additional color and none of it's
     * original colors.
     */
    private Vector3f additionalColor = new Vector3f();
    private float additionalColorPercentage = 0;

    /**
     * If the texture of the model is a textureAtlas this index specifies,
     * which texture of the textureAtlas should be used (staring top right with index 0, increasing to the right, the next row, ...)
     * If texture is no textureAtlas this value needs to stay at 0.
     */
    private int textureIndex = 0;

    /**
     * Create a new Entity for a TexturedModel without a textureAtlas.
     * @param model - TextureModel containing the model and texture the entity should use.
     * @param position - Position of the entity in the world.
     * @param rotation - Rotation of the entity around x,y and z axis.
     * @param scale - Scale of the model (1 for original model scale).
     */
    public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    /**
     * Create a new Entity for a TexturedModel with a textureAtlas.
     * @param model - TextureModel containing the model and texture the entity should use.
     * @param index - Index of the texture in the textureAtlas this Entity should use.
     * @param position - Position of the entity in the world.
     * @param rotation - Rotation of the entity around x,y and z axis.
     * @param scale - Scale of the model (1 for original model scale).
     */
    public Entity(TexturedModel model, int index, Vector3f position, Vector3f rotation, float scale) {
        this.textureIndex = index;
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    /**
     *
     * @return - The factor, that the x-textureCoordinate of this entity needs to be multiplied with, so the texture
     *          is mapped to the correct texture in the texture Atlas.
     */
    public float getTextureXOffset(){
        int column = textureIndex % (int)model.getTexture().getNumberOfRows();
        return (float)column / model.getTexture().getNumberOfRows();
    }


    /**
     *
     * @return - The factor, that the y-textureCoordinate of this entity needs to be multiplied with, so the texture
     *          is mapped to the correct texture in the texture Atlas.
     */
    public float getTextureYOffset(){
        int row = textureIndex / (int)model.getTexture().getNumberOfRows();
        return (float)row/model.getTexture().getNumberOfRows();
    }

    /**
     * Increase the position of this entity (decreases for negative values)
     * @param dx - How far to move this entity on the x-axis.
     * @param dy - How far to move this entity on the y-axis.
     * @param dz - How far to move this entity on the z-axis.
     */
    public void increasePosition(float dx, float dy, float dz){
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    /**
     * Increase the rotation of this entity (decreases for negative values)
     * @param dx - How far to rotate this entity along the x-axis.
     * @param dy - How far to rotate this entity along the y-axis.
     * @param dz - How far to rotate this entity along the z-axis.
     */
    public void increaseRotation(float dx, float dy, float dz){
        this.rotation.x += dx;
        this.rotation.y += dy;
        this.rotation.z += dz;
    }

    /**
     * Increase the scale (decrease for negative x)
     * @param x - How much to increase the scale.
     */
    public void increaseScale(float x){
        this.scale += x;
    }

    /**
     *
     * @return - TexturedModel this entity is using.
     */
    public TexturedModel getModel() {
        return model;
    }

    /**
     * Give this entity a new model that it should use.
     * @param model - New TexturedModel of this entity
     */
    public void setModel(TexturedModel model) {
        this.model = model;
    }

    /**
     *
     * @return - Current position of this entity in world coordinates.
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Set the position of this entity to a new position.
     * @param position - new position of this entity (world coordinates)
     */
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    /**
     *
     * @return - The current rotation of this entity.
     */
    public Vector3f getRotation() {
        return rotation;
    }

    /**
     * Set the rotation of this entity to a new rotation.
     * @param rotation - New rotation of this entity.
     */
    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    /**
     *
     * @return - The current scale of this entity.
     */
    public float getScale() {
        return scale;
    }

    /**
     * Set scale of this entity to a new value.
     * @param scale - New scale.
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     *
     * @return - The additional color this entity is overlapped with.
     */
    public Vector3f getAdditionalColor() {
        return additionalColor;
    }

    /**
     * Set the color this entity is overlapped with to a new color.
     * @param additionalColor - New color this entity should be overlapped with.
     */
    public void setAdditionalColor(Vector3f additionalColor) {
        this.additionalColor = additionalColor;
    }

    /**
     *
     * @return - How much of the additional color should be used on this entity (between 0 for none and 1 for only this color).
     */
    public float getAdditionalColorPercentage() {
        return additionalColorPercentage;
    }

    /**
     * Set how much of the additional color should be used on this entity.
     * @param additionalColorPercentage - How much color should be used  (between 0 for none and 1 for only this color).
     */
    public void setAdditionalColorPercentage(float additionalColorPercentage) {
        this.additionalColorPercentage = additionalColorPercentage;
    }
}
