package com.battleships.gui.postProcessing.gaussianBlur;

import com.battleships.gui.shaders.ShaderProgram;

/**
 * Shader for the {@link VerticalBlur} post processing effect.
 *
 * @author Tim Staudenmaier
 */
public class VerticalBlurShader extends ShaderProgram {

    /**
     * Path for the vertex shader file.
     */
    private static final String VERTEX_FILE = "/com/battleships/gui/postProcessing/gaussianBlur/verticalBlurVertexShader.glsl";
    /**
     * Path for the fragment shader file.
     */
    private static final String FRAGMENT_FILE = "/com/battleships/gui/postProcessing/gaussianBlur/blurFragmentShader.glsl";

    /**
     * Location value for the uniform variable targetHeight, that contains the height of the fbo this effect is used on.
     */
    private int location_targetHeight;

    /**
     * Create this shader program with the two shader files
     */
    protected VerticalBlurShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Load the height of the fbo that this effect is used for to the shader.
     * @param height width of the fbo
     */
    protected void loadTargetHeight(float height){
        super.loadFloat(location_targetHeight, height);
    }

    /**
     * Get the int value for the uniform variable and safe it to the location attribute.
     * Needs to be done to set a value to the uniform variable.
     */
    @Override
    protected void getAllUniformLocations() {
        location_targetHeight = super.getUniformLocation("targetHeight");
    }

    /**
     * Bind the position attribute to attribute 0, so vertex shader can use it as in variable.
     */
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
