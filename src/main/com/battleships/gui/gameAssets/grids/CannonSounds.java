package com.battleships.gui.gameAssets.grids;

import com.battleships.gui.audio.AudioMaster;
import com.battleships.gui.audio.Source;
import org.joml.Vector3f;

/**
 * Contains all sounds that can be played by a cannon or the impact of the cannonball.
 *
 * @author Tim Staudenmaier
 */

public class CannonSounds {

    /**
     * Constants differentiating the playable sounds.
     */
    public static final int CANNONSOUND = 0, WATERSPLASH = 1, HITSOUND = 2;

    /**
     * Source that can play the sound of a firing cannon.
     */
    private Source cannon = new Source(1, 40, 500);
    /**
     * SoundBuffer of the cannonSound.
     */
    private int cannonSound = AudioMaster.loadSound("Cannon");
    /**
     * Source for playing either waterSplashSound or hitSound on cannonball impact.
     */
    private Source waterSplash = new Source(1, 40, 500);
    /**
     * SoundBuffer for a waterSplash.
     */
    private int waterSplashSound = AudioMaster.loadSound("WaterSplash2");
    /**
     * SoundBuffer for a sound of a cannonball hitting a ship.
     */
    private int hitSound = AudioMaster.loadSound("HitSoundShort");

    /**
     * Plays the specified sound at a position.
     *
     * @param pos  Position the sound should be played at.
     * @param type Type of sound (one of the constants in this class).
     */
    public void playSound(Vector3f pos, int type) {
        switch (type) {
            case CANNONSOUND:
                cannon.setPosition(pos.x, pos.y, pos.z);
                cannon.play(cannonSound);
                break;
            case WATERSPLASH:
                waterSplash.setPosition(pos.x, pos.y, pos.z);
                waterSplash.play(waterSplashSound);
                break;
            case HITSOUND:
                waterSplash.setPosition(pos.x, pos.y, pos.z);
                waterSplash.play(hitSound);
                break;
        }
    }

    /**
     * Stops the sounds currently playing.
     */
    public void stopSound() {
        cannon.stop();
        waterSplash.stop();
    }
}
