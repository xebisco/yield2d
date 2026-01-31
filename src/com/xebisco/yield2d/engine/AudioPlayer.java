package com.xebisco.yield2d.engine;

public class AudioPlayer extends Script {
    @Editable
    @CantBeNull
    private AudioFile audioFile;

    @Editable
    private double gain = 1, pan = 0, pitch;

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

    public AudioPlayer setAudioFile(AudioFile audioFile) {
        stop();
        this.audioFile = audioFile;
        return this;
    }

    public double getGain() {
        return gain;
    }

    public AudioPlayer setGain(double gain) {
        this.gain = gain;
        return this;
    }

    public double getPan() {
        return pan;
    }

    public AudioPlayer setPan(double pan) {
        this.pan = pan;
        return this;
    }

    public boolean isPlayOnInit() {
        return playOnInit;
    }

    public AudioPlayer setPlayOnInit(boolean playOnInit) {
        this.playOnInit = playOnInit;
        return this;
    }

    public boolean isLoop() {
        return loop;
    }

    public AudioPlayer setLoop(boolean loop) {
        this.loop = loop;
        return this;
    }

    public double getPitch() {
        return pitch;
    }

    public AudioPlayer setPitch(double pitch) {
        this.pitch = pitch;
        return this;
    }
}
