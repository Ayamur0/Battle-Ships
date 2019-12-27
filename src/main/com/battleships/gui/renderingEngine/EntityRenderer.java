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

/**
 * Renderer used for rendering {@link Entity}.
 * Uses a {@link StaticShader}.
 * 
 * @author Tim Staudenmaier
 */
public class EntityRenderer {

    /**
     * Shader used by this renderer.
     */
    private StaticShader shader;

    /**
     * Create a new renderer-
     * @param shader A StaticShader for this renderer.
     * @param projectionMatrix The current projectionMatrix of the window.
     */
    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix){
        this.shader = shader;

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Renders all Entities in the given has Map. Entities are grouped by their TexturedModel
     * for faster rendering, because models with the same texture get rendered together.
     * @param entities HashMap containing all Entities that should be rendered grouped by their TexturedModels.
     */
    public void render(Map<TexturedModel, List<Entity>> entities){
        shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
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

    /**
     * Prepare this renderer and shader to render {@link Entity} using a specific {@link TexturedModel}.
     * @param texturedModel TexturedModel the entities that get rendered next use.
     */
    private void prepareTexturedModel(TexturedModel texturedModel){
        RawModel model = texturedModel.getRawModel();
        GL30.glBindVertexArray(model.getVaoID()); //bind vaos of model to be loaded
        GL20.glEnableVertexAttribArray(0); //enable vao at index 0 (positions)
        GL20.glEnableVertexAttribArray(1);//enable vao at index 1 (textureCoords)
        GL20.glEnableVertexAttribArray(2);//enable vao at index 2 (normals)
        ModelTexture texture = texturedModel.getTexture();
        shader.loadNumberOfRows(texture.getNumberOfRows());
        if(texture.isHasTransparency()){ //Render backside of faces for transparent models
            MasterRenderer.disableCulling();
        }
        shader.loadFakeLightingVariable(texture.isUseFakeLighting());
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity()); //pass the shineDamper and reflectivity values from the texture to the shader
        GL13.glActiveTexture(GL13.GL_TEXTURE0); //activate texture bank 0
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID()); //bind texture from model to render
    }

    /**
     * Unbind the last used TexturedModel. 
     * Needs to be used if no other texturedModel will be bound that would overwrite
     * the previously bound TexturedModel.
     */
    private void unbindTexturedModel(){
        MasterRenderer.enableCulling(); //enable culling in case it has been disabled
        GL20.glDisableVertexAttribArray(0); //disable position vao
        GL20.glDisableVertexAttribArray(1); //disable textureCoords vao
        GL20.glDisableVertexAttribArray(2); //disable normals vao
        GL30.glBindVertexArray(0); //unbind vaos
    }

    /**
     * Prepare the shader to render a specific Entity.
     * @param entity The entity that should get rendered next.
     */
    private void prepareInstance(Entity entity){
        //create transfMatrix with current position rotation and scale of entity
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.loadMixColor(entity.getAdditionalColor(), entity.getAdditionalColorPercentage());
        shader.loadTransformationMatrix(transformationMatrix); //pass transfMatrix to shader
        shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
    }

}
