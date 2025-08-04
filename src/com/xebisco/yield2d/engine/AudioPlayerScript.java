package com.xebisco.yield2d.engine;

public class AudioPlayerScript extends Script {
    @Editable
    @CantBeNull
    private AudioFile audioFile;

    @Editable
    private double gain = 1, pan = 0;

    @Editable
    private boolean playOnInit, loop;

    @Override
    public void init() {
        if (playOnInit)
            play();
    }

    @Override
    public void update(TimeSpan elapsed) {
        getApplication().getAudioHandler().update(this);
    }

    public void play() {
        getApplication().getAudioHandler().play(this);
    }

    public void pause() {
        getApplication().getAudioHandler().pause(this);
    }

    public void stop() {
        getApplication().getAudioHandler().stop(this);
    }

    @Override
    public void destroy() {
        getApplication().getAudioHandler().destroy(this);
    }

    public AudioFile getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(AudioFile audioFile) {
        stop();
        this.audioFile = audioFile;
    }

    public boolean isPlayOnInit() {
        return playOnInit;
    }

    public void setPlayOnInit(boolean playOnInit) {
        this.playOnInit = playOnInit;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public double getGain() {
        return gain;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    public double getPan() {
        return pan;
    }

    public void setPan(float pan) {
        this.pan = pan;
    }
}
