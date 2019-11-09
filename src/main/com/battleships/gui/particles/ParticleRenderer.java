package com.battleships.gui.particles;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.models.RawModel;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

public class ParticleRenderer {

    private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};

    private RawModel quad;
    private ParticleShader shader;

    /**
     * Load a quad model using VERTICES to render particles on.
     * Create new shader program and add projection matrix to it.
     * @param loader - Loader to load models
     * @param projectionMatrix - ProjectionMatrix for shader
     */
    protected ParticleRenderer(Loader loader, Matrix4f projectionMatrix){
        quad = loader.loadToVAO(VERTICES, 2);
        shader = new ParticleShader();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Render particles to the screen.
     * Loop through hashMap to render all particles with the same texture
     * together, so texture only needs to be changed once for each particle group, for better performance.
     * Bind texture and loop through list of particles with that texture and render them.
     * @param particles - HashMap of textures and their corresponding list of particles to be rendered on screen
     * @param camera - camera that displays particles
     * @param mode - mode to use for rendering 1 = add color of particles, 771 = render particles over each other
     */
    protected void render(Map<ParticleTexture, List<Particle>> particles, Camera camera, int mode){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        prepare(mode);
        for(ParticleTexture texture : particles.keySet()) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
            for (Particle particle : particles.get(texture)) {
                updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMatrix);
                shader.loadTextureCoordInfo(particle.getTexOffset1(), particle.getTexOffset2(), texture.getNumberOfRows(), particle.getBlend());
                GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
            }
        }
        finishRendering();
    }

    protected void cleanUp(){
        shader.cleanUp();
    }

    /**
     * Create viewMatrix for current particle, to make particle move, scale and rotate.
     * Particle is always rotated in a way so it faces the camera.
     * @param position - new position of particle
     * @param rotation - new rotation of particle
     * @param scale - scale of particle (1 is standard)
     * @param viewMatrix - viewMatrix currently used by camera
     */
    private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix){
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
        shader.loadModelViewMatrix(modelViewMatrix);
    }

    /**
     * Bind and enable vao of quad and position vbo.
     * Prepare OpenGl for rendering 2D particles.
     * @param mode - mode to use for rendering 1 = add color of particles, 771 = render particles over each other
     */
    private void prepare(int mode){
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, mode);
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
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }
}
