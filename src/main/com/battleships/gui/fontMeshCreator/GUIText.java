package com.battleships.gui.fontMeshCreator;

import com.battleships.gui.fontRendering.TextMaster;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * 2D text that can be rendered to the screen.
 * All GuiTexts need to be passed to a {@link com.battleships.gui.fontRendering.FontRenderer} to be visible on screen.
 * Before using GUITexts {@link TextMaster} needs to be initialized!
 */
public class GUIText {

    /**
     * Actual text
     */
    private String textString;
    /**
     * Font size of this text
     */
    private float fontSize;

    /**
     * ID of the vao containing all the quads this text is rendered on.
     */
    private int textMeshVao;
    /**
     * Count of vertices the vao of this text contains.
     */
    private int vertexCount;

    /**
     * Color of the text
     */
    private Vector3f color;

    /**
     * Center position of the text in screen coordinates
     */
    private Vector2f position;
    /**
     * Max size of one line in screen space
     */
    private float lineMaxSize;
    /**
     * Max amount of lines
     */
    private int numberOfLines;

    /**
     * Font for this text
     */
    private FontType font;

    /**
     * {@code true} if the text should be centered, {@code false} for the text to be on the left.
     */
    private boolean centerText;
    /**
     * Width of the outline around the characters.
     * 0 if characters have no outline.
     */
    private float outlineWidth;
    /**
     * Size of the edge transitioning from character to whatever is behind it,
     * so the transition doesn't look to sharp.
     * Needs to be above 0, recommended value is 0.1
     */
    private float outlineEdge;
    /**
     * Color of the outline around the characters.
     * As r, g, b values (all between 0 and 1)
     */
    private Vector3f outlineColor;
    /**
     * Offset of the outline from the characters.
     * Can be used to create shadow effects.
     */
    private Vector2f outlineOffset;

    /**
     * Creates a new text, loads the text's quads into a VAO, and adds the text
     * to the screen
     *
     * @param text          the string this text should read
     * @param fontSize      the font size of the text, 1 is default size
     * @param font          the font to use for this text
     * @param position      the position on the screen where the top left corner of the
     *                      text should be rendered. (top left corner (0,0) bottom right (1,1))
     * @param maxLineLength max length of one line, relative to screen is (1 is full screen width).
     *                      If a word in a line exceeds this limit the word is put into a new line
     * @param centered      whether the text should be centered or not
     * @param color         Color of the Text in RGB (0-1)
     * @param outlineWidth  width of the outline of the text, 0 for no outline
     * @param outlineEdge   size of the smooth transition edge around the outline
     * @param outlineColor  color of the outline
     * @param outlineOffset offset of the outline to create a shadow effect
     */
    public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
                   boolean centered, Vector3f color, float outlineWidth, float outlineEdge, Vector3f outlineColor, Vector2f outlineOffset) {
        this.textString = text;
        this.fontSize = fontSize;
        this.font = font;
        this.position = position;
        position.x -= maxLineLength / 2f;
        position.y -= TextMeshCreator.getLineHeight();
        this.lineMaxSize = maxLineLength;
        this.centerText = centered;
        this.color = color;
        this.outlineWidth = outlineWidth;
        this.outlineEdge = outlineEdge;
        this.outlineColor = outlineColor;
        this.outlineOffset = outlineOffset;
        TextMaster.loadText(this);
    }

    /**
     * Remove this text from the screen
     */
    public void remove() {
        TextMaster.removeText(this);
    }

    /**
     * @return The font used by this text
     */
    public FontType getFont() {
        return font;
    }

    /**
     * Set the color of the text as r, g, b values.
     * All values need to be between 0 and 1.
     *
     * @param r red value
     * @param g green value
     * @param b blue value
     */
    public void setColor(float r, float g, float b) {
        color.set(r, g, b);
    }

    /**
     * @return the color of the text
     */
    public Vector3f getColor() {
        return color;
    }

    /**
     * @return The number of lines of text after it has been loaded. Changes corresponding to maxLineLength
     */
    public int getNumberOfLines() {
        return numberOfLines;
    }

    /**
     * Sets the number of lines that this text covers (method used only in
     * loading)
     *
     * @param number number of lines
     */
    protected void setNumberOfLines(int number) {
        this.numberOfLines = number;
    }

    /**
     * @return The position of the top-left corner of the text on the screen
     */
    public Vector2f getPosition() {
        return position;
    }

    /**
     * @return the ID of the text's VAO, containing all the quads the text will be rendered on
     */
    public int getMesh() {
        return textMeshVao;
    }

    /**
     * Set the VAO and vertex count for this text
     *
     * @param vao           the VAO containing all the vertex data for the quads the
     *                      text will be rendered on
     * @param verticesCount total number of vertices in all of the quads
     */
    public void setMeshInfo(int vao, int verticesCount) {
        this.textMeshVao = vao;
        this.vertexCount = verticesCount;
    }

    /**
     * @return The total number of vertices in all of the texts quads
     */
    public int getVertexCount() {
        return this.vertexCount;
    }

    /**
     * @return the font size of the text (1 is standard)
     */
    protected float getFontSize() {
        return fontSize;
    }

    /**
     * @return {@code true} if the text should be centered
     */
    protected boolean isCentered() {
        return centerText;
    }

    /**
     * @return The maximum length of a line of this text
     */
    protected float getMaxLineSize() {
        return lineMaxSize;
    }

    /**
     * @return The string of text
     */
    protected String getTextString() {
        return textString;
    }

    /**
     * Change the text, after changing the text, this GUIText needs to be
     * removed from the TextMaster and then be loaded again to use the new text.
     *
     * @param textString new text string
     */
    public void setTextString(String textString) {
        this.textString = textString;
    }

    /**
     * @return The width of the outline
     */
    public float getOutlineWidth() {
        return outlineWidth;
    }

    /**
     * @return The size of the edge around the outline
     */
    public float getOutlineEdge() {
        return outlineEdge;
    }

    /**
     * @return The color of the outline as rgb values between 0 and 1
     */
    public Vector3f getOutlineColor() {
        return outlineColor;
    }

    /**
     * @return The offset of the outline for this text
     */
    public Vector2f getOutlineOffset() {
        return outlineOffset;
    }

    /**
     * @return Max length of a line in this text.
     */
    public float getLineMaxSize() {
        return lineMaxSize;
    }
}
