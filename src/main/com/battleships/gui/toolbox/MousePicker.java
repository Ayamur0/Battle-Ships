package com.battleships.gui.toolbox;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.window.WindowManager;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;

public class MousePicker {

    private Vector3f currentRay;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;

    public MousePicker(Matrix4f projectionMatrix, Camera camera) {
        this.projectionMatrix = projectionMatrix;
        this.camera = camera;
        this.viewMatrix = Maths.createViewMatrix(camera);
    }

    public Vector3f getCurrentRay(){
        return currentRay;
    }

    public void update(){
        viewMatrix = Maths.createViewMatrix(camera);
        currentRay = calculateMouseRay();
    }

    private Vector3f calculateMouseRay() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(WindowManager.getWindow(), x, y);
        x.rewind();
        y.rewind();

        float mouseX = (float)x.get();
        float mouseY = (float)y.get();
        Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        return toWorldCoords(eyeCoords);
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords){
        Matrix4f invertedView = viewMatrix.invert();
        Vector4f rayWorld = invertedView.transform(eyeCoords);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalize();
        return mouseRay;
    }

    private Vector4f toEyeCoords(Vector4f clipCoords){
        Matrix4f invertedProjection = projectionMatrix.invert();
        Vector4f eyeCoords = invertedProjection.transform(clipCoords);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY){
        //convert cursor position from pixel (top left corner = (0,0) bottom right = (Width, Height) to openGL coordinates (top left = (-1,-1) bottom right = (1,-1))
        float x = (2f * mouseX) / WindowManager.getWidth() - 1;
        float y = (2f * mouseY) / WindowManager.getHeight() - 1;
        return new Vector2f(x, -y);
    }


}
