package com.battleships.gui.renderingEngine;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.models.ModelTexture;
import com.battleships.gui.models.RawModel;
import com.battleships.gui.models.TexturedModel;
import com.battleships.gui.shaders.StaticShader;
import com.battleships.gui.toolbox.Maths;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

public class EntityRenderer {

    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix){
        this.shader = shader;

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Map<TexturedModel, List<Entity>> entities){
        //prepare each textured model, then prepare each entity that uses this texturedModel and render that entity
        //only needs to load each model and texture once even if it's used multiple times
        //only entities need to be loaded one by one to get their transformationMatrix
        //after rendering all entities of one texturedModel unbind that model
        for(TexturedModel model:entities.keySet()){
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for(Entity entity:batch){
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }

    }

    private void prepareTexturedModel(TexturedModel texturedModel){
        RawModel model = texturedModel.getRawModel();
        GL30.glBindVertexArray(model.getVaoID()); //bind vaos of model to be loaded
        GL20.glEnableVertexAttribArray(0); //enable vao at index 0 (positions)
        GL20.glEnableVertexAttribArray(1);//enable vao at index 1 (textureCoords)
        GL20.glEnableVertexAttribArray(2);//enable vao at index 2 (normals)
        ModelTexture texture = texturedModel.getTexture();
        if(texture.isHasTransparency()){ //Render backside of faces for transparent models
            MasterRenderer.disableCulling();
        }
        shader.loadFakeLightingVariable(texture.isUseFakeLighting());
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity()); //pass the shineDamper and reflectivity values from the texture to the shader
        GL13.glActiveTexture(GL13.GL_TEXTURE0); //activate texture bank 0
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID()); //bind texture from model to render
    }

    //disable currently loaded model vaos and then unbind them
    private void unbindTexturedModel(){
        MasterRenderer.enableCulling(); //enable culling in case it has been disabled
        GL20.glDisableVertexAttribArray(0); //disable position vao
        GL20.glDisableVertexAttribArray(1); //disable textureCoords vao
        GL20.glDisableVertexAttribArray(2); //disable normals vao
        GL30.glBindVertexArray(0); //unbind vaos
    }

    private void prepareInstance(Entity entity){
        //create transfMatrix with current position rotation and scale of entity
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix); //pass transfMatrix to shader
    }

}
