package com.battleships.gui.fontMeshCreator;

/**
 * All the data of the mesh a text is rendered on.
 *
 * @author Tim Staudenmaier
 */
public class TextMeshData {

    /**
     * array with positions of all vertices (x,y) of this textMesh.
     */
    private float[] vertexPositions;
    /**
     * array with all textureCoords for the vertices (x,y) of this textMesh.
     */
    private float[] textureCoords;

    /**
     * @param vertexPositions array with positions of all vertices (x,y)
     * @param textureCoords   array with all textureCoords for the vertices (x,y)
     */

    protected TextMeshData(float[] vertexPositions, float[] textureCoords) {
        this.vertexPositions = vertexPositions;
        this.textureCoords = textureCoords;
    }

    /**
     * @return the array containing all vertices positions
     */

    public float[] getVertexPositions() {
        return vertexPositions;
    }

    /**
     * @return the array containing all textureCoords for the vertices
     */

    public float[] getTextureCoords() {
        return textureCoords;
    }

    /**
     * @return the amount of vertices of this mesh
     */

    public int getVertexCount() {
        return vertexPositions.length / 2;
    }

}