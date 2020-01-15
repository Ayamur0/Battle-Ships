package com.battleships.gui.postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class ResolutionChanger {

    /**
     * Image renderer that renders the texture to the screen.
     */
    private ImageRenderer screenRenderer;
    /**
     * Image renderer that renders the scene into a fbo with the specified resolution.
     */
    private ImageRenderer texRenderer;
    /**
     * Shader for creating the texture with changed resolution.
     */
    private ResolutionShader shader;

    /**
     * Create new Resolution Changer.
     * The Resolution changer uses the {@link ResolutionShader} to change the resolution of the rendered Image
     * and a {@link ImageRenderer} that renders the changed scene to the screen.
     */
    public ResolutionChanger(int width, int height) {
        screenRenderer = new ImageRenderer();
        texRenderer = new ImageRenderer(width, height);
        shader = new ResolutionShader();
    }

    /**
     * Render the fbo to the screen.
     * @param texture - the fbo as texture to be rendered to the screen
     */
    public void render(int texture){
        shader.start();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        texRenderer.renderQuad();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texRenderer.getOutputTexture());
        screenRenderer.renderQuad();
        shader.stop();
    }

    /**
     * CleanUp on program exit.
     */
    public void cleanUp(){
        texRenderer.cleanUp();
        screenRenderer.cleanUp();
        shader.cleanUp();
    }

    /**
     * Changes the renderer so the texture it renders to has a new resolution.
     * @param width Width of the new resolution in pixels.
     * @param height Height of the new resolution in pixels.
     */
    public void changeResolution(int width, int height){
        texRenderer = new ImageRenderer(width, height);
    }
}
