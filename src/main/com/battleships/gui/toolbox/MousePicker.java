package com.battleships.gui.toolbox;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.gui.terrains.Terrain;
import com.battleships.gui.window.WindowManager;
import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;

/**
 * Mouse picker that translates the position of the mouse cursor on the screen to a position inside the 3D world.
 *
 * @author Tim Staudenmaier
 */
public class MousePicker {
    private static final int RECURSION_COUNT = 200;
    private static final float RAY_RANGE = 600;

    /**
     * Current vector that describes the line in the world, pointed by the mouse cursor.
     */
    private Vector3f currentRay = new Vector3f();

    /**
     * Current projectionMatrix of the window.
     */
    private Matrix4f projectionMatrix;
    /**
     * Current viewMatrix of the camera.
     */
    private Matrix4f viewMatrix;
    /**
     * Camera that views the scene.
     */
    private Camera camera;

    /**
     * Point at which the ray the mouse creates intersects with the y-level,
     * at which the water and grids are located.
     */
    private Vector3f currentIntersectionPoint;

    /**
     * Create a new MousePicker
     *
     * @param cam        Camera viewing the scene.
     * @param projection Current projectionMatrix of the window.
     */
    public MousePicker(Camera cam, Matrix4f projection) {
        camera = cam;
        projectionMatrix = projection;
        viewMatrix = Maths.createViewMatrix(camera);
    }


    /**
     * @return Point at which the ray the mouse creates intersects with the y-level,
     * at which the water and grids are located.
     */
    public Vector3f getCurrentIntersectionPoint() {
        return currentIntersectionPoint;
    }

    /**
     * Update the mouse ray and calculate new intersection point to match the current
     * view- and projectionMatrix as well as the current cursor position.
     */
    public void update() {
        projectionMatrix = MasterRenderer.getProjectionMatrix();
        viewMatrix = Maths.createViewMatrix(camera);
//        System.out.println(camera.getPosition().x + " " + camera.getPosition().y + " " + camera.getPosition().z);
        currentRay = calculateMouseRay();
//        System.out.println(currentRay.x + " " + currentRay.y + " " + currentRay.z);
        currentIntersectionPoint = getXZPlaneIntersectionPoint(camera.getPosition(), currentRay, -2.5f);

//        if(currentTerrainPoint != null)
//            System.out.println(currentTerrainPoint.x + " " + currentTerrainPoint.y + " " + currentTerrainPoint.z);
    }

    /**
     * Calculates the current ray the mouse is pointing along in the 3D world.
     *
     * @return The current vector of the mouse ray
     */
    private Vector3f calculateMouseRay() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(WindowManager.getWindow(), x, y);
        x.rewind();
        y.rewind();

        float mouseX = (float) x.get();
        float mouseY = (float) y.get();
//
        Vector2f normalizedCoords = getNormalizedDeviceCoordinates(mouseX, mouseY);
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        Vector3f worldRay = toWorldCoords(eyeCoords);
//        Vector3f worldRay = test(projectionMatrix, viewMatrix, mouseX, mouseY);
//        worldRay.sub(camera.getPosition());
        worldRay.normalize();
        return worldRay;
    }

    /**
     * Convert coordinates from eyeSpaceCoordinates to worldCoordinates.
     *
     * @param eyeCoords coordinates in eyeSpace
     * @return Corresponding coordinates in worldCoordinates.
     */
    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedView = new Matrix4f();
        viewMatrix.invert(invertedView);
        Vector4f rayWorld = new Vector4f();
        invertedView.transform(eyeCoords, rayWorld);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalize();
//        System.out.println(mouseRay.x + " " + mouseRay.y + " " + mouseRay.z);
        return mouseRay;

    }

    /**
     * Convert coordinates from clipCoordinates to eyeCoordinates.
     *
     * @param clipCoords Coordinates as clipCoordinates.
     * @return Corresponding coordinates in eyeCoordinates.
     */
    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjection = new Matrix4f();
        projectionMatrix.invert(invertedProjection);
        Vector4f eyeCoords = new Vector4f();
        invertedProjection.transform(clipCoords, eyeCoords);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    /**
     * Convert a mouse position to normalizedDeviceCoordinates.
     *
     * @param mouseX x coordinate of the mouse position.
     * @param mouseY y coordinate of the mouse position.
     * @return The corresponding normalizedDeviceCoordinates.
     */
    private Vector2f getNormalizedDeviceCoordinates(float mouseX, float mouseY) {
        float x = (2.0f * mouseX) / WindowManager.getWidth() - 1f;
        float y = (2.0f * mouseY) / WindowManager.getHeight() - 1f;
        return new Vector2f(x, -y);
    }

    /**
     * Calculates the coordinates at which the mouse ray intersects with the xz plane of the water.
     * @param origin Origin point of the mouse ray.
     * @param ray Vector of the mouse ray.
     * @param height Height of the XZ Plane
     * @return The point at which the mouse ray and XZ Plane intersect (world coordinates)
     */
    private Vector3f getXZPlaneIntersectionPoint(Vector3f origin, Vector3f ray, float height) {
        float parameter = (float) Intersectiond.intersectRayPlane(origin.x, origin.y, origin.z, ray.x, ray.y, ray.z, 0, height, 0, 0, 1, 0, 0);
        Vector3f result = new Vector3f();
        origin.add(ray.mul(parameter), result);
        return result;
    }
}
