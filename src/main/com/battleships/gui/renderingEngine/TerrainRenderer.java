package com.battleships.gui.renderingEngine;

import com.battleships.gui.models.RawModel;
import com.battleships.gui.shaders.TerrainShader;
import com.battleships.gui.terrains.Terrain;
import com.battleships.gui.terrains.TerrainTexture;
import com.battleships.gui.terrains.TerrainTexturePack;
import com.battleships.gui.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

/**
 * Renderer for rendering terrains. Uses a {@link TerrainShader} for rendering.
 */
public class TerrainRenderer {

    /**
     * Shader used for rendering.
     */
    private TerrainShader shader;

    /**
     * Create a new renderer.
     *
     * @param shader           TerrainShader for this renderer.
     * @param projectionMatrix - Current projectionMatrix of the window.
     */
    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();
        shader.stop();
    }

    /**
     * Render terrains to the screen.
     *
     * @param terrains List containing all the terrains that should be rendered.
     */
    public void render(List<Terrain> terrains) {
        //prepare each terrain and render that terrain
        //only needs to load each model and texture once even if it's used multiple times
        //only transformationMatrix needs to be loaded one by one to render the terrain side by side
        //after rendering all terrains unbindTerrain
        shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
        for (Terrain terrain : terrains) {
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindTerrain();
        }
    }

    /**
     * Prepare renderer to render a specific terrain.
     *
     * @param terrain Terrain that is rendered next.
     */
    private void prepareTerrain(Terrain terrain) {
        RawModel model = terrain.getModel();
        GL30.glBindVertexArray(model.getVaoID()); //bind vaos of model to be loaded
        GL20.glEnableVertexAttribArray(0); //enable vao at index 0 (positions)
        GL20.glEnableVertexAttribArray(1);//enable vao at index 1 (textureCoords)
        GL20.glEnableVertexAttribArray(2);//enable vao at index 2 (normals)
        bindTextures(terrain);
        shader.loadShineVariables(1, 0); //pass the shineDamper and reflectivity values from the texture to the shader
    }

    /**
     * Binds the textures of the terrain that gets rendered next.
     *
     * @param terrain Terrain that is rendered next.
     */
    private void bindTextures(Terrain terrain) {
        TerrainTexturePack texturePack = terrain.getTexturePack();
        int i = 0;
        //activate texture banks and bind textures from terrain to render
        for (TerrainTexture texture : texturePack.getTextures()) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
            GL13.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
            i++;
        }
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
    }

    /**
     * Unbind the currently bound terrain.
     * Needs to be called after the last terrain has been rendered.
     */
    private void unbindTerrain() {
        GL20.glDisableVertexAttribArray(0); //disable position vao
        GL20.glDisableVertexAttribArray(1); //disable textureCoords vao
        GL20.glDisableVertexAttribArray(2); //disable normals vao
        GL30.glBindVertexArray(0); //unbind vaos
    }

    /**
     * Load the transformationMatrix of the terrain that gets rendered next.
     *
     * @param terrain Terrain that is rendered next.
     */
    private void loadModelMatrix(Terrain terrain) {
        //create transfMatrix with current position rotation and scale of entity
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), new Vector3f(), 1);
        shader.loadTransformationMatrix(transformationMatrix); //pass transfMatrix to shader
    }
}
