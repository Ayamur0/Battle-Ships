package com.battleships.gui.Engine.Rendering.Entities;

import Engine.Rendering.Models.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class EntityOld {

    private Mesh mesh; //Mesh (Model) for Object
    private Vector3f position; //Variable that contains 3 float vector
    private Vector3f rotation;
    private Vector3f scale;
    private Matrix4f transformationMatrix; //Combine position, rotation, scale in matrix so OpenGL has all data to know how and where to place the object

    public EntityOld(Mesh mesh){
        this.mesh = mesh;
        this.position = new Vector3f();
        this.rotation = new Vector3f();
        this.scale = new Vector3f(1, 1, 1);
        this.transformationMatrix = new Matrix4f();
    }

    //use to update entity after changing rotation/position/scale
    public void update(){
        transformationMatrix.identity(); //reset matrix
        transformationMatrix.translate(position); //add position vector into the matrix
        transformationMatrix.scale(scale); //add scale vector into matrix
        //add rotation vector into matrix, convert degree values to radian
        transformationMatrix.rotateXYZ((float)Math.toRadians(rotation.x), (float)Math.toRadians(rotation.y), (float)Math.toRadians(rotation.z));
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Matrix4f getTransformationMatrix() {
        return transformationMatrix;
    }
}
