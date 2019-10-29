package com.battleships.gui.Engine.Rendering.Models;

import com.battleships.gui.Engine.Rendering.Entities.Entity;
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

public class Renderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;

    public Renderer(StaticShader shader){
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

    public void render(Entity entity, StaticShader shader){
        TexturedModel texturedModel = entity.getModel();
        RawModel model = texturedModel.getRawModel();
        GL30.glBindVertexArray(model.getVaoID()); //bind vaos of model to be loaded
        GL20.glEnableVertexAttribArray(0); //enable vao at index 0 (positions)
        GL20.glEnableVertexAttribArray(1);//enable vao at index 1 (textureCoords)
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale()); //create transfMatrix with current position rotation and scale
        shader.loadTransformationMatrix(transformationMatrix); //pass transfMatrix to shader
        GL13.glActiveTexture(GL13.GL_TEXTURE0); //activate texture bank 0
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID()); //bind texture from model to render
        //Render model with triangles and vertexCount vertices, which are unsigned int starting at index 0
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0); //disable position vao
        GL20.glDisableVertexAttribArray(1); //disable textureCoords vao
        GL30.glBindVertexArray(0); //unbind vaos
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
