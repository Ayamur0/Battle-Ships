package com.battleships.gui.toolbox;

import com.battleships.gui.entities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Contains some OpenGL related math functions.
 *
 * @author Tim Staudenmaier
 */
public class Maths {

    /**
     * Barycentric Interpolation can calculate the height of a triangle
     * at a specific point on the triangle.
     *
     * @param p1  coordinates of the first edge of the triangle (world coordinates)
     * @param p2  coordinates of the second edge of the triangle (world coordinates)
     * @param p3  coordinates of the third edge of the triangle (world coordinates)
     * @param pos position for which the height of the triangle should be calculated (only x and z coordinates, world coordinates)
     * @return The height (world y value) of the triangle at passed coordinates pos.
     */
    public static float baryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    /**
     * Creates a OpenGL Transformation matrix containing the position and scale of an object.
     *
     * @param translation Position of the object this transformation matrix should be calculated for.
     * @param scale       Scale of the object this transformation matrix should be calculated for.
     * @return A matrix containing the position and scale of the object, corresponds to the transformation matrix of the object.
     */
    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(new Vector3f(translation.x, translation.y, 1.0f), matrix);
        matrix.scale(new Vector3f(scale.x, scale.y, 1f), matrix);
        return matrix;
    }

    /**
     * Creates a OpenGL Transformation matrix containing the position, rotation and scale of an object.
     *
     * @param translation Position of the object this transformation matrix should be calculated for.
     * @param rotation    Rotation of the object this transformation matrix should be calculated for.
     * @param scale       Scale of the object this transformation matrix should be calculated for.
     * @return A matrix containing the position, rotation and scale of the object, corresponds to the transformation matrix of the object.
     */
    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity(); //reset matrix
        matrix.translate(translation);
        matrix.scale(scale);
        matrix.rotateXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y), (float) Math.toRadians(rotation.z));
        return matrix;
    }

    /**
     * Create a OpenGL viewMatrix for the passed camera.
     * Contains the position and rotation of the camera.
     *
     * @param camera Camera the viewMatrix should be calculated for.
     * @return A matrix that needs to be used as viewMatrix so the rendered scene looks like it is viewed through
     * the passed camera.
     */
    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotateX((float) Math.toRadians(camera.getPitch()));
        viewMatrix.rotateY((float) Math.toRadians(camera.getYaw()));
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        viewMatrix.translate(negativeCameraPos, viewMatrix);
        return viewMatrix;
    }
}
