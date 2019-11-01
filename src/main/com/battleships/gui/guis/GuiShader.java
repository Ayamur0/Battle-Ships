package com.battleships.gui.guis;

import com.battleships.gui.shaders.ShaderProgram;
import org.joml.Matrix4f;

public class GuiShader extends ShaderProgram {

    private static final String VERTEX_FILE = "/com/battleships/gui/guis/guiVertexShader.glsl";
    private static final String FRAGMENT_FILE = "/com/battleships/gui/guis/guiFragmentShader.glsl";

    private int location_transformationMatrix;

    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
