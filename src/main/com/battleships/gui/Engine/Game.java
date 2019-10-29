package com.battleships.gui.Engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Game {

    private long window;
    private int WIDTH = 800, HEIGHT = 600; //get Width + Height from Settings
    private final int StandardWIDTH = 800, StandardHEIGHT = 600; //To reset Width + Height to standard

    public void run() {
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

        mainGameLoop(window);

        release();

        //unload GLFW on window close
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    public void mainGameLoop(long window) { }

    public void release(){}
}
