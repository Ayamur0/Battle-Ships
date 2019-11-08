package com.battleships.gui.particles;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.models.ModelTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleMaster {

    private static List<Particle> particles = new ArrayList<>();
    private static ParticleRenderer renderer;

    /**
     * Initialize particle system.
     * @param loader - loader to pass to ParticleRenderer
     * @param projectionMatrix - projectionMatrix to pass to ParticleRenderer
     */
    public static void init(Loader loader, Matrix4f projectionMatrix){
        renderer = new ParticleRenderer(loader, projectionMatrix);
    }

    /**
     * Iterate over all particles and delete dead particles from the list,
     * so they don't get rendered anymore
     */
    public static void update(){
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()){
            Particle p = iterator.next();
            boolean stillAlive = p.update();
            if(!stillAlive){
                iterator.remove();
            }
        }
    }

    /**
     * Render all particles in particles List using renderer.
     * @param camera - Camera that should display particles
     */
    public static void renderParticles(Camera camera){
        renderer.render(particles, camera);
    }

    public static void cleanUp(){
        renderer.cleanUp();
    }

    /**
     * Add particle to particles that should be rendered.
     * @param particle - Particle to add
     */
    public static void addParticle(Particle particle){
        particles.add(particle);
    }
}
