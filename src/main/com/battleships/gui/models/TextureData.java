package com.battleships.gui.models;

import java.nio.ByteBuffer;

/**
 * Class containing all the data read from an image.
 *
 * @author Tim Staudenmaier
 */
public class TextureData {

    /**
     * Width an height of the image.
     */
    private int width;
    private int height;
    /**
     * Actual data of the image.
     */
    private ByteBuffer buffer;

    /**
     * Create a new TextureData for an image.
     * @param buffer - Data of the image.
     * @param width - Width of the image.
     * @param height - Height of the image.
     */
    public TextureData(ByteBuffer buffer, int width, int height) {
        this.width = width;
        this.height = height;
        this.buffer = buffer;
    }

    /**
     * @return - Width of the image from this TextureData.
     */
    public int getWidth() {
        return width;
    }
    /**
     * @return - Height of the image from this TextureData.
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return - Data of the image from this TextureData.
     */
    public ByteBuffer getBuffer() {
        return buffer;
    }
}
