package com.battleships.gui.models;

/**
 * Model for a {@link TexturedModel}.
 *
 * @author Tim Staudenmaier
 */
public class RawModel {

    /**
     * ID of the vao for this model.
     */
    private int vaoID;
    /**
     * Amount of vertices in this model.
     */
    private int vertexCount;

    /**
     * Create a new model.
     * @param vaoID - vaoId for this model.
     * @param vertexCount - Vertex count this model should have.
     */
    public RawModel (int vaoID, int vertexCount){
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    /**
     *
     * @return - ID of the vao of this model.
     */
    public int getVaoID() {
        return vaoID;
    }

    /**
     *
     * @return - Amount of vertices in this model.
     */
    public int getVertexCount() {
        return vertexCount;
    }
}
