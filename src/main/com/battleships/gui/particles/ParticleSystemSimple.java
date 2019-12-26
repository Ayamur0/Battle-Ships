package com.battleships.gui.particles;

import com.battleships.gui.window.WindowManager;
import org.joml.Vector3f;

/**
 * A simple Particle System to create basic Particle effects using {@link Particle}
 *
 * @author Tim Staudenmaier
 */

public class ParticleSystemSimple {

    /**
     * pps              - Particles emitted per second.
     * Speed            - Speed each particle gets emitted at.
     * gravityComplient - How much emitted particles are affected by gravity (negative for inverted gravity)
     * lifeLength       - How long the particles are alive.
     */
    private float pps;
    private float speed;
    private float gravityComplient;
    private float lifeLength;

    /**
     * Texture all particles of this system use.
     */
    private ParticleTexture texture;

    /**
     * Create a new particleSystem.
     * @param texture - Texture all particles of this system should use.
     * @param pps - How many particles should be emitted per second.
     * @param speed - How fast the particles should be emitted.
     * @param gravityComplient - How much the particles are influenced by gravity.
     * @param lifeLength - How long the particles should live.
     */
    public ParticleSystemSimple(ParticleTexture texture, float pps, float speed, float gravityComplient, float lifeLength) {
        this.texture = texture;
        this.pps = pps;
        this.speed = speed;
        this.gravityComplient = gravityComplient;
        this.lifeLength = lifeLength;
    }

    /**
     * Generate particles using all the settings of this system.
     * @param systemCenter - Center from which the particles should be generated.
     */
    public void generateParticles(Vector3f systemCenter){
        float delta = WindowManager.getDeltaTime();
        float particlesToCreate = pps * delta;
        int count = (int) Math.floor(particlesToCreate);
        float partialParticle = particlesToCreate % 1;
        for(int i=0;i<count;i++){
            emitParticle(systemCenter);
        }
        if(Math.random() < partialParticle){
            emitParticle(systemCenter);
        }
    }

    /**
     * Emits one particle with the settings of this system.
     * @param center - Position the particle is emitted from.
     */
    private void emitParticle(Vector3f center){
        float dirX = (float) Math.random() * 2f - 1f;
        float dirZ = (float) Math.random() * 2f - 1f;
        Vector3f velocity = new Vector3f(dirX, 1, dirZ);
        velocity.normalize();
        velocity.mul(speed);
        new Particle(texture, new Vector3f(center), velocity, gravityComplient, lifeLength, 0, 1);
    }
}
