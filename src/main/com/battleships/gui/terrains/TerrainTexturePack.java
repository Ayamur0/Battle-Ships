package com.battleships.gui.terrains;

import java.util.ArrayList;
import java.util.List;

public class TerrainTexturePack {

    private TerrainTexture pathTexture;
    private TerrainTexture gravelTexture;
    private TerrainTexture grassTexture;
    private TerrainTexture wetSandTexture;
    private TerrainTexture sandTexture;
    private List<TerrainTexture> textures = new ArrayList<>();

    public TerrainTexturePack(TerrainTexture pathTexture, TerrainTexture gravelTexture, TerrainTexture grassTexture, TerrainTexture wetSandTexture, TerrainTexture sandTexture) {
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
