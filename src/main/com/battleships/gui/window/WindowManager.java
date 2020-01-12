package com.battleships.gui.window;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.MainMenuGui.MainMenuManager;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiRenderer;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.main.SchiffeVersenken;
import com.battleships.gui.models.TextureData;
import com.battleships.gui.postProcessing.Fbo;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.gui.renderingEngine.TextureLoader;
import com.battleships.gui.water.WaterFrameBuffers;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.windows.User32;

import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * First class that is needed after starting the program.
 * Handles all window related things.
 *
 * @author Tim Staudenmaier
 */
public class WindowManager {

    /**
     * OpenGl ID of this window
     */
    private static long window;
    /**
     * Current Width and Height of this window in pixels.
     */
    private static int width = 1280, height = 720; //get Width + Height from Settings
    /**
     * Standard width and height for this window in pixels.
     */
    private static final int StandardWIDTH = 1280, StandardHEIGHT = 720; //To reset Width + Height to standard
    /**
     * {@code true} if the window is currently in fullScreen mode.
     */
    private static boolean fullScreen;

    /**
     * Time at which the last frame had finished rendering.
     */
    private static double lastFrame;
    /**
     * Time that has passed since the last frame had finished rendering in seconds.
     */
    private static double deltaTime;

    /**
     * Window ID of the loading screen window.
     */
    private static long loadingScreen;

    /**
     * Information about the monitor the game is on.
     */
    private static GLFWVidMode vidMode;


    /**
     * Initialize the main window the game is played in.
     * Needs to be called at start of the program!
     */
    public static void initialize(){
        //initialize GLFW
        GLFWErrorCallback.createPrint(System.err).set();
        GLFW.glfwInit();

        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);

        //create centered window
        //GLFW.glfwWindowHint(GLFW.GLFW_OPACITY, 23);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        window = GLFW.glfwCreateWindow(width, height, "Schiffe Versenken", 0, 0);
        vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window, vidMode.width() / 2 - width / 2, vidMode.height() / 2 - height / 2);
        GLFW.glfwMakeContextCurrent(window);


        //Set FPS to max Monitor frequency
        GLFW.glfwSwapInterval(1);

        //initialize OpenGL
        GL.createCapabilities();
        //set color of empty window to white
        GL11.glClearColor(0.5f,0.5f,0.5f, 1);
        GLFW.glfwSetInputMode(window, GLFW.GLFW_STICKY_MOUSE_BUTTONS, GLFW.GLFW_TRUE);
        GL13.glEnable(GL13.GL_MULTISAMPLE);
        lastFrame = GLFW.glfwGetTime();
        setIcon();
    }

    /**
     * Creates a separate Window acting as loading screen.
     * The loading texture is rendered to this window once.
     * Needs to be destroyed after loading is done.
     */
    public static void createLoadingScreen(){
        GLFW.glfwWindowHint(GLFW.GLFW_TRANSPARENT_FRAMEBUFFER, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);

        loadingScreen = GLFW.glfwCreateWindow(width, height, "loading", 0, 0);
        GLFW.glfwSetWindowPos(loadingScreen, vidMode.width() / 2 - width / 2, vidMode.height() / 2 - height / 2);
        GLFW.glfwMakeContextCurrent(loadingScreen);
        setIcon();

        List<GuiTexture> guis = new ArrayList<>();
        Loader loader = new Loader();
        GuiRenderer guiRenderer = new GuiRenderer(loader);
        guis.add(new GuiTexture(loader.loadTexture("StartIcon.png"), new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f)));

        GLFW.glfwShowWindow(loadingScreen);
        guiRenderer.render(guis);
        GLFW.glfwSwapBuffers(loadingScreen);

        GLFW.glfwMakeContextCurrent(window);
    }

    /**
     * Destroys the loading screen window and shows the game window.
     */
    public static void destroyLoadingScreen(){
        GLFW.glfwDestroyWindow(loadingScreen);
        GLFW.glfwShowWindow(window);
    }

    /**
     * Set the icon of the window.
     */
    private static void setIcon(){
        TextureData icon = TextureLoader.loadTextureData("/com/battleships/gui/res/textures/Icon2.png");
        GLFWImage image = GLFWImage.malloc();
        GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
        image.set(icon.getWidth(), icon.getHeight(), icon.getBuffer());
        imagebf.put(0, image);
        GLFW.glfwSetWindowIcon(window, imagebf);
    }

    /**
     * Update the window to show the next frame.
     * Needs to be called as last method every frame!
     */
    public static void updateWindow(){
        GLFW.glfwSwapBuffers(window);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        //allow mouse and keyboard inputs
        GLFW.glfwPollEvents();

        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);

        GLFW.glfwGetWindowSize(window, widthBuffer, heightBuffer);
        width = widthBuffer.get(0);
        height = heightBuffer.get(0);

        //TODO lock 4k
        GL11.glViewport(0, 0, width, height);

        double currentFrame = GLFW.glfwGetTime();
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;
    }

    /**
     * @return Time that has passed since last frame finished rendering in seconds.
     */
    public static float getDeltaTime() {
        return (float)deltaTime;
    }

    /**
     * @return ID of this window.
     */
    public static long getWindow() {
        return window;
    }

    /**
     * Sets the Callbacks for user inputs.
     * Sets callbacks for mouse, keyboard and window resizing.
     * @param camera Camera that views the scene and should be moved by user input.
     * @param wFbo WaterFBOs that handle the reflection and refraction of water.
     */
    public static void setCallbacks(Camera camera, WaterFrameBuffers wFbo){
        GLFW.glfwSetMouseButtonCallback(window, GameManager.testClick);
        GLFW.glfwSetScrollCallback(window, camera.scrollCallback);
        GLFW.glfwSetKeyCallback(window, GameManager.keyCallback);
        GLFW.glfwSetWindowSizeCallback(window, wFbo.sizeCallback);
        GLFW.glfwSetWindowIconifyCallback(window, wFbo.iconifyCallback);
    }

    /**
     * Sets the Callbacks for user inputs in the MainMenu.
     * @param mainMenuManager Manager that handles clicks in the main menu.
     */
    public static void setMainMenuCallbacks(MainMenuManager mainMenuManager, WaterFrameBuffers wFbo){
        GLFW.glfwSetMouseButtonCallback(window, mainMenuManager.testClick);
        GLFW.glfwSetWindowSizeCallback(window, wFbo.sizeCallback);
        GLFW.glfwSetKeyCallback(window,MainMenuManager.keyCallback);
    }

    /**
     * st
     */
    public static void clearCallbacks(){
        GLFW.glfwSetMouseButtonCallback(window, null);
        GLFW.glfwSetScrollCallback(window, null);
        GLFW.glfwSetKeyCallback(window, null);
        GLFW.glfwSetWindowSizeCallback(window, null);
    }

    /**
     * @return Width of the window in pixels.
     */
    public static int getWidth() {
        return width;
    }

    /**
     * @return Height of the window in pixels.
     */
    public static int getHeight() {
        return height;
    }

    /**
     * @return {@code true} if the window is in fullscreen.
     */
    public static boolean isFullscreen() {
        return fullScreen;
    }

    /**
     * Set the window to be fullscreen or to exit fullscreen.
     * @param fullS {@code true} if the window should enter fullscreen, {@code false} if the window should exit fullscreen.
     */
    public static void setFullScreen (boolean fullS) {
        if(fullS){
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
//            width = vidMode.width();
//            height = vidMode.height();
            GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, vidMode.width(), vidMode.height(), vidMode.refreshRate());
            fullScreen = true;
        }
        if(!fullS){
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            width = StandardWIDTH;
            height = StandardHEIGHT;
            GLFW.glfwSetWindowMonitor(window, 0, vidMode.width() / 2 - width / 2, vidMode.height() / 2 - height / 2,  width, height, vidMode.refreshRate());
            fullScreen = false;
        }
    }
}
