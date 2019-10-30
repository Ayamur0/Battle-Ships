package com.battleships.gui.Engine.Rendering.Entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {



    private long window;
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch, yaw, roll;

    public Camera (long window){
        this.window = window;
    }

    public void move(long window){
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS){
            position.z -= 0.02f;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS){
            position.z += 0.02f;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS){
            position.x += 0.02f;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS){
            position.x -= 0.02f;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
