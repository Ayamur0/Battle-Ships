package com.battleships.gui.Engine.Rendering.Entities;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class CameraOld {

    private static final Vector3f UP_VECTOR = new Vector3f(0, 1, 0); //Vector looking straight up

    protected Matrix4f viewMatrix = new Matrix4f(); //Matrix to convert transformation matrix of entity (pos,scale,rot) relative to camera position
    protected Matrix4f projectionMatrix = new Matrix4f(); //Matrix to convert 3D entity to 2D to be displayed on screen

    protected Vector3f position = new Vector3f(); //camera position
    protected Vector3f rotation = new Vector3f(0, 90 ,0); //camera rotation, rotate to look horizontally
    protected Vector3f direction = new Vector3f(); //direction camera is facing
    protected Vector3f target = new Vector3f(); //vector of direction camera should be facing afterwards (new direction)

    protected float nearPlane = 0.1f; //Entities closer to camera than near plane don't get rendered
    protected float farPlane = 1000f; //Entities further away than far plane don't get rendered
    protected float fov = 60; //Field of view from camera (view angle) entities outside this angel don't get rendered

    //update view and projection matrix after transformation matrix has changed
    public void update() {
        double pitch = Math.toRadians(rotation.x);
        double yaw = Math.toRadians(rotation.y);
        //don't convert z rotation, because it's not used as it's close to the same as x rotation

        //convert rotations into direction vectors
        direction.x = (float) (Math.cos(pitch) * Math.sin(yaw));
        direction.y = (float) Math.sin(pitch);
        direction.z = (float) (Math.cos(pitch) * Math.cos(yaw));

        //calculate viewMatrix from direction vectors
        //Position = current camera position position.add adds direction to current position (result = new camera alignment) and safes new value in target
        //using UP_VECTOR doesn't change z rotation of object as it's not needed
        viewMatrix.setLookAt(position, position.add(direction, target), UP_VECTOR);

        //calculate projectionMatrix
        //define fov of what gets seen by camera --> shown on screen
        //aspect (second parameter) defines aspect ratio of window (currently not changeable) //TODO
        //add near and far plane to only render necessary objects
        projectionMatrix.setPerspective((float)Math.toRadians(fov), 800f / 600f, nearPlane, farPlane);

    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }
}
