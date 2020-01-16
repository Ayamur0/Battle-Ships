package com.battleships.gui.postProcessing;

import com.battleships.gui.models.RawModel;
import com.battleships.gui.postProcessing.gaussianBlur.HorizontalBlur;
import com.battleships.gui.postProcessing.gaussianBlur.VerticalBlur;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.window.WindowManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Main class for all post processing effects.
 * Needs to always get initialized before any post processing is used.
 *
 * @author Tim Staudenmaier
 */

public class PostProcessing {

    /**
     * Positions so the image fully covers the window.
     */
    private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
    /**
     * Quad the image can get rendered on.
     */
    private static RawModel quad;
    /**
     * ResolutionChanger to influence contrast of the image.
     */
    private static ResolutionChanger resolutionChanger;
    /**
     * Horizontal blur effect to blur the image horizontally.
     */
    private static HorizontalBlur hBlur;
    /**
     * Vertical blur effect to blur the image vertically.
     */
    private static VerticalBlur vBlur;
//    private static HorizontalBlur hBlur2;
//    private static VerticalBlur vBlur2;
    /**
     * Creates a quad that fills the whole screen to render the fbo on.
     * And initializes all post processing effects.
     * @param loader loader to load vao
     */
    public static void init(Loader loader){
        quad = loader.loadToVAO(POSITIONS, 2);
        //divide width and height to make texture smaller, so it gets scaled when being put on the screen
        //which makes it blurrier while saving performance
        //use two blur stages to decrease flickering after the blur because of the low res texture
        resolutionChanger = new ResolutionChanger(WindowManager.getWidth(), WindowManager.getHeight());
        hBlur = new HorizontalBlur(WindowManager.getWidth() / 4, WindowManager.getHeight() / 4);
        vBlur = new VerticalBlur(WindowManager.getWidth() / 4, WindowManager.getHeight() / 4);
//        hBlur2 = new HorizontalBlur(WindowManager.getWidth() / 8, WindowManager.getHeight() / 8);
//        vBlur2 = new VerticalBlur(WindowManager.getWidth() / 8, WindowManager.getHeight() / 8);
    }

    /**
     * Do all post processing related things.
     * Render all post processing effects one after another.
     * @param colorTexture the current scene (fbo) as texture
     */
    public static void doPostProcessing(int colorTexture){
        start();
        resolutionChanger.render(colorTexture);
        hBlur.render(resolutionChanger.getOutputTexture());
        vBlur.render(hBlur.getOutputTexture());
        end();
    }

    /**
     * Changes the {@link ResolutionChanger} so the texture it renders to has a new resolution.
     * @param width Width of the new resolution in pixels.
     * @param height Height of the new resolution in pixels.
     */
    public static void changeResolution(int width, int height){
        resolutionChanger.changeResolution(width, height);
        hBlur.changeResolution(width, height);
    }

    /**
     * Cleanup on program exit.
     */
    public static void cleanUp(){
        resolutionChanger.cleanUp();
        hBlur.cleanUp();
        vBlur.cleanUp();
//        hBlur2.cleanUp();
//        vBlur2.cleanUp();
    }

    /**
     * Prepare for rendering.
     * Binds the quad and enable its position vbo.
     * Also disable depth test because scene is rendered as one 2D image.
     */
    private static void start(){
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Unbind vao and vbo after rendering and enable depth test.
     */
    private static void end(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
}
