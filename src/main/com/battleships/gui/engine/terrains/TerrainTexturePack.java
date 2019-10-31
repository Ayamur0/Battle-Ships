package com.battleships.gui.engine.terrains;

import java.util.ArrayList;
import java.util.List;

public class TerrainTexturePack {

    private TerrainTexture waterTexture;
    private TerrainTexture pathTexture;
    private TerrainTexture gravelTexture;
    private TerrainTexture grassTexture;
    private TerrainTexture wetSandTexture;
    private TerrainTexture sandTexture;
    private List<TerrainTexture> textures = new ArrayList<>();

    public TerrainTexturePack(TerrainTexture waterTexture, TerrainTexture pathTexture, TerrainTexture gravelTexture, TerrainTexture grassTexture, TerrainTexture wetSandTexture, TerrainTexture sandTexture) {
        this.waterTexture = waterTexture;
        textures.add(waterTexture);
        this.pathTexture = pathTexture;
        textures.add(pathTexture);
        this.gravelTexture = gravelTexture;
        textures.add(gravelTexture);
        this.grassTexture = grassTexture;
        textures.add(grassTexture);
        this.wetSandTexture = wetSandTexture;
        textures.add(wetSandTexture);
        this.sandTexture = sandTexture;
        textures.add(sandTexture);
    }

    public TerrainTexture getWaterTexture() {
        return waterTexture;
    }

    public TerrainTexture getPathTexture() {
        return pathTexture;
    }

    public TerrainTexture getGravelTexture() {
        return gravelTexture;
    }

    public TerrainTexture getGrassTexture() {
        return grassTexture;
    }

    public TerrainTexture getWetSandTexture() {
        return wetSandTexture;
    }

    public TerrainTexture getSandTexture() {
        return sandTexture;
    }

    public List<TerrainTexture> getTextures() {
        return textures;
    }
}
