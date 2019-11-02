package com.battleships.gui.window;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.guis.GuiClickCallback;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class WindowManager {

    private static long window;
    private static int WIDTH = 1280, HEIGHT = 720; //get Width + Height from Settings
    private final int StandardWIDTH = 800, StandardHEIGHT = 600; //To reset Width + Height to standard

    private static double lastFrame;
    private static double deltaTime;



    public static void initialize(){
        //initialize GLFW
        GLFWErrorCallback.createPrint(System.err).set();
        GLFW.glfwInit();

        //create centered window
        window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, "Schiffe Versenken", 0, 0);
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window, vidMode.width() / 2 - WIDTH / 2, vidMode.height() / 2 - HEIGHT / 2);
        GLFW.glfwMakeContextCurrent(window);

        //Set FPS to max Monitor frequency
        GLFW.glfwSwapInterval(1);

        //initialize OpenGL
        GL.createCapabilities();
        //set color of empty window to white
        GL11.glClearColor(0.5f,0.5f,0.5f, 1);
        GLFW.glfwSetInputMode(window, GLFW.GLFW_STICKY_MOUSE_BUTTONS, GLFW.GLFW_TRUE);
        lastFrame = GLFW.glfwGetTime();
    }

    public static void updateWindow(){
        GLFW.glfwSwapBuffers(window);
        //allow mouse and keyboard inputs
        GLFW.glfwPollEvents();


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

    static public void setCallbacks(Camera camera, GuiClickCallback guiClickCallback){
        GLFW.glfwSetMouseButtonCallback(window, guiClickCallback.guiClick);
        GLFW.glfwSetScrollCallback(window, camera.scrollCallback);
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }
}
