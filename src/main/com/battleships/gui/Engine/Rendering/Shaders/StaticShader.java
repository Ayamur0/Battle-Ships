package com.battleships.gui.Engine.Rendering.Shaders;

import com.battleships.gui.Engine.Rendering.Entities.Camera;
import com.battleships.gui.Engine.Toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class StaticShader extends ShaderProgram {

    private static final Vector3f UP_VECTOR = new Vector3f(0, 1, 0); //Vector looking straight up

    private static final String VERTEX_FILE = "/com/battleships/gui/Game/res/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "/com/battleships/gui/Game/res/shaders/fragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    //load matrix needed to process scaling, moving or rotating a model
    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
//        Matrix4f viewMatrix = new Matrix4f();
//        Vector3f position = new Vector3f(0,0,0);
//        Vector3f direction = new Vector3f(0,0,0);
//        Vector3f rotation = new Vector3f(0,0,0);
//        double pitch = Math.toRadians(rotation.x);
//        double yaw = Math.toRadians(rotation.y);
//        //don't convert z rotation, because it's not used as it's close to the same as x rotation
//
//        //convert rotations into direction vectors
//        direction.x = (float) (Math.cos(pitch) * Math.sin(yaw));
//        direction.y = (float) Math.sin(pitch);
//        direction.z = (float) (Math.cos(pitch) * Math.cos(yaw));
//        Vector3f target = new Vector3f(0,0,0);
//        viewMatrix.setLookAt(new Vector3f (0,0,0), position.add(direction, target), UP_VECTOR);
//        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrix(location_projectionMatrix, projection);
    }
}
