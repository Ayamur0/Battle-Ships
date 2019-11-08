package com.battleships.gui.particles;

import com.battleships.gui.window.WindowManager;
import org.joml.Vector3f;

public class Particle {

    private static final float GRAVITY = -50;

    private Vector3f position;
    private Vector3f velocity;
    private float gravityEffect;
    private float lifeLength;
    private float rotation;
    private float scale;

    private float elapsedTime = 0;

    public Particle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        //add particle to list of rendered particles
        ParticleMaster.addParticle(this);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    /**
     * Calculate how far to move the particle this frame, by using the
     * delta time (time elapsed during last frame) and the velocity and gravity values.
     * Save elapsedTime to keep track of how long particle is alive.
     * @return - {@code true} if the particle is still alive in this frame,
     *           {@code false} if the particle is alive longer than his lifeLength so he needs to be removed
     */
    protected boolean update(){
        velocity.y += GRAVITY * gravityEffect * WindowManager.getDeltaTime();
        Vector3f change = new Vector3f(velocity);
        change.mul(WindowManager.getDeltaTime());
        change.add(position, position);
        elapsedTime += WindowManager.getDeltaTime();
        return elapsedTime < lifeLength;
    }
}
