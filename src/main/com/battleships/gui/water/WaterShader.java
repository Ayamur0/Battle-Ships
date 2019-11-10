package com.battleships.gui.water;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.entities.Light;
import com.battleships.gui.shaders.ShaderProgram;
import com.battleships.gui.toolbox.Maths;
import org.joml.Matrix4f;

public class WaterShader extends ShaderProgram {

    private final static String VERTEX_FILE = "/com/battleships/gui/water/waterVertexShader.glsl";
    private final static String FRAGMENT_FILE = "/com/battleships/gui/water/waterFragmentShader.glsl";

    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
    private int location_reflectionTexture;
    private int location_refractionTexture;
    private int location_dudvMap;
    private int location_moveFactor;
    private int location_cameraPosition;
    private int location_normalMap;
    private int location_lightColor;
    private int location_lightPosition;

    public WaterShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_modelMatrix = getUniformLocation("modelMatrix");
        location_reflectionTexture = getUniformLocation("reflectionTexture");
        location_refractionTexture = getUniformLocation("refractionTexture");
        location_dudvMap = getUniformLocation("dudvMap");
        location_moveFactor = getUniformLocation("moveFactor");
        location_cameraPosition = getUniformLocation("cameraPosition");
        location_normalMap = getUniformLocation("normalMap");
        location_lightColor = getUniformLocation("lightColor");
        location_lightPosition = getUniformLocation("lightPosition");
    }

    /**
     * Load the color and position of the "sun" the light that lights the whole scene
     * to the shader, so the water reflection can use it.
     * @param sun - the main light of the scene
     */
    public void loadLight(Light sun){
        super.loadVector(location_lightColor, sun.getColor());
        super.loadVector(location_lightPosition, sun.getPosition());
    }

    /**
     * Load the moveFactor to the shader.
     * The moveFactor moves the distortion texture around, so it looks like
     * the waves of the water are moving.
     * @param factor - moveFactor to load
     */
    public void loadMoveFactor(float factor){
        super.loadFloat(location_moveFactor, factor);
    }

    /**
     * Set sampler 2D for reflection texture to use texture bank 0
     * and the one for refraction texture to use texture bank 1.
     * The du/dv Map for the water movement is in texture bank 2
     * and the normal map for the water in 3.
     */
    public void connectTextureUnits(){
        super.loadInt(location_reflectionTexture, 0);
        super.loadInt(location_refractionTexture, 1);
        super.loadInt(location_dudvMap, 2);
        super.loadInt(location_normalMap, 3);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        loadMatrix(location_projectionMatrix, projection);
    }

    /**
     * Load viewMatrix to shader.
     * Also load camera position to shader, to determine if camera looks at water from above or from the side.
     * If looked at from the side water is more reflective.
     * @param camera - the current camera that displays the scene
     */
    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        loadMatrix(location_viewMatrix, viewMatrix);
        super.loadVector(location_cameraPosition, camera.getPosition());
    }

    public void loadModelMatrix(Matrix4f modelMatrix){
        loadMatrix(location_modelMatrix, modelMatrix);
    }
}
