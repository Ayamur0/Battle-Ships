package com.battleships.gui.particles;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.models.RawModel;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

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
     * @param particles - List of particles to be rendered on screen
     * @param camera - camera that displays particles
     */
    protected void render(List<Particle> particles, Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        prepare();
        for(Particle particle : particles){
            updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
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
//        modelMatrix._m00(viewMatrix.m00());
//        modelMatrix._m01(viewMatrix.m10());
//        modelMatrix._m02(viewMatrix.m20());
//        modelMatrix._m10(viewMatrix.m01());
//        modelMatrix._m11(viewMatrix.m11());
//        modelMatrix._m12(viewMatrix.m21());
//        modelMatrix._m20(viewMatrix.m02());
//        modelMatrix._m21(viewMatrix.m12());
//        modelMatrix._m22(viewMatrix.m22());
//        modelMatrix.rotationZ(float)Math.toRadians(rotation), new Vector3f(0, 0, 1)); //TODO Z Rotation not working, is resetting x and y rotation
        modelMatrix.scale(new Vector3f(scale, scale, scale));
        Matrix4f modelViewMatrix = new Matrix4f();
        viewMatrix.mul(modelMatrix, modelViewMatrix);
        shader.loadModelViewMatrix(modelViewMatrix);
    }

    /**
     * Bind and enable vao of quad and position vbo.
     * Prepare OpenGl for rendering 2D particles.
     */
    private void prepare(){
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
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
