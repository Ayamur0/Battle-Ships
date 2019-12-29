package com.battleships.gui.particles;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * 2D Particle. Can be used in {@link ParticleSystemComplex} or {@link ParticleSystemSimple} to create particle effects.
 * Before using any Particles the {@link ParticleMaster} needs to be initialized!
 *
 * @author Tim Staudenmier
 */
public class Particle {

    /**
     * How much the particles are affected by gravity.
     * Negative value means they float upwards.
     */
    private static final float GRAVITY = -50;

    /**
     * Position of the particle (world coordinates).
     */
    private Vector3f position;
    /**
     * Velocity of the particle.
     */
    private Vector3f velocity;
    /**
     * How much the particle is influenced by gravity.
     */
    private float gravityEffect;
    /**
     * How long the particle lives in seconds.
     */
    private float lifeLength;
    /**
     * Rotation of the particle.
     * Only one rotation possible, because 2D particle always faces camera.
     */
    private float rotation;
    /**
     * Scale of the particle.
     */
    private float scale;

    /**
     * TextureAtlas this particle uses.
     */
    private ParticleTexture texture;

    /**
     * First texture in the textureAtlas this particle uses.
     */
    private Vector2f texOffset1 = new Vector2f();
    /**
     * Texture in the textureAtlas that gets blended over the first one.
     */
    private Vector2f texOffset2 = new Vector2f();
    /**
     * How much the next texture in the textureAtlas should be blended
     * over the current one for smooth transition.
     */
    private float blend;

    /**
     * Time this particle is alive in seconds.
     */
    private float elapsedTime = 0;
    /**
     * Distance from this particle to the camera.
     */
    private float distance;

    /**
     * Create a new particle.
     * @param texture TextureAtlas of this particle.
     * @param position Position at which this particle should start.
     * @param velocity Velocity this particle should start with.
     * @param gravityEffect how much this particle should be influenced by gravity.
     * @param lifeLength  How long this particle should live in seconds.
     * @param rotation Rotation of this particle.
     * @param scale Scale of this particle
     */
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

    /**
     *
     * @return The distance from this particle to the camera.
     */
    public float getDistance() {
        return distance;
    }

    /**
     * @return The textureOffset of the currently used texture in the TextureAtlas.
     */
    public Vector2f getTexOffset1() {
        return texOffset1;
    }

    /**
     *
     * @return The textureOffset of the texture that is blended onto the normal texture of this particle (next texture in the textureAtlas).
     */
    public Vector2f getTexOffset2() {
        return texOffset2;
    }

    /**
     *
     * @return How much the next texture is blended over the current one.
     */
    public float getBlend() {
        return blend;
    }

    /**
     *
     * @return The TextureAtlas of this particle.
     */
    public ParticleTexture getTexture() {
        return texture;
    }

    /**
     *
     * @return The current position of this particle.
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     *
     * @return The current rotation of this particle.
     */
    public float getRotation() {
        return rotation;
    }

    /**
     *
     * @return The scale of this particle.
     */
    public float getScale() {
        return scale;
    }

    /**
     * Calculate how far to move the particle this frame, by using the
     * delta time (time elapsed during last frame) and the velocity and gravity values.
     * Also calculate how far away particle is from camera and safe to distance.
     * Save elapsedTime to keep track of how long particle is alive.
     * @return {@code true} if the particle is still alive in this frame,
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
     * @param offset Vector to save the calculated offset to
     * @param index index of the current texture in the texture Atlas
     */
    private void setTextureOffset(Vector2f offset, int index){
        int column = index % texture.getNumberOfRows();
        int row = index / texture.getNumberOfRows();
        offset.x = (float)column / texture.getNumberOfRows();
        offset.y = (float)row / texture.getNumberOfRows();
    }
}
