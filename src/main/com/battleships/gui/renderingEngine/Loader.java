package com.battleships.gui.renderingEngine;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.models.ModelTexture;
import com.battleships.gui.models.RawModel;
import com.battleships.gui.models.TexturedModel;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3, positions);
        storeDataInAttributeList(1,2, textureCoords);
        storeDataInAttributeList(2, 3, normals);
        unbindVAO();
        return new RawModel(vaoID, indices.length); //3 coords per vertex
    }

    public int loadTexture(String fileName){
        ModelTexture texture = TextureLoader.loadTexture(fileName);
        //use mipmap to render textures that are further away in lower resolution
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1.5f);
        textures.add(texture.getID());
        return texture.getID();
    }

    public Entity loadEntityfromOBJ(String objPath, String texturePath){
        RawModel model = OBJLoader.loadObjModel(objPath);
        TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loadTexture(texturePath)));
        return new Entity(texturedModel, new Vector3f(0,0,-1), new Vector3f(), 1);
    }

    public Entity loadEntityfromOBJ(String objPath, String texturePath, float shineDamper, float reflectivity){
        RawModel model = OBJLoader.loadObjModel(objPath);
        TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loadTexture(texturePath)));
        ModelTexture texture = texturedModel.getTexture();
        texture.setReflectivity(reflectivity);
        texture.setShineDamper(shineDamper);
        return new Entity(texturedModel, new Vector3f(0,0,-1), new Vector3f(), 1);
    }

    public void cleanUp(){
        //delete all vaos
        for (int vao : vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        //delete all vbos
        for(int vbo : vbos){
            GL15.glDeleteBuffers(vbo);
        }
        //delete all textures
        for (int texture : textures){
            GL11.glDeleteTextures(texture);
        }
    }

    //create VertexArrayObject that stores multiple vbos, and represents one attribute of a model (positions, textures, normals,...)
    private int createVAO(){
        int vaoID = GL30.glGenVertexArrays(); //create ID for a new vao
        vaos.add(vaoID);  //add vao to vao list, to keep track of all vaos to be able to unbind all on program exit
        GL30.glBindVertexArray(vaoID);  //bind a vertex array to that id
        return vaoID;
    }

    //store data in a vao, by using vbos
    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data){
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

    private void unbindVAO(){
        GL30.glBindVertexArray(0); //unbind vao by binding 0
    }

    private void bindIndicesBuffer(int[] indices){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID); //create indices array buffer
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);  //Static data, because it shouldn't be edited
    }

    private IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();//finish writing to buffer and readying it to read from it
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip(); //finish writing to buffer and readying it to read from it
        return buffer;
    }
}
