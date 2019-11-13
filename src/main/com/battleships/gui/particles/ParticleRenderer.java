package com.battleships.gui.particles;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.models.RawModel;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.gui.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

public class ParticleRenderer {

    private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
    //max number of particles on screen at once
    private static final int MAX_INSTANCES = 10000;
    //length of data for each particle
    //16 floats for viewMatrix
    //4 floats for the textureOffsets
    //1 float for the blend value
    private static final int INSTANCE_DATA_LENGTH = 21;

    //float buffer needed to store data into vbo, created once so it doesn't need to be created each time to save performance
    private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH);

    /**
     * @param
     */

    private RawModel quad;
    private ParticleShader shader;

    private Loader loader;
    private int vbo;
    private int pointer = 0;

    /**
     * Load a quad model using VERTICES to render particles on.
     * Create vbo to save all particle instance attributes in
     * Create new shader program and add projection matrix to it.
     * @param loader - Loader to load models
     * @param projectionMatrix - ProjectionMatrix for shader
     */
    protected ParticleRenderer(Loader loader, Matrix4f projectionMatrix){
        this.loader = loader;
        this.vbo = loader.createEmptyVbo(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
        quad = loader.loadToVAO(VERTICES, 2);
        //add the attributes for the columns of the viewMatrix (each column is stored as one 4D vector)
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 1, 4, INSTANCE_DATA_LENGTH, 0);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 2, 4, INSTANCE_DATA_LENGTH, 4);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 3, 4, INSTANCE_DATA_LENGTH, 8);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 4, 4, INSTANCE_DATA_LENGTH, 12);
        //add attribute for the 2 textureCoordOffsets (stored as one 4D Vector)
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 5, 4, INSTANCE_DATA_LENGTH, 16);
        //add attribute for the blend value
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 6, 4, INSTANCE_DATA_LENGTH, 20);
        shader = new ParticleShader();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Render particles to the screen.
     * Loop through hashMap to render all particles with the same texture
     * together, so texture only needs to be changed once for each particle group, for better performance.
     * Bind texture and store viewMatrix data and texture data in float array for instanced rendering.
     * Then loop through list of particles with that texture and render them using instanced rendering.
     * @param particles - HashMap of textures and their corresponding list of particles to be rendered on screen
     * @param camera - camera that displays particles
     */
    protected void render(Map<ParticleTexture, List<Particle>> particles, Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        prepare();
        for(ParticleTexture texture : particles.keySet()) {
            bindTexture(texture);
            List<Particle> particleList = particles.get(texture);
            pointer = 0;
            float[] vboData = new float[particleList.size() * INSTANCE_DATA_LENGTH];
            for (Particle particle : particleList) {
                updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMatrix, vboData);
                updateTexCoordInfo(particle, vboData);
            }
            loader.updateVbo(vbo, vboData, buffer);
            GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), particleList.size());
        }
        finishRendering();
    }

    protected void cleanUp(){
        shader.cleanUp();
    }

    /**
     * Store texture data for current particle in float array so it can be used to render the particle with instanced rendering
     * @param particle
     * @param data
     */
    private void updateTexCoordInfo(Particle particle, float[] data){
        data[pointer++] = particle.getTexOffset1().x;
        data[pointer++] = particle.getTexOffset1().y;
        data[pointer++] = particle.getTexOffset2().x;
        data[pointer++] = particle.getTexOffset2().y;
        data[pointer++] = particle.getBlend();
    }


    /**
     *  Set OpenGL mode to add color of particles if additive is {@code true} in texture or render particles over each other.
     *  Bind texture to OpenGL and upload numberOfRows of texture (textureAtlas) to the shader.
     * @param texture - texture to bind to openGL
     */

    private void bindTexture(ParticleTexture texture){

        if(texture.isAdditive())
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        else
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        shader.loadNumberOfRows(texture.getNumberOfRows());
    }

    /**
     * Update viewMatrix for particle that should currently be rendered, to make particle move, scale and rotate.
     * Changes the viewMatrix (rotation part which is top left 3x3) so particle always faces camera.
     * Coverts viewMatrix to float array to make it usable with instanced rendering.
     * @param position - new position of the particle
     * @param rotation - new rotation of the particle (only Z rotation)
     * @param scale - scale of the particle (1 is standard)
     * @param viewMatrix - viewMatrix currently used by the camera
     * @param vboData - float array to safe the data to, so it can be used to render particles with instanced rendering.
     */
    private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix, float[] vboData){
        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.translate(position);
        //set the rotation 3x3 part of the model matrix to transpose of view matrix
        //rotation part of view and model matrix combined then looks like this:
        //1 0 0
        //0 1 0
        //0 0 1
        //which means no rotation because particle rotation cancels out camera rotation, so particle is always facing camera
        viewMatrix.transpose3x3(modelMatrix);
        modelMatrix.scale(new Vector3f(scale, scale, scale));
        Matrix4f modelViewMatrix = new Matrix4f();
        viewMatrix.mul(modelMatrix, modelViewMatrix);
        modelViewMatrix.rotate((float)Math.toRadians(rotation), new Vector3f(0, 0, 1));
        storeMatrixData(modelViewMatrix, vboData);
    }

    /**
     * Store all values from a matrix in a float array.
     * Values are ordered by columns, so first 4 values in array are first column from matrix.
     * @param matrix - matrix to convert to float array
     * @param vboData - float array matrix values should be saved to
     */
    private void storeMatrixData(Matrix4f matrix, float[] vboData){
        vboData[pointer++] = matrix.m00();
        vboData[pointer++] = matrix.m01();
        vboData[pointer++] = matrix.m02();
        vboData[pointer++] = matrix.m03();
        vboData[pointer++] = matrix.m10();
        vboData[pointer++] = matrix.m11();
        vboData[pointer++] = matrix.m12();
        vboData[pointer++] = matrix.m13();
        vboData[pointer++] = matrix.m20();
        vboData[pointer++] = matrix.m21();
        vboData[pointer++] = matrix.m22();
        vboData[pointer++] = matrix.m23();
        vboData[pointer++] = matrix.m30();
        vboData[pointer++] = matrix.m31();
        vboData[pointer++] = matrix.m32();
        vboData[pointer++] = matrix.m33();
    }

    /**
     * Bind and enable vao of quad and all vbos.
     * Prepare OpenGl for rendering 2D particles.
     */
    private void prepare(){
        shader.start();
        shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
        GL30.glBindVertexArray(quad.getVaoID());
        for(int i = 0; i <= 6; i++)
            GL20.glEnableVertexAttribArray(i);
        GL11.glEnable(GL11.GL_BLEND);
        //stop particles from being rendered to depth buffer
        GL11.glDepthMask(false);
    }

    /**
     * Undo all changes made to OpenGL for rendering particles, so normal objects can be rendered again.
     * Disable vao and vbos.
     */
    private void finishRendering(){
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        for(int i = 0; i <= 6; i++)
            GL20.glDisableVertexAttribArray(i);
        GL30.glBindVertexArray(0);
        shader.stop();
    }
}
