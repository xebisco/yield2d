package com.xebisco.yield2d.engine;

public abstract class AudioHandler extends ApplicationHandler {

    public abstract void play(AudioPlayer audioPlayer);
    public abstract void pause(AudioPlayer audioPlayer);
    public abstract void stop(AudioPlayer audioPlayer);
    public abstract void destroy(AudioPlayer audioPlayer);
    public abstract void destroy(AudioFile audioFile);
    public abstract boolean isPlaying(AudioPlayer audioPlayer);
    public abstract void update(AudioPlayer audioPlayer);

    @Override
    public void load() {

    }

    @Override
    public void init() {

    }

    @Override
    public void update(TimeSpan elapsed) {

    }

    @Override
    public void destroy() {

    }
}
