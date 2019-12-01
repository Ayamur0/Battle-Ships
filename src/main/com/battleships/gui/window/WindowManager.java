package com.battleships.gui.window;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.main.SchiffeVersenken;
import com.battleships.gui.postProcessing.Fbo;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.gui.water.WaterFrameBuffers;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.nio.IntBuffer;

public class WindowManager {

    private static long window;
    private static int width = 1280, height = 720; //get Width + Height from Settings
    private static final int StandardWIDTH = 1280, StandardHEIGHT = 720; //To reset Width + Height to standard
    private static boolean fullScreen;
    private static boolean changeFullScreen;

    private static double lastFrame;
    private static double deltaTime;



    public static void initialize(){
        //initialize GLFW
        GLFWErrorCallback.createPrint(System.err).set();
        GLFW.glfwInit();

        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);

        //create centered window
        window = GLFW.glfwCreateWindow(width, height, "Schiffe Versenken", 0, 0);
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
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
    }

    public static void updateWindow(){
        GLFW.glfwSwapBuffers(window);
        //allow mouse and keyboard inputs
        GLFW.glfwPollEvents();

        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);

        GLFW.glfwGetWindowSize(window, widthBuffer, heightBuffer);
        width = widthBuffer.get(0);
        height = heightBuffer.get(0);
        GL11.glViewport(0, 0, width, height);

        double currentFrame = GLFW.glfwGetTime();
        deltaTime = currentFrame - lastFrame;
        lastFrame = currentFrame;
    }

    public static float getDeltaTime() {
        return (float)deltaTime;
    }

    public static long getWindow() {
        return window;
    }

    static public void setCallbacks(Camera camera, GuiClickCallback guiClickCallback, WaterFrameBuffers wFbo){
        GLFW.glfwSetMouseButtonCallback(window, guiClickCallback.guiClick);
        //TODO set ingame clickcallback so that if on no gui, it gets checked if on playingfield
        GLFW.glfwSetScrollCallback(window, camera.scrollCallback);
        GLFW.glfwSetKeyCallback(window, camera.keyCallback);
        GLFW.glfwSetWindowSizeCallback(window, wFbo.sizeCallback);
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static boolean isFullscreen() {
        return fullScreen;
    }

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
