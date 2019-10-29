package com.battleships.gui.Engine.Rendering.Entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {

    private static final Vector3f UP_VECTOR = new Vector3f(0, 1, 0); //Vector looking straight up

    private long window;
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch, yaw, roll;

    public Camera (long window){
        this.window = window;
    }

    public void move(){
        if(GLFW.glfwGetKey(W)){
            position.z -= 0.02f;
        }
        if(GLFW.glfwGetKey(S)){
            position.z += 0.02f;
        }
        if(GLFW.glfwGetKey(D)){
            position.x += 0.02f;
        }
        if(GLFW.glfwGetKey(A)){
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
