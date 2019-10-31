package com.battleships.gui.engine.shaders;

import com.battleships.gui.engine.entities.Camera;
import com.battleships.gui.engine.entities.Light;
import com.battleships.gui.engine.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TerrainShader extends ShaderProgram {

    private static final Vector3f UP_VECTOR = new Vector3f(0, 1, 0); //Vector looking straight up

    private static final String VERTEX_FILE = "/com/battleships/gui/engine/shaders/TerrainVertexShader.glsl";
    private static final String FRAGMENT_FILE = "/com/battleships/gui/engine/shaders/TerrainFragmentShader.glsl";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_shineDamper;
    private int location_reflectivity;

    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColor = super.getUniformLocation("lightColor");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
    }

    public void loadShineVariables(float damper, float reflectivity){
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    //load light Position and Color vectors into shader uniforms
    public void loadLight(Light light){
        super.loadVector(location_lightPosition, light.getPosition());
        super.loadVector(location_lightColor, light.getColor());
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
