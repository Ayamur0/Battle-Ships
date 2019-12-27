package com.battleships.gui.postProcessing.gaussianBlur;

import com.battleships.gui.particles.ParticleTexture;
import com.battleships.gui.shaders.ShaderProgram;

/**
 * Shader for the {@link HorizontalBlur} post processing effect.
 * 
 * @author Tim Staudenmaier
 */
public class HorizontalBlurShader extends ShaderProgram {

    /**
     * Path for the vertex shader file.
     */
    private static final String VERTEX_FILE = "/com/battleships/gui/postProcessing/gaussianBlur/horizontalBlurVertexShader.glsl";
    /**
     * Path for the fragment shader file.
     */
    private static final String FRAGMENT_FILE = "/com/battleships/gui/postProcessing/gaussianBlur/blurFragmentShader.glsl";

    /**
     * Location value for the uniform variable targetWidth, that contains the width of the fbo this effect is used on.
     */
    private int location_targetWidth;

    /**
     * Create this shader program with the two shader files
     */
    protected HorizontalBlurShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Load the width of the fbo that this effect is used for to the shader.
     * @param width width of the fbo
     */
    protected void loadTargetWidth(float width){
        super.loadFloat(location_targetWidth, width);
    }

    /**
     * Get the int value for the uniform variable and safe it to the location attribute.
     * Needs to be done to set a value to the uniform variable.
     */
    @Override
    protected void getAllUniformLocations() {
        location_targetWidth = super.getUniformLocation("targetWidth");
    }

    /**
     * Bind the position attribute to attribute 0, so vertex shader can use it as in variable.
     */
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
