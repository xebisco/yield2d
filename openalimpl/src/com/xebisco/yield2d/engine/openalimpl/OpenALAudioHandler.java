package com.xebisco.yield2d.engine.openalimpl;

import com.xebisco.yield2d.engine.AudioFile;
import com.xebisco.yield2d.engine.AudioHandler;
import com.xebisco.yield2d.engine.AudioPlayer;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class OpenALAudioHandler extends AudioHandler {

    private Map<AudioPlayer, Integer> playerIds;
    private Map<AudioFile, Integer> bufferIds;
    private long device, context;

    public int setupPlayer(AudioPlayer audioPlayer) {
        Integer alPlayer = playerIds.get(audioPlayer);
        if (alPlayer == null) {
            alPlayer = alGenSources();
            playerIds.put(audioPlayer, alPlayer);
        }
        return alPlayer;
    }

    public int setupBuffer(AudioFile audioFile) {
        Integer alBuffer = bufferIds.get(audioFile);
        if (alBuffer == null) {
            alBuffer = alGenBuffers();
            bufferIds.put(audioFile, alBuffer);
            InputStream in = getApplication().getFileHandler().openInputStream(audioFile.getPath());
            WaveData waveData = WaveData.create(in);
            if(waveData == null) throw new IllegalStateException("WAVE DATA IS NULL.");
            alBufferData(alBuffer, waveData.format, waveData.data, waveData.samplerate);
            getApplication().getFileHandler().closeInputStream(in);
        }
        return alBuffer;
    }

    @Override
    public void load() {
        playerIds = new HashMap<>();
        bufferIds = new HashMap<>();

        device = alcOpenDevice((ByteBuffer) null);
        if (device == 0) {
            throw new IllegalStateException("Failed to open the default OpenAL device.");
        }
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        context = alcCreateContext(device, (IntBuffer) null);
        if (context == 0) {
            throw new IllegalStateException("Failed to create OpenAL context.");
        }
        alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);
    }

    @Override
    public void play(AudioPlayer audioPlayer) {
        int pid = setupPlayer(audioPlayer);
        alSourcei(pid, AL_BUFFER, setupBuffer(audioPlayer.getAudioFile()));
        alSourcePlay(pid);
    }

    @Override
    public void pause(AudioPlayer audioPlayer) {
        alSourcePause(setupPlayer(audioPlayer));
    }

    @Override
    public void stop(AudioPlayer audioPlayer) {
        alSourceStop(setupPlayer(audioPlayer));
    }

    @Override
    public void destroy(AudioPlayer audioPlayer) {
        alDeleteSources(playerIds.get(audioPlayer));
    }

    @Override
    public void destroy(AudioFile audioFile) {
        alDeleteBuffers(bufferIds.get(audioFile));
    }

    @Override
    public boolean isPlaying(AudioPlayer audioPlayer) {
        return alGetSourcei(setupPlayer(audioPlayer), AL_SOURCE_STATE) == AL_PLAYING;
    }

    @Override
    public void update(AudioPlayer audioPlayer) {
        int id = setupPlayer(audioPlayer);
        alSourcei(id, AL_LOOPING, audioPlayer.isLoop() ? AL_TRUE : AL_FALSE);
        alSourcef(id, AL_GAIN, (float) audioPlayer.getGain());
        float pan = (float) (audioPlayer.getPan() * 0.5f); // 0.5 = sin(30') for a +/- 30 degree arc
        alSourcei(id, AL_SOURCE_RELATIVE, 1);
        alSource3f(id, AL_POSITION, pan, 0, (float) -Math.sqrt(1.0f - pan * pan));
    }
}
