package com.battleships.gui.guis;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;

/**
 * All Gui elements in the game are instances of this class.
 * Contains all information needed to create a 2D GUI.
 *
 * @author Tim Staudenmaier
 */

public class GuiTexture {

    /**
     * ID of the texture this gui uses.
     */
    private int texture;
    /**
     * If texture us a texture atlas indicates the number of rows/columns that
     * texture atlas contains. (1 if texture is no texture atlas)
     */
    private int rows = 1;
    /**
     * Offset of the texture in the textureAtlas that is used (0 if no texture Atlas is used).
     */
    private float offsetX = 0, offsetY = 0;
    /**
     * Position the middle of this gui is at (screen coordinates).
     */
    private Vector2f positions;
    /**
     * Scale of this gui (screen coordinates)
     */
    private Vector2f scale;

    /**
     * Create a new gui with a texture at the specified position and the specified scale.
     * @param texture - The texture the gui should use.
     * @param positions - Position of the center of the gui in screen coordinates (0,0) top left and (1,1) bottom right.
     * @param scale - Scale for this gui as x and y values for horizontal and vertical scale.
     *              Scale is percentage of the screen the gui should occupy, 1 for screen width/height.
     */
    public GuiTexture(int texture, Vector2f positions, Vector2f scale) {
        this.texture = texture;
        this.positions = positions;
        this.scale = scale;
    }

    /**
     * Create a new gui with a texture at the specified position. The scale will be set
     * to the scale of the texture image.
     * @param texture - The texture the gui should use.
     * @param positions - Position of the center of the gui in screen coordinates (0,0) top left and (1,1) bottom right.
     */
    public GuiTexture(int texture, Vector2f positions) {
        this.texture = texture;
        this.positions = positions;

        setScaleToTextureAspect();
    }

    /**
     * @return - TextureID of this gui.
     */
    public int getTexture() {
        return texture;
    }

    /**
     * @return - Positions of the center of this gui
     */
    public Vector2f getPositions() {
        return positions;
    }

    /**
     * @return - Scale of this gui as x and y values for horizontal and vertical scale in terms of screen space occupied (1 = screen width)
     */
    public Vector2f getScale() {
        return scale;
    }

    /**
     * Used for easier scaling of gui elements.
     * Sets the scale of the gui so it has the same sizen, compared to the game window,
     * like the full texture would have on the monitor.
     * Texture has width of 960 Pixels and monitor is 1920 x 1080 -> 50% of monitor so scale is 0.5.
     */
    private void setScaleToTextureAspect(){
        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);
        GL11.glGetTexLevelParameteriv(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH, x);
        GL11.glGetTexLevelParameteriv(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT, y);
        int width = x.get();
        int height = y.get();

        scale = new Vector2f();
//        scale.x = width / (float)GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()).width();
//        scale.y = height / (float)GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()).height();
        scale.x = (float)width / 1920;
        scale.y = (float)height / 1080;
    }

    /**
     * @return - NumberOfRows this texture uses (1 if this isn't a texture atlas).
     */
    public int getRows() {
        return rows;
    }

    /**
     * Set amount of rows this texture uses (default 1, for no texture atlas).
     * @param rows - numberOfRows this texture has.
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * @return - The offset of the column in the textureAtlas for the texture this GUITexture uses.
     */
    public float getOffsetX() {
        return offsetX / rows;
    }

    /**
     * Sets the column of the texture in the Texture Atlas this GUITexture should use (default 0, for no texture atlas).
     * @param offsetX - Column of texture to be used.
     */
    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    /**
     * @return - The offset of the row in the textureAtlas for the texture this GUITexture uses.
     */
    public float getOffsetY() {
        return offsetY / rows;
    }
    /**
     * Sets the row of the texture in the Texture Atlas this GUITexture should use (default 0, for no texture atlas).
     * @param offsetY - Row of texture to be used.
     */
    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }
}
