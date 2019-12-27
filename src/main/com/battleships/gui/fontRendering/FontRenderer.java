package com.battleships.gui.fontRendering;

import com.battleships.gui.fontMeshCreator.FontType;
import com.battleships.gui.fontMeshCreator.GUIText;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

/**
 * Renderer capable of rendering {@link GUIText}s.
 *
 * @author Tim Staudenmaier
 */
public class FontRenderer {

    /**
     * @param shader shader to be used when rendering a text/font
     */
    private FontShader shader;

    /**
     * Creates a new FontRenderer using the {@link FontShader}.
     */

    public FontRenderer() {
        shader = new FontShader();
    }

    /**
     * Iterate over the fonts and the texts using this font, then bind texture for current font
     * and render all texts for that font. Then use the next font.
     *
     * @param texts HashMap containing all fonts and the texts using that font that should be rendered
     */
    public void render(Map<FontType, List<GUIText>> texts){
        prepare();
        for(FontType font : texts.keySet()){
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
            for(GUIText text : texts.get(font)){
                renderText(text);
            }
        }
        endRendering();
    }

    /**
     * clean up the shader on exit of program
     */
    public void cleanUp(){
        shader.cleanUp();
    }

    /**
     * Prepare OpenGl to be able to render text and start shader
     */
    private void prepare(){
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        shader.start();
    }

    /**
     * Bind vao of text to be rendered and activate position and textureCoords vbos.
     * Then load color and position(translation) to the shader and render the text.
     * After rendering disable vbos and unbind vao.
     *
     * @param text text to be rendered
     */
    private void renderText(GUIText text){
        GL30.glBindVertexArray(text.getMesh());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        shader.loadColor(text.getColor());
        shader.loadTranslation(text.getPosition());
        shader.loadBorderWidth(text.getOutlineWidth());
        shader.loadBorderEdge(text.getOutlineEdge());
        shader.loadOutlineColor(text.getOutlineColor());
        shader.loadOffset(text.getOutlineOffset());
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    /**
     * Disable text rendering in OpenGl so standard entities can be rendered again
     */
    private void endRendering(){
        shader.stop();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

}
