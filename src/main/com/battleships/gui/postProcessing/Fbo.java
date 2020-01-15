package com.battleships.gui.postProcessing;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.window.WindowManager;
import com.battleships.logic.Settings;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

/**
 * FrameBufferObject a scene can be rendered to instead of the screen.
 * Can be used to edit the scene with post processing effects before showing it on the screen.
 * Before using post processing {@link PostProcessing} needs to be initialized!
 * 
 * @author Tim Staudenmaier
 */

public class Fbo {

    /**
     * No DepthBuffer
     */
    public static final int NONE = 0;
    /**
     * Use a DepthTexture. With this texture the rendered image
     * can be edited in the shaders before showing it on screen.
     */
    public static final int DEPTH_TEXTURE = 1;
    /**
     * Use a DepthRenderBuffer. With this buffer the rendered image
     * can't be edited in the shaders.
     */
    public static final int DEPTH_RENDER_BUFFER = 2;

    /**
     * Width of the image this fbo renders to.
     */
    private int width;
    /**
     * Height of the image this fbo renders to.
     */
    private int height;

    /**
     * Type of DepthBuffer this fbo uses.
     */
    private int type;

    /**
     * OpenGL ID of this frameBuffer
     */
    private int frameBuffer;

    /**
     * ID of the color texture of this frame buffer.
     */
    private int colorTexture;
    /**
     * ID of the depth texture of this frame buffer.
     * Only used with a depthTexture. 
     * If this isn't used the ID is 0.
     */
    private int depthTexture;

    /**
     * ID of the depth buffer this frame buffer uses.
     * Only used with a depthRenderBuffer. 
     * If this isn't used the ID is 0.
     */
    private int depthBuffer;
    /**
     * ID of the color buffer this frame buffer uses.
     */
    private int colorBuffer;

    /**
     * Creates an FBO of a specified width and height, with the desired type of
     * depth buffer attachment.
     *
     * @param width the width of the FBO.
     * @param height the height of the FBO.
     * @param depthBufferType an int indicating the type of depth buffer attachment that this FBO should use.
     */
    public Fbo(int width, int height, int depthBufferType) {
        this.width = width;
        this.height = height;
        this.type = depthBufferType;
        initializeFrameBuffer(depthBufferType);
    }

    /**
     * Update size of this fbo to match current window size.
     */
    public void updateSize(){
        Settings settings = GameManager.getSettings();
        if(settings.getResWidth() != -1 && settings.getResHeight() != -1){
            if(this.height != settings.getResHeight() || this.width != settings.getResWidth()){
                cleanUp();
                this.height = settings.getResHeight();
                this.width = settings.getResWidth();
                initializeFrameBuffer(type);
                return;
            }
            return;
        }
        if(this.height == WindowManager.getHeight() && this.width == WindowManager.getWidth())
            return;
        cleanUp();
        this.width = WindowManager.getWidth();
        this.height = WindowManager.getHeight();
        initializeFrameBuffer(type);
    }

    /**
     * Deletes the frame buffer and its attachments when the game closes.
     */
    public void cleanUp() {
        GL30.glDeleteFramebuffers(frameBuffer);
        GL11.glDeleteTextures(colorTexture);
        GL11.glDeleteTextures(depthTexture);
        GL30.glDeleteRenderbuffers(depthBuffer);
        GL30.glDeleteRenderbuffers(colorBuffer);
    }

    /**
     * Binds the frame buffer, setting it as the current render target. Anything
     * rendered after this will be rendered to this FBO, and not to the screen.
     */
    public void bindFrameBuffer() {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBuffer);
        GL11.glViewport(0, 0, width, height);
    }

    /**
     * Unbinds the frame buffer, setting the default frame buffer as the current
     * render target. Anything rendered after this will be rendered to the
     * screen, and not this FBO.
     */
    public void unbindFrameBuffer() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, WindowManager.getWidth(), WindowManager.getHeight());
    }

    /**
     * Binds the current FBO to be read from.
     */
    public void bindToRead() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, frameBuffer);
        GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
    }

    /**
     * @return The ID of the texture containing the color buffer of the FBO.
     */
    public int getColorTexture() {
        return colorTexture;
    }

    /**
     * @return The texture containing the FBOs depth buffer.
     */
    public int getDepthTexture() {
        return depthTexture;
    }

    /**
     * Creates the FBO along with a color buffer texture attachment, and
     * possibly a depth buffer.
     *
     * @param type the type of depth buffer attachment to be attached to the FBO (0-2).
     */
    private void initializeFrameBuffer(int type) {
        createFrameBuffer();
        createTextureAttachment();
        if (type == DEPTH_RENDER_BUFFER) {
            createDepthBufferAttachment();
        } else if (type == DEPTH_TEXTURE) {
            createDepthTextureAttachment();
        }
        unbindFrameBuffer();
    }

    /**
     * Creates a new frame buffer object and sets the buffer to which drawing
     * will occur to color attachment 0 which is where the color buffer texture is.
     */
    private void createFrameBuffer() {
        frameBuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
    }

    /**
     * Creates a texture and sets it as the color buffer attachment for this
     * FBO.
     */
    private void createTextureAttachment() {
        colorTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorTexture, 0);
    }

    /**
     * Adds a depth buffer to the FBO in the form of a texture, which can later
     * be sampled.
     */
    private void createDepthTextureAttachment() {
        depthTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture, 0);
    }

    /**
     * Adds a depth buffer to the FBO in the form of a render buffer. This can't
     * be used for sampling in the shaders.
     */
    private void createDepthBufferAttachment() {
        depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
    }
}
