package com.battleships.gui.engine.entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {



    private long window;
    private Vector3f position = new Vector3f(0,4,0);
    private float pitch, yaw, roll;

    public Camera (long window){
        this.window = window;
    }

    public void move(long window){ //TODO make movement relative to Camera
        int sprint = 1;
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS){
            sprint = 6;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS){
            position.z -= 0.2f * sprint;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS){
            position.z += 0.2f * sprint;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS){
            position.x += 0.2f * sprint;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS){
            position.x -= 0.2f * sprint;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS){
            position.y += 0.2f;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS){
            position.y -= 0.2f;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_Q) == GLFW.GLFW_PRESS){
            yaw -= 0.6f;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_E) == GLFW.GLFW_PRESS){
            yaw += 0.6f;
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
