package com.battleships.gui.engine.renderingEngine;

import com.battleships.gui.engine.entities.Entity;
import com.battleships.gui.engine.models.ModelTexture;
import com.battleships.gui.engine.models.RawModel;
import com.battleships.gui.engine.models.TexturedModel;
import com.battleships.gui.engine.shaders.TerrainShader;
import com.battleships.gui.engine.terrains.Terrain;
import com.battleships.gui.engine.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class TerrainRenderer {

    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(List<Terrain> terrains){
        //prepare each terrain and render that terrain
        //only needs to load each model and texture once even if it's used multiple times
        //only transformationMatrix needs to be loaded one by one to render the terrain side by side
        //after rendering all terrains unbindTerrain
        for (Terrain terrain : terrains){
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindTerrain();
        }
    }
    private void prepareTerrain(Terrain terrain){
        RawModel model = terrain.getModel();
        GL30.glBindVertexArray(model.getVaoID()); //bind vaos of model to be loaded
        GL20.glEnableVertexAttribArray(0); //enable vao at index 0 (positions)
        GL20.glEnableVertexAttribArray(1);//enable vao at index 1 (textureCoords)
        GL20.glEnableVertexAttribArray(2);//enable vao at index 2 (normals)
        ModelTexture texture = terrain.getTexture();
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity()); //pass the shineDamper and reflectivity values from the texture to the shader
        GL13.glActiveTexture(GL13.GL_TEXTURE0); //activate texture bank 0
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID()); //bind texture from model to render
    }

    //disable currently loaded model vaos and then unbind them
    private void unbindTerrain(){
        GL20.glDisableVertexAttribArray(0); //disable position vao
        GL20.glDisableVertexAttribArray(1); //disable textureCoords vao
        GL20.glDisableVertexAttribArray(2); //disable normals vao
        GL30.glBindVertexArray(0); //unbind vaos
    }

    private void loadModelMatrix(Terrain terrain){
        //create transfMatrix with current position rotation and scale of entity
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()),new Vector3f(), 1);
        shader.loadTransformationMatrix(transformationMatrix); //pass transfMatrix to shader
    }
}
