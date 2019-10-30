package com.battleships.gui.Engine.Rendering.RenderingEngine;

import com.battleships.gui.Engine.Rendering.Entities.Entity;
import com.battleships.gui.Engine.Rendering.Models.ModelTexture;
import com.battleships.gui.Engine.Rendering.Models.RawModel;
import com.battleships.gui.Engine.Rendering.Models.TexturedModel;
import com.battleships.gui.Engine.Rendering.Shaders.StaticShader;
import com.battleships.gui.Engine.Toolbox.Maths;
import com.battleships.gui.Game.Main.SchiffeVersenken;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;

public class Renderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;
    private StaticShader shader;

    public Renderer(StaticShader shader){
        this.shader = shader;
        //disable rendering of the backside of faces (inside of models)
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void prepare(){
        //Clear window
        GL11.glEnable(GL11.GL_DEPTH_TEST); //Only render pixel closest to camera
        GL11.glClearColor(0.2f, 0.2f, 0.2f, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT);
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
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity()); //pass the shineDamper and reflectivity values from the texture to the shader
        GL13.glActiveTexture(GL13.GL_TEXTURE0); //activate texture bank 0
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID()); //bind texture from model to render
    }

    //disable currently loaded model vaos and then unbind them
    private void unbindTexturedModel(){
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

    private void createProjectionMatrix(){
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetWindowSize(SchiffeVersenken.getWindow(), w, h);
        int width = w.get(0);
        int height = h.get(0);
        float aspectRatio = (float) width / (float) height;
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix._m00(x_scale);
        projectionMatrix._m11(y_scale);
        projectionMatrix._m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix._m23(-1);
        projectionMatrix._m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix._m33(0);
//        projectionMatrix = new Matrix4f();
//        projectionMatrix.setPerspective((float)Math.toRadians(FOV), 800f / 600f, NEAR_PLANE, FAR_PLANE);
    }
}
