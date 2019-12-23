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

public class AudioMaster {

    private static long device;
    private static long context;

    private static float SFXVolume;

    private static List<Integer> buffers = new ArrayList<>();
    private static List<Source> sources = new ArrayList<>();

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

    public static int loadSound(String file){
        int buffer = AL10.alGenBuffers();
        buffers.add(buffer);
        WaveData waveFile = WaveData.create("/com/battleships/gui/res/sounds/" + file + ".wav");
        AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
        waveFile.dispose();
        return buffer;
    }

    public static void changeVolume(float volume){
        SFXVolume = volume;
        for(Source s : sources){
            s.setVolume(volume);
        }
    }

    public static float getSFXVolume() {
        return SFXVolume;
    }

    public static void addSource(Source source){
        sources.add(source);
    }

    public static void cleanUp(){
        for(Source source : sources)
            source.delete();
        for(int buffer : buffers)
            AL10.alDeleteBuffers(buffer);
        ALC10.alcDestroyContext(context);
        ALC10.alcCloseDevice(device);
    }
}
