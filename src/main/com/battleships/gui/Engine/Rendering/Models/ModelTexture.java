package com.battleships.gui.Engine.Rendering.Models;

public class ModelTexture {

    private int textureID;

    private float shineDamper = 1; //how far reflected light spreads
    private float reflectivity = 0; //how much light gets reflected

    public ModelTexture(int textureID) {
        this.textureID = textureID;
    }

    public int getID() {
        return textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
