package com.battleships.gui.guis;

import org.joml.Vector2f;

public class GuiTexture {

    private int texture;
    private Vector2f positions;
    private Vector2f scale;

    public GuiTexture(int texture, Vector2f positions, Vector2f scale) {
        this.texture = texture;
        this.positions = positions;
        this.scale = scale;
    }

    public int getTexture() {
        return texture;
    }

    public Vector2f getPositions() {
        return positions;
    }

    public Vector2f getScale() {
        return scale;
    }
}
