package com.battleships.gui.entities;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.PlayingField;
import com.battleships.gui.terrains.Terrain;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.nio.DoubleBuffer;

public class Camera {

    private static final float WALK_SPEED = 20;
    private static final int RUN_MULTIPLIER = 5;
    private static final float maxY = 315;
    private static final float minY = -3;

    private float zoom = 0;


    private Vector3f position = new Vector3f(0,4,0);
    private float pitch, yaw, roll;
    private float currentForwardSpeed;
    private float currentSidewaysSpeed;
    private float currentUpwardsSpeed;

    private boolean turned;
    private float originX;
    private float originZ;
    private float radius;

    private boolean mouseLocked = false;

    public GLFWScrollCallback scrollCallback = new GLFWScrollCallback(){
        @Override
        public void invoke(long window, double xOffset, double yOffset) {
            zoom = (float)yOffset;
        }
    };

        public void move(long window, Terrain terrain){ //TODO limit zoom and pitch amount, keep movement in game area
        calculatePitch(window);
        calculatePosition(window);
        pitch %= 360;
        yaw %= 360;
        float deltaTime = WindowManager.getDeltaTime();

        //calculate moved distance from speed and the time that has elapsed since last frame
        float forwardDistance = currentForwardSpeed * deltaTime;
        float sidewaysDistance = currentSidewaysSpeed * deltaTime;
        float upwardsDistance= currentUpwardsSpeed * deltaTime;

        //all calculations use trigonometry

        //calculate new positions depending on distance moved forward
        position.x += (float) (forwardDistance * Math.sin(Math.toRadians(yaw)));
        position.z -= (float) (forwardDistance * Math.cos(Math.toRadians(yaw)));

        //calculate new positions depending on distance moved sideways
        position.x += (float) (sidewaysDistance * Math.sin(Math.toRadians(yaw + 90)));
        position.z -= (float) (sidewaysDistance * Math.cos(Math.toRadians(yaw + 90)));

        //calculate new positions depending on distance moved upwards
        position.y += upwardsDistance;

        //calculate new positions depending on zoom
        if(position.y - 2* zoom * Math.abs(Math.sin(Math.toRadians(pitch))) > minY && position.y - 2* zoom * Math.abs(Math.sin(Math.toRadians(pitch))) < maxY) {
            position.x += (float) (2 * zoom * Math.sin(Math.toRadians(yaw)) * Math.abs(Math.cos(Math.toRadians(pitch))));
            position.z -= (float) (2 * zoom * Math.cos(Math.toRadians(yaw)) * Math.abs(Math.cos(Math.toRadians(pitch))));
            position.y -= 2 * zoom * Math.abs(Math.sin(Math.toRadians(pitch)));
        }

        currentForwardSpeed = 0;
        currentSidewaysSpeed = 0;
        currentUpwardsSpeed = 0;
        zoom = 0;

        position.x = originX + (float)Math.cos(Math.toRadians(yaw + 90))*radius;
        position.z = originZ + (float)Math.sin(Math.toRadians(yaw + 90))*radius;

        //don't allow to get under terrain
        float terrainHeight = terrain.getHeightOfTerrain(position.x, position.z);
        if(position.y < terrainHeight + 1){
            position.y = terrainHeight + 1;
        }
    }

    //set speeds in the directions based in inputs
    private void calculatePosition(long window){
        int run = 1;
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS){
            run = RUN_MULTIPLIER;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS){
            currentForwardSpeed = WALK_SPEED * run;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS){
            currentForwardSpeed = -WALK_SPEED * run;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS){
            currentSidewaysSpeed = WALK_SPEED * run;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS){
            currentSidewaysSpeed = -WALK_SPEED * run;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS){
            currentUpwardsSpeed = WALK_SPEED;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS){
            currentUpwardsSpeed = -WALK_SPEED;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_Q) == GLFW.GLFW_PRESS){
            yaw -= 0.6f;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_E) == GLFW.GLFW_PRESS){
            yaw += 0.6f;
        }
    }

    private void calculatePitch(long window){
        //hide cursor while holding right mouse button
        //center mouse and keep mouse in window
        //mouse moves camera while holding right mouse button
        if(GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS && !mouseLocked) {
            GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
            GLFW.glfwSetCursorPos(window, WindowManager.getWidth() / 2f, WindowManager.getHeight() / 2f);
            mouseLocked = true;
        }
        if(mouseLocked){
            DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
            GLFW.glfwGetCursorPos(window, x, y);
            x.rewind();
            y.rewind();

            double newX = x.get();
            double newY = y.get();

            double deltaX = newX - WindowManager.getWidth() / 2f;
            double deltaY = newY - WindowManager.getHeight() / 2f;


            pitch += deltaY * 0.1f;
            //prevent camera looping around x axis
            if(pitch > 90)
                pitch = 90;
            if(pitch < -90)
                pitch = -90;

            yaw += deltaX * 0.1f;

            GLFW.glfwSetCursorPos(window, WindowManager.getWidth() / 2f, WindowManager.getHeight() / 2f);
        }
        //enable cursor on release of right mouse button and disable mouse moving camera
        if(GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_RELEASE) {
            GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
            mouseLocked = false;
        }
    }

    public void setStandardPos(){
            radius = Math.abs(350 * ((float)GameManager.getPlayingField().getSize() + 1) / PlayingField.getMAXSIZE() - Math.abs(GameManager.getPlayingField().getOwn().getPosition().z));
            originZ = GameManager.getPlayingField().getOwn().getPosition().z;
            originX = position.x = (GameManager.getPlayingField().getOwn().getPosition().x + GameManager.getPlayingField().getOpponent().getPosition().x) / 2f;
            position.y = 255f * ((float)GameManager.getPlayingField().getSize() + 1) / PlayingField.getMAXSIZE();
            position.z = -350 + 0.5f * -350f * (1 - ((float)GameManager.getPlayingField().getSize() + 1) / PlayingField.getMAXSIZE());
            pitch = 70;
            yaw = 0;
    }

    public void turnCamera(){
            if(350 < yaw || yaw < 10)
                yaw = 180;
            else
                yaw = 0;
    }

    public void invertPitch(){
        this.pitch = -pitch;
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
