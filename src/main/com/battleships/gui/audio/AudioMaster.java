package com.battleships.gui.audio;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;
import org.lwjgl.stb.STBVorbisInfo;
import org.w3c.dom.stylesheets.LinkStyle;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * This AudioMaster is the main class for all audio related things.
 * It is always needed if any sound should be played in the game.
 *
 * @author Tim Staudenmaier
 */
public class AudioMaster {

    private static long device;
    private static long context;

    /**
     * Current default volume of all sources.
     */
    private static float SFXVolume = 1;

    /**
     * List containing all loaded buffers for sounds.
     */
    private static List<Integer> buffers = new ArrayList<>();
    /**
     * List containing all currently existing sources.
     */
    private static List<Source> sources = new ArrayList<>();

    /**
     * Initialize OpenAl for playing Sounds. This method needs to be called
     * before any sound can be used.
     */
    public static void init() {
        device = ALC10.alcOpenDevice((ByteBuffer)null);
        if (device == ALC10.ALC_INVALID_DEVICE)
            throw new IllegalStateException("Failed to open the default device.");

        ALCCapabilities deviceCaps = ALC.createCapabilities(device);

        context = ALC10.alcCreateContext(device, (IntBuffer)null);
        if (context == ALC10.ALC_INVALID_CONTEXT)
            throw new IllegalStateException("Failed to create an OpenAL context.");

        ALC10.alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);

        AL10.alDistanceModel(AL11.AL_EXPONENT_DISTANCE_CLAMPED);
    }

    /**
     * Set the data of the listener. Sound gets played so it sounds correctly in relation to
     * the position and rotation of the listener.
     * @param x - x-coordinate of the listeners position (in world coordinates)
     * @param y - y-coordinate of the listeners position (in world coordinates)
     * @param z - z-coordinate of the listeners position (in world coordinates)
     * @param pitch - pitch of the listener
     * @param yaw - yaw of the listener
     */
    public static void setListenerData(float x, float y, float z, float pitch, float yaw){
        AL10.alListener3f(AL10.AL_POSITION, x, y, z);
        FloatBuffer orientation = BufferUtils.createFloatBuffer(6);
        pitch = (float)Math.toRadians(pitch);
        yaw = (float)Math.toRadians(yaw);
        //orientation.put(new float[] {(float) Math.sin(Math.toRadians(yaw)),-(float)Math.sin(Math.toRadians(pitch) * (float)Math.cos(Math.toRadians(yaw))),-(float)Math.cos(Math.toRadians(pitch) * (float)Math.cos(Math.toRadians(yaw))),0,1,0});
        orientation.put(new float[] {(float) (Math.cos(pitch) * Math.sin(yaw)), (float) Math.sin(pitch), (float) (Math.cos(pitch) * Math.cos(yaw)),0,-1,0});
        orientation.flip();
        AL10.alListenerfv(AL10.AL_ORIENTATION, orientation);
        AL10.alListener3f(AL10.AL_VELOCITY,0,0,0);
    }

    /**
     * Loads a sound file, to make it playable through a source.
     * Sound files need to be in .wav format.
     * @param file - Name of the sound file to load.
     * @return - ID of the buffer containing the data for the sound file. Needs to be given to a source
     *          to play the sound.
     */
    public static int loadSound(String file){
        int buffer = AL10.alGenBuffers();
        buffers.add(buffer);
        WaveData waveFile = WaveData.create("/com/battleships/gui/res/sounds/" + file + ".wav");
        AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
        waveFile.dispose();
        return buffer;
    }

    /**
     * Change the volume of all sources and set standard volume for new sources.
     * @param volume - New volume (0 for no sound, 1 is standard)
     */
    public static void changeVolume(float volume){
        SFXVolume = volume;
        for(Source s : sources){
            s.setVolume(volume);
        }
    }

    /**
     *
     * @return - Current volume level.
     */
    public static float getSFXVolume() {
        return SFXVolume;
    }

    /**
     * Adds a source to this audioManager, so it can control the volume of the source and cleanUp the source on exit.
     * @param source - The source to add
     */
    public static void addSource(Source source){
        sources.add(source);
    }

    /**
     * Cleans up all OpenAl and sound related stuff, needs to be called on program exit.
     */
    public static void cleanUp(){
        for(Source source : sources)
            source.delete();
        for(int buffer : buffers)
            AL10.alDeleteBuffers(buffer);
        ALC10.alcDestroyContext(context);
        ALC10.alcCloseDevice(device);
    }
}
