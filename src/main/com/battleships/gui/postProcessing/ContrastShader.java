package com.battleships.gui.postProcessing;

import com.battleships.gui.shaders.ShaderProgram;

public class ContrastShader extends ShaderProgram {

    private static final String VERTEX_FILE = "/com/battleships/gui/postProcessing/contrastVertexShader.glsl";
    private static final String FRAGMENT_FILE = "/com/battleships/gui/postProcessing/contrastFragmentShader.glsl";

    /**
     * Create this shader program with the two shader files
     */
    public ContrastShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
    }

    /**
     * Bind the position attribute to attribute 0, so vertex shader can use it as in variable.
     */
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
