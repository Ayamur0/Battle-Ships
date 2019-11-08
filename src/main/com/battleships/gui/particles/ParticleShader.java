package com.battleships.gui.particles;

import com.battleships.gui.shaders.ShaderProgram;
import org.joml.Matrix4f;

public class ParticleShader extends ShaderProgram {

    private static final String VERTEX_FILE = "/com/battleships/gui/particles/particleVertexShader.glsl";
    private static final String FRAGMENT_FILE = "/com/battleships/gui/particles/particleFragmentShader.glsl";

    private int location_modelViewMatrix;
    private int location_projectionMatrix;


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