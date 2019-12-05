package com.battleships.gui.fontMeshCreator;

import com.battleships.gui.fontRendering.TextMaster;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GUIText {

    private String textString;
    private float fontSize;

    private int textMeshVao;
    private int vertexCount;
    private Vector3f color = new Vector3f(0f, 0f, 0f);

    private Vector2f position;
    private float lineMaxSize;
    private int numberOfLines;

    private FontType font;

    private boolean centerText = false;
    private float outlineWidth;
    private float outlineEdge;
    private Vector3f outlineColor;
    private Vector2f outlineOffset;

    /**
     * Creates a new text, loads the text's quads into a VAO, and adds the text
     * to the screen
     *
     * @param text
     *            - the text
     * @param fontSize
     *            - the font size of the text, 1 is default size
     * @param font
     *            - the font to use for this text
     * @param position
     *            - the position on the screen where the top left corner of the
     *            text should be rendered. (top left corner (0,0) bottom right (1,1))
     * @param maxLineLength
     *            - max length of one line, relative to screen is (1 is full screen width).
     *            If a word in a line exceeds this limit the word is put into a new line
     * @param centered
     *            - whether the text should be centered or not
     * @param outlineWidth
     *            - width of the outline of the text, 0 for no outline
     * @param outlineEdge
     *            - size of the smooth transition edge around the outline
     * @param outlineColor
     *            - color of the outline
     * @param outlineOffset
     *            - offset of the outline to create a shadow effect
     */
    public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
                   boolean centered, float outlineWidth, float outlineEdge, Vector3f outlineColor, Vector2f outlineOffset) {
        this.textString = text;
        this.fontSize = fontSize;
        this.font = font;
        this.position = position;
        this.lineMaxSize = maxLineLength;
        this.centerText = centered;
        this.outlineWidth = outlineWidth;
        this.outlineEdge = outlineEdge;
        this.outlineColor = outlineColor;
        this.outlineOffset = outlineOffset;
        TextMaster.loadText(this);
    }

    /**
     * Remove the text from the screen
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
     * Set the color of the text
     *
     * @param r
     *            - red value, between 0 and 1
     * @param g
     *            - green value, between 0 and 1
     * @param b
     *            - blue value, between 0 and 1
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
     * @param vao
     *            - the VAO containing all the vertex data for the quads the
     *            text will be rendered on
     * @param verticesCount
     *            - total number of vertices in all of the quads
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
     * Sets the number of lines that this text covers (method used only in
     * loading)
     *
     * @param number - number of lines
     */
    protected void setNumberOfLines(int number) {
        this.numberOfLines = number;
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
     *
     * @return The width of the outline
     */
    public float getOutlineWidth() {
        return outlineWidth;
    }

    /**
     *
     * @return The size of the edge around the outline
     */
    public float getOutlineEdge() {
        return outlineEdge;
    }

    /**
     *
     * @return The color of the outline as rgb values between 0 and 1
     */
    public Vector3f getOutlineColor() {
        return outlineColor;
    }

    /**
     *
     * @return The offset of the outline for this text
     */
    public Vector2f getOutlineOffset() {
        return outlineOffset;
    }

    /**
     *
     * @return Max length of a line in this text.
     */
    public float getLineMaxSize() {
        return lineMaxSize;
    }
}
