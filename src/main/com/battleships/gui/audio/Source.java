package com.battleships.gui.audio;

import org.lwjgl.openal.AL10;

/**
 * Sources are the main tool for emitting sound in the world.
 * A source can play sounds with different settings at one spot in the world.
 *
 * @author Tim Staudenmaier
 */
public class Source {

    private int sourceId;

    /**
     * Create a source that is capable of playing a sound.
     * @param rolloff - How fast the volume decreases if the listener goes further away from the source. (Higher means faster decrease)
     * @param referenceDist - The distance at which the sound volume matches the standard volume of the AudioManager
     * @param maxDist - The maximum Distance at which the sound can be heard, after this the sound cannot be heard
     *                even if it would have benn loud enough.
     */
    public Source(float rolloff, float referenceDist, float maxDist) {
        this.sourceId = AL10.alGenSources();
        AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, rolloff);
        AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, referenceDist);
        AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, maxDist);
        setVolume(AudioMaster.getSFXVolume());
        AudioMaster.addSource(this);
    }

    /**
     * This source starts playing the given sound with it's current settings.
     * @param buffer - The buffer containing the sound that should be played.
     *                (Buffers for sound files can be generated using the AudioManager)
     */
    public void play(int buffer){
        stop();
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
        continuePlaying();
    }

    /**
     * Deletes this source (also stops playing it's sounds).
     */
    public void delete(){
        stop();
        AL10.alDeleteSources(sourceId);
    }

    /**
     * Pauses this source so it doesn't emit any sounds.
     */
    public void pause(){
        AL10.alSourcePause(sourceId);
    }

    /**
     * Continues playing the current sound after it has been paused.
     */
    public void continuePlaying(){
        AL10.alSourcePlay(sourceId);
    }

    /**
     * Completely stops playing, to resume playing the play method needs to be called again with a buffer.
     */
    public void stop(){
        AL10.alSourceStop(sourceId);
    }

    /**
     * Sets whether the sound of this source should be looped after it's over.
     * @param looping - {@code true} if the source should start playing from the beginning after the sound is over, {@code false} if the source
     *                  should stop playing after the sound is over.
     */
    public void setLooping(boolean looping){
        AL10.alSourcei(sourceId, AL10.AL_LOOPING, looping ? AL10.AL_TRUE : AL10.AL_FALSE);
    }

    /**
     *
     * @return - {@code true} if this source is currently playing a sound.
     */
    public boolean isPlaying(){
        return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    /**
     * Set the volume of this source.
     * @param volume - New volume for this source.
     */
    public void setVolume(float volume){
        AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
    }

    /**
     * Set the position of this source.
     * @param x - x-position of this source (world coordinates)
     * @param y - y-position of this source (world coordinates)
     * @param z - z-position of this source (world coordinates)
     */
    public void setPosition(float x, float y, float z){
        AL10.alSource3f(sourceId, AL10.AL_POSITION, x, y, z);
    }
}
