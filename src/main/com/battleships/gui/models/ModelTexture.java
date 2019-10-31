package com.battleships.gui.models;

public class ModelTexture {

    private int textureID;

    private float shineDamper = 1; //how far reflected light spreads
    private float reflectivity = 0; //how much light gets reflected

    private boolean hasTransparency = false;
    private boolean useFakeLighting = false; //to render objects that are only one face thick better, because normals only face to one side, so the other would be dark without this option

    public ModelTexture(int textureID) {
        this.textureID = textureID;
    }

    public boolean isHasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
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
