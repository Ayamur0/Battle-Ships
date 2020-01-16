package com.battleships.gui.water;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.window.WindowManager;
import org.lwjgl.glfw.GLFWWindowIconifyCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import java.nio.ByteBuffer;

/**
 * Frame buffers for water reflection and refraction.
 * Contains the textures for reflection and refraction and the capability to create these textures.
 *
 * @author Tim Staudenmaier
 */
public class WaterFrameBuffers {

    /**
     * Width of the reflection texture in pixels.
     */
    private static int REFLECTION_WIDTH = 320;
    /**
     * Height of the reflection texture in pixels.
     */
    private static int REFLECTION_HEIGHT = 180;

    /**
     * Width of the refraction texture in pixels.
     */
    private static int REFRACTION_WIDTH = 1280;
    /**
     * Height of the refraction texture in pixels.
     */
    private static int REFRACTION_HEIGHT = 720;

    /**
     * ID of the frame buffer the reflection texture is rendered to.
     */
    private int reflectionFrameBuffer;
    /**
     * ID of the texture of the reflection texture.
     */
    private int reflectionTexture;
    /**
     * ID of the depth buffer of the reflection.
     */
    private int reflectionDepthBuffer;

    /**
     * ID of the frame buffer the refraction texture is rendered to.
     */
    private int refractionFrameBuffer;
    /**
     * ID of the texture of the refraction texture.
     */
    private int refractionTexture;
    /**
     * ID of the depth buffer of the refraction.
     */
    private int refractionDepthTexture;

    /**
     * {@code true} if the window was recently maximized (after being put into taskbar).
     * Gets set back to {@code false} after processed.
     */
    private boolean iconify;

    /**
     * Initialize water fbos reflection and refraction.
     */
    public WaterFrameBuffers() {//call when loading the game
        initializeReflectionFrameBuffer();
        initializeRefractionFrameBuffer();
    }

    /**
     * Updates the sizes of the frame buffers to match the current window size.
     */
    public void updateFrameBuffers(){
        boolean lowSpecMode = GameManager.getSettings().isLowSpecMode();
        int reflectionFactor = lowSpecMode ? 8 : 4;
        int refractionFactor = lowSpecMode ? 4 : 1;
        if(GameManager.getSettings().getResHeight() == -1 || GameManager.getSettings().getResWidth() == -1) {
            REFLECTION_WIDTH = WindowManager.getWidth() / reflectionFactor;
            REFLECTION_HEIGHT = WindowManager.getHeight() / reflectionFactor;
            REFRACTION_WIDTH = WindowManager.getWidth() / refractionFactor;
            REFRACTION_HEIGHT = WindowManager.getHeight() / refractionFactor;
        }
        else {
            REFLECTION_WIDTH = GameManager.getSettings().getResWidth() / reflectionFactor;
            REFLECTION_HEIGHT = GameManager.getSettings().getResHeight() / reflectionFactor;
            REFRACTION_WIDTH = GameManager.getSettings().getResWidth() / refractionFactor;
            REFRACTION_HEIGHT = GameManager.getSettings().getResHeight() / refractionFactor;
        }
        initializeReflectionFrameBuffer();
        initializeRefractionFrameBuffer();
    }

    /**
     * Cleanup call on program exit.
     */
    public void cleanUp() {//call when closing the game
        GL30.glDeleteFramebuffers(reflectionFrameBuffer);
        GL11.glDeleteTextures(reflectionTexture);
        GL30.glDeleteRenderbuffers(reflectionDepthBuffer);
        GL30.glDeleteFramebuffers(refractionFrameBuffer);
        GL11.glDeleteTextures(refractionTexture);
        GL11.glDeleteTextures(refractionDepthTexture);
    }

    /**
     * Bind reflection frame buffer setting it as the current render target. Anything
     * rendered after this will be rendered to this FBO, and not to the screen.
     */
    public void bindReflectionFrameBuffer() {//call before rendering to this FBO
        bindFrameBuffer(reflectionFrameBuffer,REFLECTION_WIDTH,REFLECTION_HEIGHT);
    }

    /**
     * Bind refraction frame buffer setting it as the current render target. Anything
     * rendered after this will be rendered to this FBO, and not to the screen.
     */
    public void bindRefractionFrameBuffer() {//call before rendering to this FBO
        bindFrameBuffer(refractionFrameBuffer,REFRACTION_WIDTH,REFRACTION_HEIGHT);
    }

    /**
     * Unbinds the frame buffer, setting the default frame buffer as the current
     * render target. Anything rendered after this will be rendered to the
     * screen, and not this FBO.
     */
    public void unbindCurrentFrameBuffer() {//call to switch to default frame buffer
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, WindowManager.getWidth(), WindowManager.getHeight());
    }

    /**
     * @return Texture of water after reflection has been applied.
     */
    public int getReflectionTexture() {//get the resulting texture
        return reflectionTexture;
    }

    /**
     * @return Texture of water after refraction has been applied.
     */
    public int getRefractionTexture() {//get the resulting texture
        return refractionTexture;
    }

    /**
     * @return Texture of water after refraction depth texture has been applied.
     */
    public int getRefractionDepthTexture(){//get the resulting depth texture
        return refractionDepthTexture;
    }

    /**
     * Creates the Reflection FBO along with a color buffer texture attachment, and
     *  a depth buffer attachment.
     */
    private void initializeReflectionFrameBuffer() {
        reflectionFrameBuffer = createFrameBuffer();
        reflectionTexture = createTextureAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);
        reflectionDepthBuffer = createDepthBufferAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);
        unbindCurrentFrameBuffer();
    }

    /**
     * Creates the Refraction FBO along with a color buffer texture attachment, and
     *  a depth texture attachment.
     */
    private void initializeRefractionFrameBuffer() {
        refractionFrameBuffer = createFrameBuffer();
        refractionTexture = createTextureAttachment(REFRACTION_WIDTH,REFRACTION_HEIGHT);
        refractionDepthTexture = createDepthTextureAttachment(REFRACTION_WIDTH,REFRACTION_HEIGHT);
        unbindCurrentFrameBuffer();
    }

    /**
     * Binds the frame buffer, setting it as the current render target. Anything
     * rendered after this will be rendered to this FBO, and not to the screen.
     *
     * @param frameBuffer - the frameBuffer to bind.
     * @param width - width of the frameBuffer.
     * @param height - height of the frameBuffer.
     */
    private void bindFrameBuffer(int frameBuffer, int width, int height){
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        GL11.glViewport(0, 0, width, height);
    }

    /**
     * Creates a new frame buffer object and sets the buffer to which drawing
     * will occur to color attachment 0 which is where the color buffer texture is.
     */
    private int createFrameBuffer() {
        int frameBuffer = GL30.glGenFramebuffers();
        //generate name for frame buffer
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        //create the framebuffer
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
        //indicate that we will always render to color attachment 0
        return frameBuffer;
    }

    /**
     * Creates a texture and sets it as the color buffer attachment for this
     * FBO.
     * @param width - width of the texture attachment.
     * @param height - height of the texture attachment.
     */
    private int createTextureAttachment( int width, int height) {
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture, 0);
        return texture;
    }

    /**
     * Adds a depth buffer to the FBO in the form of a texture, which can later
     * be sampled.
     * @param width - width of the texture attachment.
     * @param height - height of the texture attachment.
     */
    private int createDepthTextureAttachment(int width, int height){
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, texture, 0);
        return texture;
    }

    /**
     * Adds a depth buffer to the FBO in the form of a render buffer. This can't
     * be used for sampling in the shaders.
     * @param width - width of the buffer attachment.
     * @param height - height of the buffer attachment.
     */
    private int createDepthBufferAttachment(int width, int height) {
        int depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
        return depthBuffer;
    }

    /**
     * Method that gets called every time the window is resized by the user.
     * Needed to adjust resolution of frameBuffers corresponding to resolution of the window.
     */
    public GLFWWindowSizeCallback sizeCallback = new GLFWWindowSizeCallback() {
        @Override
        public void invoke(long window, int width, int height) {
            updateFrameBuffers();
        }
    };

    /**
     * Needed to resize FrameBuffer after tabbing out or into the game so water gets displayed properly.
     */
    public GLFWWindowIconifyCallback iconifyCallback = new GLFWWindowIconifyCallback() {
        @Override
        public void invoke(long l, boolean small) {
            if(small)
                return;
            new Thread(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                iconify = true;
            }).start();
        }
    };

    /**
     * Reset waterFrameBuffer sizes after window has been resized through tabbing out.
     */
    public void processIconify(){
        if(iconify) {
            updateFrameBuffers();
            iconify = false;
        }
    }

}
