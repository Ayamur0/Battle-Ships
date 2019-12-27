package com.battleships.gui.skybox;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.models.RawModel;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Renderer capable of rendering a skybox.
 * Uses a {@link SkyboxShader}.
 * 
 * @author Tim Staudenmaier
 */
public class SkyboxRenderer {

    /**
     * Size of the skybox in world coordinate size.
     */
    private static final float SIZE = 750f;

    /**
     * Vertices for creating a cube, the skybox cubeTexture can
     * be rendered on.
     */
    private static final float[] VERTICES = {
            -SIZE,  SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            -SIZE,  SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE,  SIZE
    };

    /**
     * Order in which the textures are mapped to the sides of the skybox.
     */
    private static String[] TEXTURE_FILES = {"right", "left", "top", "bottom", "back", "front"};

    /**
     * Cube model the skybox is rendered on.
     */
    private RawModel cube;
    /**
     * ID of the cubeTexture for the skybox.
     */
    private int texture;
    /**
     * Shader for rendering the skybox.
     */
    private SkyboxShader shader;

    /**
     * Create a new renderer.
     * @param loader Loader to load model of skybox.
     * @param projectionMatrix Current projectionMatrix of the window.
     */
    public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix){
        cube = loader.loadToVAO(VERTICES, 3);
        texture = loader.loadCubeMap(TEXTURE_FILES);
        shader = new SkyboxShader();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Render the skybox of this renderer.
     * @param camera Camera that views the scene.
     */
    public void render(Camera camera){
//        GL11.glDepthMask(false);
//        GL11.glDepthRange(1f, 1f);
        shader.start();
        shader.loadViewMatrix(camera);
        //bind vao enable vbo and bind texture
        GL30.glBindVertexArray(cube.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);

        //render skybox
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());

        //disable vao and vbo
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        shader.stop();
//        GL11.glDepthRange(0f, 1f);
//        GL11.glDepthMask(true);
    }

    /**
     * Clean up all skybox related stuff.
     * Needs to be called on program exit.
     */
    public void cleanUp(){
        shader.cleanUp();
    }
}
