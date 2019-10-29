package com.battleships.gui.Engine.Rendering.Shaders;

import Engine.Rendering.Entities.CameraOld;
import Engine.Rendering.Entities.EntityOld;
import Engine.Rendering.Models.ModelTexture;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class GLSLProgram {

    private static final float[] MATRIX_BUFFER = new float[16];

    private int program;
    private HashMap<String, Integer> uniformVariables = new HashMap<>(); //Uniform Variables make shaders changeable during runtime

    public GLSLProgram(String fragmentShaderFile){
        //create shader program
        program = GL20.glCreateProgram();
        attachShader(GL20.GL_FRAGMENT_SHADER, fragmentShaderFile);

        //add program to GL
        GL20.glLinkProgram(program);
        GL20.glValidateProgram(program);
    }

    public GLSLProgram(String vertexShaderFile, String fragmentShaderFile, String... vertexAttributes){
        //create shader program
        program = GL20.glCreateProgram();
        attachShader(GL20.GL_VERTEX_SHADER, vertexShaderFile);
        attachShader(GL20.GL_FRAGMENT_SHADER, fragmentShaderFile);

        //tell vertex shader which vbo attributes to use
        for(int i = 0; i < vertexAttributes.length; i++){
            GL20.glBindAttribLocation(program, i, vertexAttributes[i]);
        }

        //add program to GL
        GL20.glLinkProgram(program);
        GL20.glValidateProgram(program);
    }

    public void addUniformVariable(String variable){
        int location = GL20.glGetUniformLocation(program, variable);
        uniformVariables.put(variable, location);
    }

    //change variable (ex: basic.vs shader change rgb values for color)
    public void setVec3(String variable, float x, float y, float z){
        if(uniformVariables.containsKey(variable)){
            enable();
            int location = uniformVariables.get(variable);
            GL20.glUniform3f(location, x, y, z);
        }
    }

    public void setTexture(String variable, ModelTexture texture, int textureSlot){
        if(uniformVariables.containsKey(variable)){
            enable();
            //get variable from uniform shader variables
            int location = uniformVariables.get(variable);
            GL20.glUniform1i(location, textureSlot);
            //set texture in slot with index textureSlot to texture
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureSlot);
            //bind texture to OpenGL for OpenGL to be able to use it
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
        }
    }

    public void setMatrix(String variable, Matrix4f matrix){
        if(uniformVariables.containsKey(variable)){ //check if variable exists
            enable(); //upload shader
            int location = uniformVariables.get(variable); //get location (in shaders files) where variable is located
            //upload float values from Matrix one by one
            //first load into MATRIX_BUFFER so it's possible to upload one by one
            GL20.glUniformMatrix4fv(location, false, matrix.get(MATRIX_BUFFER));
        }
    }

    public void setMatrices(CameraOld camera, EntityOld entity){
        this.setMatrix("transformationMatrix", entity.getTransformationMatrix());
        this.setMatrix("viewMatrix", camera.getViewMatrix());
        this.setMatrix("projectionMatrix", camera.getProjectionMatrix());
    }

    public void enable(){
        GL20.glUseProgram(program);
    }

    public void disable(){
        GL20.glUseProgram(0);
    }

    public void release(){
        disable();
        GL20.glDeleteProgram(program);
    }

    private void attachShader(int shaderType, String file){
        try{
            //convert Shader from file to String
            BufferedReader reader = new BufferedReader(new InputStreamReader(GLSLProgram.class.getResourceAsStream(file)));
            StringBuilder builder = new StringBuilder();
            while(reader.ready()){
                builder.append(reader.readLine() + System.lineSeparator());
            }
            reader.close();

            //compile shader
            int id = GL20.glCreateShader(shaderType);
            GL20.glShaderSource(id, builder);
            GL20.glCompileShader(id);
            if(GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
                throw new RuntimeException("Failed to compile shader: " + System.lineSeparator() + GL20.glGetShaderInfoLog(id));
            }

            //add shader to program
            GL20.glAttachShader(program, id);
            //delete shader string
            GL20.glDeleteShader(id);
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
