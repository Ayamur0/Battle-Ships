package com.battleships.gui.particles;

import com.battleships.gui.shaders.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class ParticleShader extends ShaderProgram {

    private static final String VERTEX_FILE = "/com/battleships/gui/particles/particleVertexShader.glsl";
    private static final String FRAGMENT_FILE = "/com/battleships/gui/particles/particleFragmentShader.glsl";

    private int location_modelViewMatrix;
    private int location_projectionMatrix;
    private int location_texOffset1;
    private int location_texOffset2;
    private int location_texCoordInfo;


    public ParticleShader(){
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Loads all locations (as ints) for all uniform variables in the shader code
     * into location_ attributes, to make them accessible
     */

    @Override
    protected void getAllUniformLocations() {
        location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_texOffset1 = super.getUniformLocation("texOffset1");
        location_texOffset2 = super.getUniformLocation("texOffset2");
        location_texCoordInfo = super.getUniformLocation("texCoordInfo");
    }

    /**
     * Binds the attribute position to attribute 0 for this shader.
     * Shader code can then access the attribute
     */
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    /**
     * Load info for how to use the textureAtlas for the current particle to the shader.
     * @param offset1 - offset in textureAtlas for the current texture
     * @param offset2 - offset in textureAtlas for the next texture (needed to blend)
     * @param numRows - number of rows in the textureAtlas
     * @param blend - how much to blend in the next texture into the current one
     */
    protected void loadTextureCoordInfo(Vector2f offset1, Vector2f offset2, float numRows, float blend){
        super.load2DVector(location_texOffset1, offset1);
        super.load2DVector(location_texOffset2, offset2);
        super.load2DVector(location_texCoordInfo, new Vector2f(numRows, blend));
    }

    /**
     * Load a matrix as viewMatrix for the shader.
     * @param modelView - Matrix to be loaded, is used to convert world space to eye space
     */

    protected void loadModelViewMatrix(Matrix4f modelView){
        super.loadMatrix(location_modelViewMatrix, modelView);
    }

    /**
     * Load a matrix as projectionMatrix for the shader.
     * @param projectionMatrix - Matrix to be loaded, is used to convert eye space to viewport space (2D screen)
     */

    protected void loadProjectionMatrix(Matrix4f projectionMatrix){
        super.loadMatrix(location_projectionMatrix, projectionMatrix);
    }
}
//private int location_modelViewMatrix;
//    private int location_projectionMatrix;
//
//    public ParticleShader() {
//        super(VERTEX_FILE, FRAGMENT_FILE);
//    }
//
//    @Override
//    protected void getAllUniformLocations() {
//        location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
//        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
//    }
//
//    @Override
//    protected void bindAttributes() {
//        super.bindAttribute(0, "position");
//    }
//
//    protected void loadModelViewMatrix(Matrix4f modelView) {
//        super.loadMatrix(location_modelViewMatrix, modelView);
//    }
//
//    protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
//        super.loadMatrix(location_projectionMatrix, projectionMatrix);
//    }
//
//}