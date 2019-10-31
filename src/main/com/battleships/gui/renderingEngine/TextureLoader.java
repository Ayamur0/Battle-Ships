package com.battleships.gui.renderingEngine;

import com.battleships.gui.models.ModelTexture;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class TextureLoader {


    static ModelTexture loadTexture(String filename){

        //create usable TextureLoader for OpenGl from image
        InputStream is = TextureLoader.class.getResourceAsStream("/com/battleships/gui/res/textures/" + filename);
        if (is == null)
            throw new RuntimeException("Resource not found: " + filename);
        ByteBuffer rawBytes;
        try {
            rawBytes = ioResourceToByteBuffer(is, 16384);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer c = BufferUtils.createIntBuffer(1);
        ByteBuffer decodedImage = STBImage.stbi_load_from_memory(rawBytes, w, h, c, 4);
        if(decodedImage == null)
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

    private static ByteBuffer ioResourceToByteBuffer(InputStream source, int bufferSize) throws IOException {
        ByteBuffer buffer = null;

        try (ReadableByteChannel rbc = Channels.newChannel(source)){
            buffer = BufferUtils.createByteBuffer(bufferSize);

            while (true) {
                int bytes = rbc.read(buffer);
                if (bytes == -1)
                    break;
                if(buffer.remaining() == 0)
                    buffer = resizeBuffer(buffer, buffer.capacity() * 2);

            }
        }
        catch (Throwable t){
            throw new NullPointerException("Input Stream not valid!");
        }

        buffer.flip();
        return buffer;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity){
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
