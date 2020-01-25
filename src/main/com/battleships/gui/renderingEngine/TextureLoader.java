package com.battleships.gui.renderingEngine;

import com.battleships.gui.models.ModelTexture;
import com.battleships.gui.models.TextureData;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Loader for converting images to textures formatted for OpenGL.
 *
 * @author Tim Staudenmaier
 */
public class TextureLoader {

    /**
     * Load a texture from an image (.jpg or .tga images).
     *
     * @param filename Path to the image for the texture.
     * @return A ModelTexture containing the loaded image converted into a texture.
     */
    static ModelTexture loadTexture(String filename) {

        //create usable TextureLoader for OpenGl from image
        InputStream is = TextureLoader.class.getResourceAsStream("/com/battleships/gui/res/textures/" + filename);
        if (is == null)
            throw new RuntimeException("Resource not found: " + filename);
        ByteBuffer rawBytes;
        try {
            rawBytes = ioResourceToByteBuffer(is);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer c = BufferUtils.createIntBuffer(1);
        ByteBuffer decodedImage = STBImage.stbi_load_from_memory(rawBytes, w, h, c, 4);
        if (decodedImage == null)
            throw new RuntimeException("Image file '" + filename + "' could not be loaded: " + STBImage.stbi_failure_reason());

        //add texture to OpenGL
        int width = w.get();
        int height = h.get();
        int id = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, decodedImage);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        STBImage.stbi_image_free(decodedImage);
        return new ModelTexture(id);
    }

    /**
     * Load the data from an image needed to create a texture from it.
     *
     * @param fileName Path to the image the data should be loaded from.
     * @return A TextureData containing all the data needed from the image.
     */
    public static TextureData loadTextureData(String fileName) {

        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            InputStream in = TextureLoader.class.getResourceAsStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ", didn't work");
            System.exit(-1);
        }
        return new TextureData(buffer, width, height);
    }

    /**
     * Load a texture from an image (.png images).
     *
     * @param fileName Path to the image for the texture.
     * @return A ModelTexture containing the loaded image converted into a texture.
     */
    public static ModelTexture loadPNGTexture(String fileName) {
        TextureData data = loadTextureData("/com/battleships/gui/res/textures/" + fileName);
        int id = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());

        return new ModelTexture(id);
    }

    /**
     * Loads all the data an InputStream contains into a ByteBuffer.
     *
     * @param source InputStream containing the data.
     * @return A ByteBuffer containing all the bytes from the InputStream.
     * @throws NullPointerException If the passed InputStream doesn't exist.
     */
    private static ByteBuffer ioResourceToByteBuffer(InputStream source) throws NullPointerException {
        ByteBuffer buffer;

        try (ReadableByteChannel rbc = Channels.newChannel(source)) {
            buffer = BufferUtils.createByteBuffer(16384);

            while (true) {
                int bytes = rbc.read(buffer);
                if (bytes == -1)
                    break;
                if (buffer.remaining() == 0)
                    buffer = resizeBuffer(buffer, buffer.capacity() * 2);

            }
        } catch (Throwable t) {
            throw new NullPointerException("Input Stream not valid!");
        }

        buffer.flip();
        return buffer;
    }

    /**
     * Increases the size of a byteBuffer if the buffer was to small for the data
     * that should be written to it.
     *
     * @param buffer      The buffer whose size should be increased.
     * @param newCapacity The new size the buffer should have.
     * @return A buffer containing all the data from the buffer that was passed with a new size.
     */
    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
