package com.battleships.gui.postProcessing;

import com.battleships.gui.shaders.ShaderProgram;

public class ResolutionShader extends ShaderProgram {

    private static final String VERTEX_FILE = "/com/battleships/gui/postProcessing/resolutionVertexShader.glsl";
    private static final String FRAGMENT_FILE = "/com/battleships/gui/postProcessing/resolutionFragmentShader.glsl";

    /**
     * Create this shader program with the two shader files
     */
    public ResolutionShader() {
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
