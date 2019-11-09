package com.battleships.gui.particles;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Particle {

    private static final float GRAVITY = -50;

    private Vector3f position;
    private Vector3f velocity;
    private float gravityEffect;
    private float lifeLength;
    private float rotation;
    private float scale;

    private ParticleTexture texture;

    private Vector2f texOffset1 = new Vector2f();
    private Vector2f texOffset2 = new Vector2f();
    private float blend;

    private float elapsedTime = 0;
    private float distance;

    public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
        this.texture = texture;
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        //add particle to list of rendered particles
        ParticleMaster.addParticle(this);
    }

    public float getDistance() {
        return distance;
    }

    public Vector2f getTexOffset1() {
        return texOffset1;
    }

    public Vector2f getTexOffset2() {
        return texOffset2;
    }

    public float getBlend() {
        return blend;
    }

    public ParticleTexture getTexture() {
        return texture;
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
     * Also calculate how far away particle is from camera and safe to distance.
     * Save elapsedTime to keep track of how long particle is alive.
     * @return - {@code true} if the particle is still alive in this frame,
     *           {@code false} if the particle is alive longer than his lifeLength so he needs to be removed
     */
    protected boolean update(Camera camera){
        velocity.y += GRAVITY * gravityEffect * WindowManager.getDeltaTime();
        Vector3f change = new Vector3f(velocity);
        change.mul(WindowManager.getDeltaTime());
        change.add(position, position);
        Vector3f distanceVector = new Vector3f();
        camera.getPosition().sub(position, distanceVector);
        distance = distanceVector.lengthSquared();
        updateTextureCoordInfo();
        elapsedTime += WindowManager.getDeltaTime();
        return elapsedTime < lifeLength;
    }

    /**
     * Calculate which texture from the textureAtlas to use for this particle.
     * The texture gets calculated corresponding to how long the particle is alive.
     * It starts at texture index 0 at the beginning of it's life and before it dies it has the last texture.
     * Then the offset for the textureCoords are calculated so the right texture is used.
     * The Blend value is calculated to blend smoothly between one and the next texture.
     * To correctly blend the current and next texture, the texture after the current one in the atlas also needs to be calculated.
     */
    private void updateTextureCoordInfo(){
        float lifeFactor = elapsedTime / lifeLength;
        int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
        float atlasProgression = lifeFactor * stageCount;
        int index1 = (int) Math.floor(atlasProgression);
        int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
        this.blend = atlasProgression % 1;
        setTextureOffset(texOffset1, index1);
        setTextureOffset(texOffset2, index2);

    }

    /**
     * Calculate current offset of texture in textureAtlas.
     * First get number of row and column as integers.
     * Then divide by number of rows so number and row are between 0 and 1,
     * because the textureCoords are all between 0 and 1.
     * @param offset - Vector to save the calculated offset to
     * @param index - index of the current texture in the texture Atlas
     */
    private void setTextureOffset(Vector2f offset, int index){
        int column = index % texture.getNumberOfRows();
        int row = index / texture.getNumberOfRows();
        offset.x = (float)column / texture.getNumberOfRows();
        offset.y = (float)row / texture.getNumberOfRows();
    }
}
