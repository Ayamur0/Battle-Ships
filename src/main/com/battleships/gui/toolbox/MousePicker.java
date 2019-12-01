package com.battleships.gui.toolbox;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.gui.terrains.Terrain;
import com.battleships.gui.window.WindowManager;
import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;

public class MousePicker {
    private static final int RECURSION_COUNT = 200;
    private static final float RAY_RANGE = 600;

    private Vector3f currentRay = new Vector3f();

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;

    private Terrain terrain;
    private Vector3f currentTerrainPoint;
    private Vector3f currentIntersectionPoint;

    public MousePicker(Camera cam, Matrix4f projection, Terrain terrain) {
        camera = cam;
        projectionMatrix = projection;
        viewMatrix = Maths.createViewMatrix(camera);
        this.terrain = terrain;
    }

    public Vector3f getCurrentTerrainPoint() {
        return currentTerrainPoint;
    }

    public Vector3f getCurrentIntersectionPoint() {
        return currentIntersectionPoint;
    }

    public Vector3f getCurrentRay() {
        return currentRay;

    }

    public void update() {
        projectionMatrix = MasterRenderer.getProjectionMatrix();
        viewMatrix = Maths.createViewMatrix(camera);
//        System.out.println(camera.getPosition().x + " " + camera.getPosition().y + " " + camera.getPosition().z);
        currentRay = calculateMouseRay();
//        System.out.println(currentRay.x + " " + currentRay.y + " " + currentRay.z);
        if (intersectionInRange(0, RAY_RANGE, currentRay)) {
            currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
        } else {
            currentTerrainPoint = null;
        }
        currentIntersectionPoint = getXZPlaneIntersectionPoint(camera.getPosition(), currentRay, -2.5f);

//        if(currentTerrainPoint != null)
//            System.out.println(currentTerrainPoint.x + " " + currentTerrainPoint.y + " " + currentTerrainPoint.z);
    }

    private Vector3f calculateMouseRay() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(WindowManager.getWindow(), x, y);
        x.rewind();
        y.rewind();

        float mouseX = (float)x.get();
        float mouseY = (float)y.get();
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

    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjection = new Matrix4f();
        projectionMatrix.invert(invertedProjection);
        Vector4f eyeCoords = new Vector4f();
        invertedProjection.transform(clipCoords, eyeCoords);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    private Vector2f getNormalizedDeviceCoordinates(float mouseX, float mouseY) {
        float x = (2.0f * mouseX) / WindowManager.getWidth() - 1f;
        float y = (2.0f * mouseY) / WindowManager.getHeight() - 1f;
        return new Vector2f(x, -y);
    }

    private Vector3f test(Matrix4f proj, Matrix4f view, float x, float y){
        Vector3f worldCoords = new Vector3f();
        new Matrix4f(proj).mul(view).translate(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z).unproject(x, y, -1.0f,
                new int[] { 0, 0, WindowManager.getWidth(), WindowManager.getHeight() }, worldCoords);
        return worldCoords;
    }

    //**********************************************************

    private Vector3f getXZPlaneIntersectionPoint(Vector3f origin, Vector3f ray, float height){
        float parameter = (float) Intersectiond.intersectRayPlane(origin.x, origin.y, origin.z, ray.x, ray.y, ray.z, 0, height, 0, 0, 1, 0, 0);
        Vector3f result = new Vector3f();
        origin.add(ray.mul(parameter), result);
        return result;
    }


    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        Vector3f camPos = camera.getPosition();
        Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
        start.add(scaledRay, start);
        return start;
    }

    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
        float half = start + ((finish - start) / 2f);
        if (count >= RECURSION_COUNT) {
            Vector3f endPoint = getPointOnRay(ray, half);
            Terrain terrain = getTerrain(endPoint.x, endPoint.z);
            if (terrain != null) {
                return endPoint;
            } else {
                return null;
            }
        }
        if (intersectionInRange(start, half, ray)) {
            return binarySearch(count + 1, start, half, ray);
        } else {
            return binarySearch(count + 1, half, finish, ray);
        }
    }

    private boolean intersectionInRange(float start, float finish, Vector3f ray) {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);
        if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isUnderGround(Vector3f testPoint) {
        Terrain terrain = getTerrain(testPoint.x, testPoint.z);
        float height = 0;
        if (terrain != null) {
            height = terrain.getHeightOfTerrain(testPoint.x, testPoint.z);
        }
        if (testPoint.y < height) {
            return true;
        } else {
            return false;
        }
    }

    private Terrain getTerrain(float worldX, float worldZ) {
        return terrain;
    }
}
