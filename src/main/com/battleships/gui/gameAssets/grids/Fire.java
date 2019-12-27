package com.battleships.gui.gameAssets.grids;

import com.battleships.gui.audio.AudioMaster;
import com.battleships.gui.audio.Source;
import com.battleships.gui.particles.ParticleSystemComplex;
import com.battleships.gui.particles.ParticleTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Specific {@link ParticleSystemComplex} that creates a fire particle effect.
 * Can also create the sound for fires.
 *
 * @author Tim Staudenmaier
 */
public class Fire extends ParticleSystemComplex {

    /**
     * Sound of a fire.
     */
    private int fireSound = AudioMaster.loadSound("fire");

    /**
     * Create new fire particle system.
     * @param loader Loader to load texture.
     */
    public Fire(Loader loader) {
        super(new ParticleTexture(loader.loadTexture("particles/fire.png"), 8, true),20, 3.5f, -0.05f, 2f, 17);
        super.setLifeError(0.3f);
        super.setScaleError(0.3f);
        super.setSpeedError(0.15f);
        super.randomizeRotation();
        super.setDirection(new Vector3f(0.1f, 1, 0.1f), -0.15f);
    }

    /**
     * Create a new source that plays a fire sound.
     * @param pos Position of the source.
     * @return The created source.
     */
    public Source createFireSound(Vector2f pos){
        Source sound = new Source(1, 10, 300);
        sound.setPosition(pos.x, 0, pos.y);
        sound.setLooping(true);
        sound.play(fireSound);
        return sound;
    }
}
