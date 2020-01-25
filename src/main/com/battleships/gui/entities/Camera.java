package com.battleships.gui.entities;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.gui.terrains.Terrain;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWScrollCallback;

/**
 * The scene always gets rendered, as if it was filmed with the camera.
 * To render a scene a camera is mandatory.
 *
 * @author Tim Staudenmaier
 */
public class Camera {

    /**
     * How fast the camera moves if the player is walking.
     */
    private static final float WALK_SPEED = 20;
    /**
     * Factor by that the speed is multiplied if the player is running.
     */
    private static final int RUN_MULTIPLIER = 5;
    /**
     * Maximum y position (in world coordinates) the camera is able to reach in this game.
     * The camera can't go above this y-coordinate.
     */
    private static final float maxY = 315;
    /**
     * Minimum y position (in world coordinates) the camera is able to reach in this game.
     * The camera can't go below this y-coordinate.
     */
    private static final float minY = -3;

    /**
     * Zoom that was input by the user using the scroll wheel.
     * This value always gets used in the next frame to determine how far the camera has to move.
     * After the new position has been calculated this value will be set to 0 again.
     */
    private float zoom = 0;
    /**
     * Scroll Callback of GLFW that is needed to get notified when the user scroll.
     * This callback also tells how far the scroll wheel was scrolled with the yOffset parameter.
     * The yOffset is negative if the scroll wheel was scrolled downwards, else it's positive.
     */
    public GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
        @Override
        public void invoke(long window, double xOffset, double yOffset) {
            zoom = (float) yOffset;
        }
    };
    /**
     * Position of the camera in the world.
     */
    private Vector3f position = new Vector3f(0, 4, 0);
    /**
     * Pitch, yaw and roll of the camera.
     */
    private float pitch, yaw, roll;
    /**
     * Current ForwardSpeed of the camera.
     * Used to calculate new position of the camera, determined by currentSpeed
     * and time passed.
     */
    private float currentForwardSpeed;
    /**
     * Current SidewaysSpeed of the camera.
     * Used to calculate new position of the camera, determined by currentSpeed
     * and time passed.
     */
    private float currentSidewaysSpeed;
    /**
     * Current UpwardsSpeed of the camera.
     * Used to calculate new position of the camera, determined by currentSpeed
     * and time passed.
     */
    private float currentUpwardsSpeed;
    /**
     * In this game the camera is fixed on one circle.
     * originX is the x coordinate of the center of this circle.
     */
    private float originX;
    /**
     * In this game the camera is fixed on one circle.
     * originZ is the z coordinate of the center of this circle.
     */
    private float originZ;
    /**
     * Determines the radius of the circle the camera can move on.
     */
    private float radius;
    //TODO remove
    private boolean mouseLocked = false;

    /**
     * Move the camera to the new position for the current frame, using all the data needed to calculate that position.
     * (currentSpeed adjusted zoom, new rotation)
     *
     * @param terrain Terrain the camera is moving on (needed, so camera can't move under terrain).
     */
    public void move(Terrain terrain) {
        //calculatePitch();
        calculatePosition();
        pitch %= 360;
        yaw %= 360;

        //calculate new positions depending on zoom
        if (position.y - 2 * zoom * Math.abs(Math.sin(Math.toRadians(pitch))) > minY && position.y - 2 * zoom * Math.abs(Math.sin(Math.toRadians(pitch))) < maxY) {
            position.x += (float) (2 * zoom * Math.sin(Math.toRadians(yaw)) * Math.abs(Math.cos(Math.toRadians(pitch))));
            position.z -= (float) (2 * zoom * Math.cos(Math.toRadians(yaw)) * Math.abs(Math.cos(Math.toRadians(pitch))));
            position.y -= 2 * zoom * Math.abs(Math.sin(Math.toRadians(pitch)));
        }

        zoom = 0;

        position.x = originX + (float) Math.cos(Math.toRadians(yaw + 90)) * radius;
        position.z = originZ + (float) Math.sin(Math.toRadians(yaw + 90)) * radius;


        //don't allow to get under terrain
        float terrainHeight = terrain.getHeightOfTerrain(position.x, position.z);
        if (position.y < terrainHeight + 1) {
            position.y = terrainHeight + 1;
        }
    }

    /**
     * Sets all speeds needed to calculate the new position of the camera by reading the inputs
     * the user is currently making.
     */
    //set speeds in the directions based in inputs
    private void calculatePosition() {
        if (GLFW.glfwGetKey(WindowManager.getWindow(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            zoom += 0.5f;
        }
        if (GLFW.glfwGetKey(WindowManager.getWindow(), GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            zoom -= 0.5f;
        }
        if (GLFW.glfwGetKey(WindowManager.getWindow(), GLFW.GLFW_KEY_Q) == GLFW.GLFW_PRESS) {
            yaw -= 0.6f;
        }
        if (GLFW.glfwGetKey(WindowManager.getWindow(), GLFW.GLFW_KEY_E) == GLFW.GLFW_PRESS) {
            yaw += 0.6f;
        }
    }

//    /**
//     * Calculates the new Rotation (pitch and yaw) of the by reading the inputs
//     * the user is currently making.
//     */
//    private void calculatePitch(){
//        //hide cursor while holding right mouse button
//        //center mouse and keep mouse in window
//        //mouse moves camera while holding right mouse button
//        if(GLFW.glfwGetMouseButton(WindowManager.getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS && !mouseLocked) {
//            GLFW.glfwSetInputMode(WindowManager.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
//            GLFW.glfwSetCursorPos(WindowManager.getWindow(), WindowManager.getWidth() / 2f, WindowManager.getHeight() / 2f);
//            mouseLocked = true;
//        }
//        if(mouseLocked){
//            DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
//            DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
//            GLFW.glfwGetCursorPos(WindowManager.getWindow(), x, y);
//            x.rewind();
//            y.rewind();
//
//            double newX = x.get();
//            double newY = y.get();
//
//            double deltaX = newX - WindowManager.getWidth() / 2f;
//            double deltaY = newY - WindowManager.getHeight() / 2f;
//
//
//            pitch += deltaY * 0.1f;
//            //prevent camera looping around x axis
//            if(pitch > 90)
//                pitch = 90;
//            if(pitch < -90)
//                pitch = -90;
//
//            yaw += deltaX * 0.1f;
//
//            GLFW.glfwSetCursorPos(WindowManager.getWindow(), WindowManager.getWidth() / 2f, WindowManager.getHeight() / 2f);
//        }
//        //enable cursor on release of right mouse button and disable mouse moving camera
//        if(GLFW.glfwGetMouseButton(WindowManager.getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_RELEASE) {
//            GLFW.glfwSetInputMode(WindowManager.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
//            mouseLocked = false;
//        }
//    }

    /**
     * Resets the camera to the standard position where it shows both grids of the players.
     */
    public void setStandardPos() {
        GridManager gridManager = GameManager.getGridManager();
        radius = Math.abs(350 * ((float) gridManager.getSize() + 1) / GridManager.getMAXSIZE() * 0.4f);
        originZ = gridManager.getOwnGrid().getPosition().z;
        originX = position.x = (gridManager.getOwnGrid().getPosition().x + gridManager.getOpponentGrid().getPosition().x) / 2f;
        position.y = 255f * ((float) gridManager.getSize() + 1) / GridManager.getMAXSIZE();
        position.z = -350 + 0.5f * -350f * (1 - ((float) gridManager.getSize() + 1) / GridManager.getMAXSIZE());
        pitch = 70;
        yaw = 0;
    }

    /**
     * Turn the camera to be on the opposite side of the grids if camera was close to standard position.
     * Resets the camera to standard position if it wasn't close to that position.
     */
    public void turnCamera() {
        if (350 < yaw || yaw < 10)
            yaw = 180;
        else
            yaw = 0;
    }

    /**
     * Invert the pitch rotation of the camera.
     */
    public void invertPitch() {
        this.pitch = -pitch;
    }

    /**
     * @return Current position of the camera in world coordinates.
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * @return Current pitch of the camera.
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * @return Current yaw of the camera.
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Increases the yaw of this camera.
     *
     * @param degrees By how many degrees the yaw should be increased.
     */
    public void addYaw(float degrees) {
        yaw += degrees;
    }

    /**
     * @return Current roll of the camera.
     */
    public float getRoll() {
        return roll;
    }

}
