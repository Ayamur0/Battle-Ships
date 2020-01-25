package com.battleships.gui.renderingEngine;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.models.ModelTexture;
import com.battleships.gui.models.RawModel;
import com.battleships.gui.models.TextureData;
import com.battleships.gui.models.TexturedModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Loader for loading any image to a texture and for loading and creating vbos, vaos, {@link RawModel}s and {@link Entity}.
 *
 * @author Tim Staudenmaier
 */

public class Loader {

    /**
     * List of all currently loaded vaos.
     */
    private List<Integer> vaos = new ArrayList<>();
    /**
     * List of all currently loaded vbos.
     */
    private List<Integer> vbos = new ArrayList<>();
    /**
     * List of all currently loaded textures.
     */
    private List<Integer> textures = new ArrayList<>();

    /**
     * Create empty vbo.
     * Size is corresponding to amount of floats that should be saved in vbo.
     *
     * @param floatCount the amount of float values that should be later stored int the vbo
     * @return the id of the created vbo
     */
    public int createEmptyVbo(int floatCount) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatCount * 4, GL15.GL_STREAM_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return vbo;
    }

    /**
     * Adds data to a vbo, all data that was in that vbo before is lost.
     *
     * @param vbo    vbo the data should be added to
     * @param data   the data to add
     * @param buffer a float buffer needed to write data into vbo
     */
    public void updateVbo(int vbo, float[] data, FloatBuffer buffer) {
        buffer.clear();
        //add data to float buffer
        buffer.put(data);
        //flip buffer to read mode
        buffer.flip();
        //bind vbo to write to
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        //add data to vbo
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity(), GL15.GL_STREAM_DRAW);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
        //unbind vbo
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Add a instance float attribute to a vbo with divisor 1 so it changes each time the vbo it's stored in gets used.
     *
     * @param vao                 the vao that contains the vbo
     * @param vbo                 the vbo the attribute should be added to
     * @param attribute           the attribute id that should be added
     * @param dataSize            the size of each data in the attribute
     * @param instancedDataLength length of the data in the attribute (amount of float values)
     * @param offset              offset at which this attribute begins for each instance in the vbo
     */
    public void addInstancedAttribute(int vao, int vbo, int attribute, int dataSize, int instancedDataLength, int offset) {
        //bin vao and vbo
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL30.glBindVertexArray(vao);
        //create pointer for attribute to add
        GL20.glVertexAttribPointer(attribute, dataSize, GL15.GL_FLOAT, false, instancedDataLength * 4, offset * 4);
        //set attribute as instance attribute with divisor 1, so it changes each time the vao gets rendered, to the next attribute
        GL33.glVertexAttribDivisor(attribute, 1);
        //unbind vao and vbo
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    /**
     * Create a vao for a 3D object from the data for the vertices in that vao.
     *
     * @param positions     Positions of all vertices (x,y,z).
     * @param textureCoords TextureCorrds of all vertices (x,y,z).
     * @param normals       Normals of all vertices (x,y,z)
     * @param indices       Indices indicating the order in which the vertices are connected.
     * @return A {@link RawModel} containing all the loaded data for the model.
     */
    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normals);
        unbindVAO();
        return new RawModel(vaoID, indices.length); //3 coords per vertex
    }

    /**
     * Create a vao for a plane.
     *
     * @param positions  Positions of the vertices for the plane (x,y for 2D x,y,z for 3D).
     * @param dimensions Dimensions the plane should have (2 for 2D or 3 for 3D).
     * @return ID of the created vao.
     */
    public RawModel loadToVAO(float[] positions, int dimensions) {
        int vaoID = createVAO();
        this.storeDataInAttributeList(0, dimensions, positions);
        unbindVAO();
        return new RawModel(vaoID, positions.length / dimensions);
    }

    /**
     * Create a vao for a 2D plane that can be textured.
     *
     * @param positions     Positions of the vertices of the plane.
     * @param textureCoords TexturedCoordinates of the vertices.
     * @return ID of the created vao.
     */
    public int loadToVAO(float[] positions, float[] textureCoords) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, 2, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        unbindVAO();
        return vaoID;
    }

    /**
     * Load the texture of a font file.
     *
     * @param fileName Path to the image that contains the textureAtlas for the font.
     * @return ID of the loaded texture.
     */
    public int loadFontTexture(String fileName) {
        ModelTexture texture = TextureLoader.loadPNGTexture(fileName);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);
        textures.add(texture.getID());
        return texture.getID();
    }

    /**
     * Load a standard texture from an image.
     *
     * @param fileName Path to the image that contains the texture.
     *                 Recommended formats are .jpg, .png and .tga.
     * @return ID of the loaded texture.
     */
    public int loadTexture(String fileName) {
        ModelTexture texture;

        if (fileName.contains("png"))
            texture = TextureLoader.loadPNGTexture(fileName);
        else
            texture = TextureLoader.loadTexture(fileName);

        //use mipmap to render textures that are further away in lower resolution
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1.5f);
        textures.add(texture.getID());
        return texture.getID();
    }


    /**
     * Load a cubeMap texture consisting of 6 separate textures.
     *
     * @param textureFiles Array containing the paths for the 6 textures of the cubeMap.
     * @return ID of the loaded cubeMap texture.
     */
    public int loadCubeMap(String[] textureFiles) {
        //activate and bind texture bank
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

        //load all images to opengl
        for (int i = 0; i < textureFiles.length; i++) {
            TextureData data = TextureLoader.loadTextureData("/com/battleships/gui/res/textures/skybox/" + textureFiles[i] + ".png");
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA,
                    data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        textures.add(texID);
        return texID;
    }

    /**
     * Load a model from an OBJFile.
     *
     * @param objPath      Path to the OBJFile containing the model.
     * @param texturePath  Path to the corresponding texture image.
     * @param shineDamper  How much the light this model reflects should spread.
     * @param reflectivity How much light this model should reflect.
     * @return A {@link TexturedModel} containing all the loaded data.
     */
    public TexturedModel loadModelFromOBJ(String objPath, String texturePath, float shineDamper, float reflectivity) {
        RawModel model = OBJLoader.loadObjModel(objPath);
        TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loadTexture(texturePath)));
        ModelTexture texture = texturedModel.getTexture();
        texture.setReflectivity(reflectivity);
        texture.setShineDamper(shineDamper);
        return texturedModel;
    }

    /**
     * Clean up all loaded vaos, vbos and textures.
     * Needs to be called on program exit.
     */
    public void cleanUp() {
        //delete all vaos
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        //delete all vbos
        for (int vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        //delete all textures
        for (int texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }

    /**
     * Create a VertexArrayObject that stores multiple vbos, and represents one attribute of a model (positions, textures, normals,...)
     *
     * @return ID of the created vao.
     */
    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays(); //create ID for a new vao
        vaos.add(vaoID);  //add vao to vao list, to keep track of all vaos to be able to unbind all on program exit
        GL30.glBindVertexArray(vaoID);  //bind a vertex array to that id
        return vaoID;
    }

    /**
     * Stores data in a vao using vbos.
     *
     * @param attributeNumber Attribute number in the vao the data should be stored to.
     * @param coordinateSize  How many float values in the data make up one coordinate (for 3D coordinates x,y,z 3 values).
     * @param data            Actual data that should be stored in the vao.
     */
    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        int vboID = GL15.glGenBuffers(); //generate new vbo
        vbos.add(vboID); //add vbo to vbo list, to keep track of all vbos to be able to unbind all on program exit
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); //Static data, because it shouldn't be edited
        //create pointer to attribute with number attributeNumber, with the size 3 (x,y,z),
        // 0 -> no other data which needs to be skipped when reading array, 0 -> no offset start at index 0
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        //unbind buffer after using
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Unbind the currently bound vao.
     */
    private void unbindVAO() {
        GL30.glBindVertexArray(0); //unbind vao by binding 0
    }

    /**
     * Bind data from an IntBuffer to the currently bound vao.
     * Used for binding the indices of the vertices to a vao.
     *
     * @param indices int array containing the indices of the vertices for the currently bound vao.
     */
    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID); //create indices array buffer
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);  //Static data, because it shouldn't be edited
    }

    /**
     * Convert data from a int array into a int buffer.
     *
     * @param data Data in an array that should be converted.
     * @return A buffer containing all the int values in the same order as the array did.
     */
    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();//finish writing to buffer and readying it to read from it
        return buffer;
    }

    /**
     * Convert data from a float array into a float buffer.
     *
     * @param data Data in an array that should be converted.
     * @return A buffer containing all the float values in the same order as the array did.
     */
    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip(); //finish writing to buffer and readying it to read from it
        return buffer;
    }
}
