package com.battleships.gui.fontRendering;

import com.battleships.gui.shaders.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class FontShader extends ShaderProgram {

    private static final String VERTEX_FILE = "/com/battleships/gui/fontRendering/fontVertexShader.glsl";
    private static final String FRAGMENT_FILE = "/com/battleships/gui/fontRendering/fontFragmentShader.glsl";

    private int location_color;
    private int location_translation;

    /**
     * Load shader files used for font shader program
     */
    public FontShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * store all int values for the locations of the uniform variables in the shader code.
     */
    @Override
    protected void getAllUniformLocations() {
        location_color = super.getUniformLocation("color");
        location_translation = super.getUniformLocation("translation");
    }

    /**
     * Bind the in variables of the vertex shader to the vao attributes for the object to be rendered
     */
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    /**
     * Use int value of the uniform location of the uniform variable color to store a vec3
     * color in that variable so the shader can use it.
     * @param color - color to be uploaded to the shader
     */
    protected void loadColor(Vector3f color){
        super.loadVector(location_color, color);
    }

    /**
     * Upload translation(position) 2d vector to the shader code
     * @param translation - translation to be uploaded to the shader
     */
    protected void loadTranslation(Vector2f translation){
        super.load2DVector(location_translation, translation);
    }


}
