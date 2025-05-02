package com.xebisco.yield2d.logic;

public abstract class BasicLogic implements IBasicLogic {
    private boolean loaded, loading, unloading;
    private int frames;

    @Override
    public void load() {
        setLoaded(false);
        setLoading(true);

        setFrames(0);

        IBasicLogic.super.load();
        setLoaded(true);
        setLoading(false);
    }

    @Override
    public void unload() {
        setUnloading(true);
        IBasicLogic.super.unload();
        setLoaded(false);
        setUnloading(false);
    }

    @Override
    public void update(float deltaTime) {
        IBasicLogic.super.update(deltaTime);
        setFrames(getFrames() + 1);
    }

    public boolean isLoaded() {
        return loaded;
    }

    private void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isLoading() {
        return loading;
    }

    private void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean isUnloading() {
        return unloading;
    }

    private void setUnloading(boolean unloading) {
        this.unloading = unloading;
    }
    
    public int getFrames() {
        return frames;
    }

    private void setFrames(int frames) {
        this.frames = frames;
    }
}
