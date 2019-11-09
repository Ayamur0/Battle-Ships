package com.battleships.gui.particles;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.models.ModelTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.*;

public class ParticleMaster {

    private static Map<ParticleTexture, List<Particle>> particles = new HashMap<>();
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
     * Iterate over all particles lists and then all particles in the list.
     * Delete dead particles from the list, so they don't get rendered anymore.
     * If list is empty after deleting particle from it, remove entry with that list
     * from hashMap particles.
     * Sort the list by the distance of each to the camera, so when they get rendered, they
     * get rendered in the right order.
     * Uses insertionSort because the list will be almost sorted most of the time, so insertion sort
     * is the most efficient.
     */
    public static void update(Camera camera){
        Iterator<Map.Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();
        while (mapIterator.hasNext()){
            Map.Entry<ParticleTexture, List<Particle>> entry = mapIterator.next();
            List<Particle> list = entry.getValue();
            Iterator<Particle> iterator = list.iterator();
            while (iterator.hasNext()){
                Particle p = iterator.next();
                boolean stillAlive = p.update(camera);
                if(!stillAlive){
                    iterator.remove();
                    if(list.isEmpty()){
                        mapIterator.remove();
                    }
                }
            }
            if(!entry.getKey().isAdditive())
                InsertionSort.sortHighToLow(list);
        }
    }

    /**
     * Render all particles in particles List using renderer.
     * @param camera - Camera that should display particles
     * @param mode - mode to use for rendering 1 = add color of particles, 771 = render particles over each other
     */
    public static void renderParticles(Camera camera, int mode){
        renderer.render(particles, camera);
    }

    public static void cleanUp(){
        renderer.cleanUp();
    }

    /**
     * Add particle to particles that should be rendered.
     * If list for particles with that textures already exists add particle to that list.
     * Else create new entry in hashMap with that texture and a new list of particles.
     * @param particle - Particle to add
     */
    public static void addParticle(Particle particle){
        List<Particle> list = particles.computeIfAbsent(particle.getTexture(), k -> new ArrayList<>());
        list.add(particle);
    }
}
