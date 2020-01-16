package com.battleships.gui.postProcessing.gaussianBlur;

import com.battleships.gui.postProcessing.ImageRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * Effect that can be used by {@link com.battleships.gui.postProcessing.PostProcessing} to horizontally blur the image.
 * 
 * @author Tim Staudenmaier
 */
public class HorizontalBlur {

    /**
     * Renderer that renders the image after postProcessing.
     */
    private ImageRenderer renderer;
    /**
     * Shader needed for this effect.
     */
    private HorizontalBlurShader shader;

    /**
     * Create new HorizontalBlur post processing effect.
     * @param targetFboWidth width of the fbo this effect should affect
     * @param targetFboHeight height of the fbo this effect should affect
     */
    public HorizontalBlur(int targetFboWidth, int targetFboHeight){
        shader = new HorizontalBlurShader();
        shader.start();
        shader.loadTargetWidth(targetFboWidth);
        shader.stop();
        renderer = new ImageRenderer(targetFboWidth, targetFboHeight);
    }

    /**
     * Changes the renderer so the texture it renders to has a new resolution.
     * @param width Width of the new resolution in pixels.
     * @param height Height of the new resolution in pixels.
     */
    public void changeResolution(int width, int height){
        renderer = new ImageRenderer(width / 4, height / 4);
    }

    /**
     * Render the horizontal blur.
     * @param texture the texture of the current scene (fbo)
     */
    public void render(int texture){
        shader.start();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        renderer.renderQuad();
        shader.stop();
    }

    /**
     * @return the texture of the current scene after the effect was applied.
     */
    public int getOutputTexture(){
        return renderer.getOutputTexture();
    }

    /**
     * Clean up on program exit.
     */
    public void cleanUp(){
        renderer.cleanUp();
        shader.cleanUp();
    }
}
