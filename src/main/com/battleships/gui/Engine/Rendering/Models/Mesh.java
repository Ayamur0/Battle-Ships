package Engine.Rendering.Models;

import Engine.Rendering.Shaders.GLSLProgram;
import org.lwjgl.opengl.*;

public class Mesh {
    //VertexArrayObject
    private int vaoID;
    private int[] vbos; //VertexBufferObjects (position, texture,...)
    private int vertexCount;
    private boolean isUsingIndexBuffer;
    private int indexBufferVBO;

    //create Mesh with coordinates
    public Mesh(float[] positions) {
        //make vao usable
        vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);

        //create position vbo with 3 dates x,y,z
        int positionVBO = addStaticAttribute(0, positions, 3);

        vbos = new int[] {positionVBO};
        vertexCount = positions.length / 3;
    }

    //create Mesh with indices
    public Mesh(int[] indices ,float[] positions) {
        //make vao usable
        vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);

        indexBufferVBO = attachIndexBuffer(indices);
        //create position vbo with 3 dates x,y,z
        int positionVBO = addStaticAttribute(0, positions, 3);

        vbos = new int[] {positionVBO};
        vertexCount = indices.length;

        isUsingIndexBuffer = true;
    }

    //create Mesh with indices and textureCoordinates
    public Mesh(int[] indices ,float[] positions, float[] textureCoordinates) {
        //make vao usable
        vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);

        indexBufferVBO = attachIndexBuffer(indices);
        //create position vbo with 3 dates x,y,z
        int positionVBO = addStaticAttribute(0, positions, 3);
        //create texture vbo with 2 coords x,y
        int textureCoordsVBO = addStaticAttribute(1, textureCoordinates, 2);

        vbos = new int[] {positionVBO, textureCoordsVBO};
        vertexCount = indices.length;

        isUsingIndexBuffer = true;
    }

    public void render(){
        GL30.glBindVertexArray(vaoID);

        //activate vbos
        for(int i = 0; i < vbos.length; i++)
            GL20.glEnableVertexAttribArray(i);

        if(isUsingIndexBuffer) {
            GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);
        }
        else {
            //draw object in window
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);
        }

        //disable vbos
        for(int i = 0; i < vbos.length; i++) {
            GL20.glDisableVertexAttribArray(i);
        }
    }

    public void render2(int[] meshIndices, TextureLoader[] textures, GLSLProgram shaderProgram){
        GL30.glBindVertexArray(vaoID);
        int rendered = 0;

        //activate vbos
        for(int i = 0; i < vbos.length; i++)
            GL20.glEnableVertexAttribArray(i);

        for(int i = 0; i < meshIndices.length; i++){
            shaderProgram.setTexture("diffuseTexture", textures[i],0);  //set texture in shaderProgram for part i of model rendering
            GL13.glActiveTexture(GL13.GL_TEXTURE0);  //activate texture
            GL13.glBindTexture(GL13.GL_TEXTURE_2D, textures[i].getID());  //bind texture
            if(i == 0) {
                rendered += meshIndices[i] + 3;
                GL13.glDrawElements(GL11.GL_TRIANGLES, meshIndices[i] + 3, GL11.GL_UNSIGNED_INT, 0);  //render entity with corresponding texture
            }
            else {
                GL13.glDrawElements(GL11.GL_TRIANGLES, meshIndices[i] + 3 - rendered, GL11.GL_UNSIGNED_INT, meshIndices[i - 1] * 4);  //render entity with corresponding texture
                rendered += meshIndices[i] + 3;
            }
        }

        for(int i = 0; i < vbos.length; i++) {
            GL20.glDisableVertexAttribArray(i);
        }
    }

    //free resources from rendering after game exit
    public void release(){
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glDeleteVertexArrays(vaoID);
        for(int id : vbos)
            GL15.glDeleteBuffers(id);
        if(isUsingIndexBuffer)
            GL15.glDeleteBuffers(indexBufferVBO);
    }

    //create new vbo
    private int addStaticAttribute(int index, float[] data, int dataSize){
        //bind vbo
        int vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

        //generate buffer
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(index, dataSize, GL11.GL_FLOAT, false, 0, 0);
        return vbo;
    }

    //create vbo from indices
    private int attachIndexBuffer(int[] indices){
        int vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
        return vbo;
    }
}
