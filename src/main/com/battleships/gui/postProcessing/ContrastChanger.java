package com.battleships.gui.postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class ContrastChanger {

    private ImageRenderer renderer;
    private ContrastShader shader;

    /**
     * Create new Contrast Changer.
     * The Contrast changer uses the {@link ContrastShader} to create the contrast effect
     * and a {@link ImageRenderer} that renders the changed scene to the screen.
     */
    public ContrastChanger() {
        shader = new ContrastShader();
        renderer = new ImageRenderer();
    }

    /**
     * Render the fbo to the screen.
     * @param texture - the fbo as texture to be rendered to the screen
     */
    public void render(int texture){
        shader.start();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        renderer.renderQuad();
        shader.stop();
    }

    /**
     * CleanUp on program exit.
     */
    public void cleanUp(){
        renderer.cleanUp();
        shader.cleanUp();
    }
}
