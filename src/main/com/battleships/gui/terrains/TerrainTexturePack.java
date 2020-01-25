package com.battleships.gui.terrains;

import java.util.ArrayList;
import java.util.List;

/**
 * TexturePack containing 5 {@link TerrainTexture}s. Every {@link Terrain} has one TerrainTexturePack.
 *
 * @author Tim Staudenmaier
 */
public class TerrainTexturePack {

    /**
     * List containing all {@link TerrainTexture}s in this TexturePack.
     */
    private List<TerrainTexture> textures = new ArrayList<>();

    /**
     * Create a new TexturePack from 5 {@link TerrainTexture}.
     * All textures should be tileable.
     *
     * @param pathTexture    Texture for a path
     * @param gravelTexture  Texture for gravel
     * @param grassTexture   Texture for grass
     * @param wetSandTexture Texture for wet sand
     * @param sandTexture    Texture for dry sand
     */
    public TerrainTexturePack(TerrainTexture pathTexture, TerrainTexture gravelTexture, TerrainTexture grassTexture, TerrainTexture wetSandTexture, TerrainTexture sandTexture) {
        textures.add(pathTexture);
        textures.add(gravelTexture);
        textures.add(grassTexture);
        textures.add(wetSandTexture);
        textures.add(sandTexture);
    }

    /**
     * @return A list containing all {@link TerrainTexture}s in this TexturePack.
     */
    public List<TerrainTexture> getTextures() {
        return textures;
    }
}
