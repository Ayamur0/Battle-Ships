package com.battleships.gui.postProcessing;

import org.lwjgl.opengl.GL11;

public class ImageRenderer {

    private Fbo fbo;

    /**
     * Create a ImageRenderer which is used to render fbos.
     * @param width - width of the image that should be rendered
     * @param height - height of the image that should be rendered
     */
    public ImageRenderer(int width, int height) {
        this.fbo = new Fbo(width, height, Fbo.NONE);
    }

    public ImageRenderer() {}

    /**
     * Render a quad to the screen, on which the image texture can be rendered
     */
    public void renderQuad() {
        if (fbo != null) {
            fbo.bindFrameBuffer();
        }
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
        if (fbo != null) {
            fbo.unbindFrameBuffer();
        }
    }

    /**
     * @return The texture the fbo uses. (The current scene that got rendered)
     */
    public int getOutputTexture() {
        return fbo.getColorTexture();
    }

    /**
     * CleanUp on program exit
     */
    public void cleanUp() {
        if (fbo != null) {
            fbo.cleanUp();
        }
    }
}
