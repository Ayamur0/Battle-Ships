package com.battleships.gui.guis;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;

public class GuiTexture {

    private int texture;
    private Vector2f positions;
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
}
