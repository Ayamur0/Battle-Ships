package com.battleships.gui.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;


/**
 * WaveData of a .wav file.
 * Contains all necessary data of a .wav file so OpenAl is able to use this file as a sound buffer.
 *
 * @author Tim Staudenmaier
 */
public class WaveData {

    /**
     * Information on the format of the read .wav file.
     */
    final int format;
    /**
     * Information on the samplerate of the read .wav file.
     */
    final int samplerate;
    /**
     * Information on the totalBytes of the read .wav file.
     */
    final int totalBytes;
    /**
     * Information on the bytesPerFrame of the read .wav file.
     */
    final int bytesPerFrame;
    /**
     * ByteBuffer containing all the bytes of the actual sound of the .wav file.
     */
    final ByteBuffer data;

    /**
     * AudioInputStream needed to read a .wav file.
     */
    private final AudioInputStream audioStream;
    /**
     * Data array the audioStream can read to.
     */
    private final byte[] dataArray;

    /**
     * Create a waveData containing all data needed, to use a .wav file as sound.
     * @param stream Input stream containing the .wav file.
     */
    private WaveData(AudioInputStream stream) {
        this.audioStream = stream;
        AudioFormat audioFormat = stream.getFormat();
        format = getOpenAlFormat(audioFormat.getChannels(), audioFormat.getSampleSizeInBits());
        this.samplerate = (int) audioFormat.getSampleRate();
        this.bytesPerFrame = audioFormat.getFrameSize();
        this.totalBytes = (int) (stream.getFrameLength() * bytesPerFrame);
        this.data = BufferUtils.createByteBuffer(totalBytes);
        this.dataArray = new byte[totalBytes];
        loadData();
    }

    /**
     * Removes this waveData from memory.
     */
    protected void dispose() {
        try {
            audioStream.close();
            data.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load data from .wav file into this WaveData.
     * @return A ByteBuffer containing all the data.
     */
    private ByteBuffer loadData() {
        try {
            int bytesRead = audioStream.read(dataArray, 0, totalBytes);
            data.clear();
            data.put(dataArray, 0, bytesRead);
            data.flip();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't read bytes from audio stream!");
        }
        return data;
    }


    /**
     * Create WaveData from a .wav file.
     * @param file File to create the WaveData for.
     * @return WaveData containing all information about the passed .wav file.
     */
    public static WaveData create(String file){
        InputStream stream = WaveData.class.getResourceAsStream(file);
        if(stream==null){
            System.err.println("Couldn't find file: "+file);
            return null;
        }
        InputStream bufferedInput = new BufferedInputStream(stream);
        AudioInputStream audioStream = null;
        try {
            audioStream = AudioSystem.getAudioInputStream(bufferedInput);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return new WaveData(audioStream);
    }

    /**
     *
     * @param channels How many channels the audio should use (1 for mono, 2 or more stereo)
     * @param bitsPerSample How many bits are used per sample (8 or 16)
     * @return An Integer representing the audio format in OpenAl Constants ({@code AL10.AL_FORMAT_MONO16} or  {@code AL10.AL_FORMAT_STEREO16})
     */
    private static int getOpenAlFormat(int channels, int bitsPerSample) {
        if (channels == 1) {
            return bitsPerSample == 8 ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_MONO16;
        } else {
            return bitsPerSample == 8 ? AL10.AL_FORMAT_STEREO8 : AL10.AL_FORMAT_STEREO16;
        }
    }

}
