package com.battleships.gui.guis;

import com.battleships.gui.models.RawModel;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

/**
 * Renderer for rendering 2D Gui Elements to the screen.
 *
 * @author Tim Staudenmaier
 */
public class GuiRenderer {

    /**
     * Rectangle that {@link GuiTexture} can be rendered on.
     */
    private final RawModel quad;
    /**
     * Shader that this rendered uses.
     */
    private GuiShader shader;

    /**
     * Create a new GuiRenderer.
     * @param loader - Loader to load rectangles on which {@link GuiTexture} gets rendered on.
     */
    public GuiRenderer (Loader loader){
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
        quad = loader.loadToVAO(positions, 2);
        shader = new GuiShader();
    }

    /**
     * Renders the {@link GuiTexture} to the screen using the {@link GuiShader}.
     * @param guis - List containing all guiTextures that should be rendered.
     */
    public void render(List<GuiTexture> guis){
        shader.start();
        //bind vao and enable position attribute
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        //render gui
        for(GuiTexture gui : guis){
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
            Matrix4f matrix = Maths.createTransformationMatrix(new Vector2f(gui.getPositions().x * 2.0f - 1, (-1.0f * (gui.getPositions().y  * 2.0f - 1))), gui.getScale());
            shader.loadTransformation(matrix);
            shader.loadNumberOfRows(gui.getRows());
            shader.loadOffset(gui.getOffsetX(), gui.getOffsetY());
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        //disable position attribute and unbind vao
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    /**
     * Cleans up all gui renderer stuff.
     * Needs to be called on programm exit.
     */
    public void cleanUp(){
        shader.cleanUp();
    }
}
