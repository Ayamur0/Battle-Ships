package com.battleships.gui.fontRendering;

import com.battleships.gui.shaders.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class FontShader extends ShaderProgram {

    private static final String VERTEX_FILE = "/com/battleships/gui/fontRendering/fontVertexShader.glsl";
    private static final String FRAGMENT_FILE = "/com/battleships/gui/fontRendering/fontFragmentShader.glsl";

    private int location_color;
    private int location_translation;
    private int location_borderWidth;
    private int location_borderEdge;
    private int location_outlineColor;
    private int location_offset;

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
        location_borderWidth = super.getUniformLocation("borderWidth");
        location_borderEdge = super.getUniformLocation("borderEdge");
        location_outlineColor = super.getUniformLocation("outlineColor");
        location_offset = super.getUniformLocation("offset");
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

    /**
     * Upload width for the outline of the font to the shader code
     * @param width - width of the character outline, 0 for no outline
     */
    protected void loadBorderWidth(float width){
        super.loadFloat(location_borderWidth, width);
    }

    /**
     * Upload size of the edge of the font outline to the shader code
     * @param edge - size of the smooth transition at the edge of the character outline, to prevent sharp edges
     *             keep at 0.1 when no border is used
     */
    protected void loadBorderEdge(float edge){
        super.loadFloat(location_borderEdge, edge);
    }

    /**
     * Upload color of the font outline to the shader code
     * @param color - color of the font outline in r,g,b with values between 0 and 1
     */
    protected void loadOutlineColor(Vector3f color){
        super.loadVector(location_outlineColor, color);
    }

    protected void loadOffset(Vector2f offset){
        super.load2DVector(location_offset, offset);
    }

}
