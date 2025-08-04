package com.xebisco.yield2d.engine;

public abstract class AudioHandler extends ApplicationHandler {

    public abstract void play(AudioPlayerScript audioPlayer);
    public abstract void pause(AudioPlayerScript audioPlayer);
    public abstract void stop(AudioPlayerScript audioPlayer);
    public abstract void destroy(AudioPlayerScript audioPlayer);
    public abstract void destroy(AudioFile audioFile);
    public abstract boolean isPlaying(AudioPlayerScript audioPlayer);
    public abstract void update(AudioPlayerScript audioPlayer);

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
