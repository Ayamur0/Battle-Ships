package com.battleships.gui.skybox;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.shaders.ShaderProgram;
import com.battleships.gui.toolbox.Maths;
import org.joml.Matrix4f;

public class SkyboxShader extends ShaderProgram {

    private static final String VERTEX_FILE = "/com/battleships/gui/skybox/skyboxVertexShader.glsl";
    private static final String FRAGMENT_FILE = "/com/battleships/gui/skybox/skyboxFragmentShader.glsl";

    private int location_projectionMatrix;
    private int location_viewMatrix;

    //load shader files
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        //reset translation of view matrix, so skybox moves with camera
        matrix._m30(0);
        matrix._m31(0);
        matrix._m32(0);
        super.loadMatrix(location_viewMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}
