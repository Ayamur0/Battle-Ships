package com.battleships.gui.entities;

import org.joml.Vector3f;

/**
 * Each scene can have one light source.
 * For the light to act like a sun it's position needs to be set far away from the scene and the color should be white.
 *
 * @author Tim Staudenmaier
 */

public class Light {

    /**
     * Position of this light in the world.
     */
    private Vector3f position;
    /**
     * Color of the light this light emits (r,g,b all between 0 and 1).
     */
    private Vector3f color;

    /**
     * Create a new light.
     * @param position Position where the light should be created (world coordinates)
     * @param color Color this light should emit (r,g,b all between 0 and 1)
     */
    public Light(Vector3f position, Vector3f color) {
        this.position = position;
        this.color = color;
    }

    /**
     *
     * @return Current position of this light (world coordinates).
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Set position of this light to a new one.
     * @param position New position for this light (world coordinates)
     */
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    /**
     *
     * @return Color of light this light is emitting.
     */
    public Vector3f getColor() {
        return color;
    }

    /**
     * Set color of the light this light is emitting to a new color.
     * @param color New color of this light (r,g,b all between 0 and 1).
     */
    public void setColor(Vector3f color) {
        this.color = color;
    }
}
